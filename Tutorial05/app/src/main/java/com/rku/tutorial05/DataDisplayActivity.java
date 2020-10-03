package com.rku.tutorial05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class DataDisplayActivity extends AppCompatActivity {
    //*******************"Tutorial 08"*******************
    TextView data, onlineData; //onlineData for Tutorial 10
    MyDatabaseHelper myDB;
    String userdata = "";
    String valUserData = ""; //For Tutorial 10
    //*******************"Tutorial 08"*******************
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        //*******************"Tutorial 08 (Offline Database userdata)"*******************
        data = findViewById(R.id.DisplayTextView);
        Intent intent = getIntent();
        //*******************"Tutorial 08"*******************

        //*******************"Tutorial 10 (Online website dataView from json file)"*******************
        onlineData = findViewById(R.id.onlineDataTextView);
        temp = intent.getIntExtra("temp",0);
        if(temp == 4){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            data.setVisibility(View.GONE);

        }
        else {
            //*******************"Tutorial 08 (dataView from Offline Database)"*******************
            myDB = new MyDatabaseHelper(this);
            String username = intent.getStringExtra("username");
            Toast.makeText(DataDisplayActivity.this, username, Toast.LENGTH_SHORT).show();
            Cursor cursor = myDB.getPartUserData(username);
            cursor.moveToFirst();
            userdata += cursor.getString(1);
            userdata += "\n" + cursor.getString(2);
            userdata += "\n" + cursor.getString(3);
            userdata += "\n" + cursor.getString(4);
            userdata += "\n" + cursor.getString(5);
            userdata += "\n" + cursor.getString(6);
            userdata += "\n" + cursor.getString(7);
            data.setText(userdata);
            onlineData.setVisibility(View.GONE);
            //*******************"Tutorial 08"*******************
        }
        //*******************"Tutorial 10"*******************
    }

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
}