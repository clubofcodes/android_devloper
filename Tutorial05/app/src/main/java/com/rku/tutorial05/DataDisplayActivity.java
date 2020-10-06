package com.rku.tutorial05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import classes.MyDatabaseHelper;
import classes.MyUtil;

public class DataDisplayActivity extends AppCompatActivity {
    //*******************"Tutorial 08"*******************
    TextView conView,fullname,gen,phone,email,city,field, siteTitle,site,loc,comp_add;
    MyDatabaseHelper myDB;
    String valUserData = ""; //valUserData for Tutorial 10
    //*******************"Tutorial 08"*******************
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        //*******************"Tutorial 08 (Offline Database userdata)"*******************
        conView = findViewById(R.id.shortNameText);
        fullname = findViewById(R.id.fullNameText);
        gen = findViewById(R.id.genText);
        phone = findViewById(R.id.phoneNumText);
        email = findViewById(R.id.emailText);
        city = findViewById(R.id.cityText);
        field = findViewById(R.id.fieldText);
        LinearLayout ll = (LinearLayout) findViewById(R.id.contactIconsLayout);
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
            int position = intent.getIntExtra("userPosition", 0);
            try {
                JSONObject object = MyUtil.jsonArray.getJSONObject(position);
                JSONObject addressObj = object.getJSONObject("address");
                JSONObject companyObj = object.getJSONObject("company");

                conView.setText(object.getString("id")); //id
                fullname.setText(object.getString("name"));
                gen.setText(object.getString("username")); //nickname
                phone.setText(object.getString("phone"));
                email.setText(object.getString("email"));
                loc.setText("Address"); //user address
                city.setText(addressObj.getString("street") + ", " +
                        addressObj.getString("suite") + ", " +
                        addressObj.getString("city") + ", " +
                        addressObj.getString("zipcode"));
                comp_add.setText("Company Address");
                field.setText(companyObj.getString("name") + ", " +
                        companyObj.getString("catchPhrase") + ", " +
                        companyObj.getString("bs"));
                site.setText(object.getString("website"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            //*******************"Tutorial 08 (dataView from Offline Database)"*******************
            myDB = new MyDatabaseHelper(this);
            String username = intent.getStringExtra("username");
            Toast.makeText(DataDisplayActivity.this, username, Toast.LENGTH_SHORT).show();
            Cursor cursor = myDB.getPartUserData(username);
            cursor.moveToFirst();
            conView.setText(cursor.getString(1).substring(0,1)+cursor.getString(2).substring(0,1));
            fullname.setText(cursor.getString(1)+" "+cursor.getString(2));
            gen.setText(cursor.getString(6));
            phone.setText(cursor.getString(4));
            email.setText(cursor.getString(3));
            city.setText(cursor.getString(7));
            field.setText(cursor.getString(5));
            site.setVisibility(View.GONE);
            siteTitle.setVisibility(View.GONE);
            //*******************"Tutorial 08"*******************
        }
        //*******************"Tutorial 10"*******************
    }

    public void back_activity(View view) {
        onBackPressed();
    }
    //*******************"Tutorial 10 (Back button/key event management)"*******************
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),WelcomeUsersActivity.class);
        if(temp == 4){
            intent.putExtra("temp",3);
        }else {
            intent.putExtra("temp",1);
        }
        startActivity(intent);
        this.finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    //*******************"Tutorial 10"*******************
}