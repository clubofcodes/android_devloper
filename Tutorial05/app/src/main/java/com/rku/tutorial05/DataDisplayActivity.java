package com.rku.tutorial05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import classes.MyDatabaseHelper;
import classes.MyUtil;

public class DataDisplayActivity extends AppCompatActivity {
    //*******************"Tutorial 08"*******************
    TextView conView,fullname,gen,phone,email,city,field,siteTitle,site,loc,comp_add,nxtLink,prevLink,delLink;
    MyDatabaseHelper myDB;
    String username;
    //*******************"Tutorial 08"*******************
    int temp,i;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);
        preferences = getSharedPreferences("session", MODE_PRIVATE);
        editor = preferences.edit();

        nxtLink = findViewById(R.id.nxtDataClickbtn);
        prevLink = findViewById(R.id.prevDataClickbtn);
        delLink = findViewById(R.id.delClickbtn);

        //*******************"Tutorial 08 (Offline Database userdata)"*******************
        conView = findViewById(R.id.shortNameText);
        fullname = findViewById(R.id.fullNameText);
        gen = findViewById(R.id.genText);
        phone = findViewById(R.id.phoneNumText);
        email = findViewById(R.id.emailText);
        city = findViewById(R.id.cityText);
        field = findViewById(R.id.fieldText);
        Intent intent = getIntent();
        //*******************"Tutorial 08"*******************

        //*******************"Tutorial 11(Online data title change)"*******************
        loc = findViewById(R.id.cityTitleText);
        comp_add = findViewById(R.id.fieldTitleText);
        site = findViewById(R.id.siteText);
        siteTitle = findViewById(R.id.siteTitleText);
        //*******************"Tutorial 11"*******************

        //*******************"Tutorial 10 (Online website dataView from json file)"*******************
        temp = intent.getIntExtra("temp",0);
        if(temp == 4){
            TextView edit = findViewById(R.id.editClickbtn);
            edit.setVisibility(View.GONE);
            int position = intent.getIntExtra("userPosition", 0);
            try {
                JSONObject object = MyUtil.jsonArray.getJSONObject(position);
                JSONObject addressObj = object.getJSONObject("address");
                JSONObject companyObj = object.getJSONObject("company");
                //dataView changes are for good view of user details in Tut11.
                conView.setText(object.getString("id")); //id
                fullname.setText(object.getString("name"));
                gen.setText(object.getString("username")); //nickname
                phone.setText(object.getString("phone"));
                email.setText(object.getString("email"));
                loc.setText("Address"); //user address title
                city.setText(addressObj.getString("street") + ", " +
                        addressObj.getString("suite") + ", " +
                        addressObj.getString("city") + ", " +
                        addressObj.getString("zipcode")); //user address
                comp_add.setText("Company Address"); //Company address title
                field.setText(companyObj.getString("name") + ", " +
                        companyObj.getString("catchPhrase") + ", " +
                        companyObj.getString("bs")); //company address
                site.setText(object.getString("website"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            //*******************"Tutorial 08 (dataView from Offline Database)"*******************
            myDB = new MyDatabaseHelper(this);
            username = intent.getStringExtra("username");
            Cursor cursor = myDB.getPartUserData(username);
            cursor.moveToFirst();
            conView.setText(cursor.getString(1).substring(0,1)+cursor.getString(2).substring(0,1)); //shortName
            fullname.setText(cursor.getString(1)+" "+cursor.getString(2));
            gen.setText(cursor.getString(6));
            phone.setText(cursor.getString(8));
            email.setText(cursor.getString(3));
            city.setText(cursor.getString(7));
            field.setText(cursor.getString(5));
            site.setVisibility(View.GONE);
            siteTitle.setVisibility(View.GONE);
            //*******************"Tutorial 08"*******************
            int size = myDB.getUserList().size(); --size;
            int loopPos = getIntent().getIntExtra("loopPos",myDB.getUserList().size());
            int pos = getIntent().getIntExtra("pos",myDB.getUserList().size());
            if(size == loopPos || size == pos){
                nxtLink.setVisibility(View.GONE);
            }else if(pos == 0 || loopPos == 0){
                prevLink.setVisibility(View.GONE);
            }
        }
        //*******************"Tutorial 10"*******************

        nxtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (i=0;i<myDB.getUserList().size();i++){
                    if(username.equals(myDB.getUserList().get(i))){
                        Intent nxtIntent = new Intent(DataDisplayActivity.this,DataDisplayActivity.class);
                        nxtIntent.putExtra("username",myDB.getUserList().get(++i));
                        nxtIntent.putExtra("loopPos",i);
                        startActivity(nxtIntent);
                        finish();
                    }
                }
            }
        });

        prevLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prevSize = myDB.getUserList().size(); --prevSize;
                for (i=prevSize;i>0;i--){
                    if(username.equals(myDB.getUserList().get(i))){
                        Intent prevIntent = new Intent(DataDisplayActivity.this,DataDisplayActivity.class);
                        prevIntent.putExtra("username",myDB.getUserList().get(--i));
                        prevIntent.putExtra("loopPos",i);
                        startActivity(prevIntent);
                        finish();
                    }
                }
            }
        });

        delLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(DataDisplayActivity.this,R.style.DialogTheme);
                builder.setTitle("Warning!!")
                        .setMessage("Are You Sure You Want To Delete.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDB.user_del(username);
                                startActivity(new Intent(DataDisplayActivity.this,WelcomeUsersActivity.class));
                                finish();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog del_dialog = builder.create();
                del_dialog.show();
            }
        });
    }

    public void back_activity(View view) {
        onBackPressed();
    }
    public void edit_data(View view) {
        Intent setDataIntent = new Intent(DataDisplayActivity.this,SignupActivity.class);
        setDataIntent.putExtra("update",1);
        Cursor cursor = myDB.getPartUserData(username);
        cursor.moveToFirst();
        setDataIntent.putExtra("username",username);
        setDataIntent.putExtra("pwd",cursor.getString(4));
        setDataIntent.putExtra("firstname",cursor.getString(1));
        setDataIntent.putExtra("lastname",cursor.getString(2));
        setDataIntent.putExtra("gender",cursor.getString(6));
        setDataIntent.putExtra("number",cursor.getString(8));
        setDataIntent.putExtra("city",cursor.getString(7));
        setDataIntent.putExtra("field",cursor.getString(5));
        startActivity(setDataIntent);
        finish();
    }

    //*******************"Tutorial 10 (Back button/key event management)"*******************
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(temp == 4){
            editor.putInt("temp",3);
        }else {
            editor.putInt("temp",1);
        }
        editor.commit();
        startActivity(new Intent(getApplicationContext(),WelcomeUsersActivity.class));
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    //*******************"Tutorial 10"*******************
}