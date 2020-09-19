package com.rku.tutorial05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class RegDisplayActivity extends AppCompatActivity {
    //*******************"Tutorial 08"*******************
    TextView data;
    MyDatabaseHelper myDB;
    String userdata = "";
    //*******************"Tutorial 08"*******************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_display);

        //*******************"Tutorial 08"*******************
        data = findViewById(R.id.DisplayTextView);
        myDB = new MyDatabaseHelper(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        Toast.makeText(RegDisplayActivity.this, username, Toast.LENGTH_SHORT).show();
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
        //*******************"Tutorial 08"*******************

    }
}