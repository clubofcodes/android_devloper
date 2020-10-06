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
    TextView conView,fullname,gen,phone,email,city,field, onlineData; //onlineData for Tutorial 10
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

        //*******************"Tutorial 10 (Online website dataView from json file)"*******************
        onlineData = findViewById(R.id.onlineDataTextView);
        temp = intent.getIntExtra("temp",0);
        if(temp == 4){
            int position = intent.getIntExtra("userPosition", 0);
            try {
                JSONObject object = MyUtil.jsonArray.getJSONObject(position);
                valUserData += "Id : " + object.getString("id");
                valUserData += "\nName : " + object.getString("name");
                valUserData += "\nUsername : " + object.getString("username");
                valUserData += "\nEmail : " + object.getString("email");
                JSONObject addressObj = object.getJSONObject("address");
                valUserData += "\nAddress : " +
                        addressObj.getString("street") + ", " +
                        addressObj.getString("suite") + ", " +
                        addressObj.getString("city") + ", " +
                        addressObj.getString("zipcode");
                valUserData += "\nPhone : " + object.getString("phone");
                valUserData += "\nWebsite : " + object.getString("website");
                JSONObject companyObj = object.getJSONObject("company");
                valUserData += "\nAddress : " +
                        companyObj.getString("name") + ", " +
                        companyObj.getString("catchPhrase") + ", " +
                        companyObj.getString("bs");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            onlineData.setText(valUserData);
            conView.setVisibility(View.GONE);
            fullname.setVisibility(View.GONE);
            gen.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);

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
            onlineData.setVisibility(View.GONE);
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