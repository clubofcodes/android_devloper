package com.rku.tutorial05;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import classes.MyDatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    //*******************"Tutorial 06"*******************
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //*******************"Tutorial 06"*******************
    //*****************"Tutorial 07"***********************
    MyDatabaseHelper myDB;
    Cursor cursor;
    //*****************"Tutorial 07"***********************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        login=findViewById(R.id.Loginbutton);

        //*******************"Tutorial 06"*******************
        preferences = getSharedPreferences("session", MODE_PRIVATE);
        editor = preferences.edit();
        String pref_email = preferences.getString("email","");
        if(!pref_email.equals("")){
            Intent intent = new Intent(LoginActivity.this, WelcomeUsersActivity.class);
            startActivity(intent);
            finish();
        }
        //*******************"Tutorial 06"*******************

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //*****************"Tutorial 07"***********************
                myDB = new MyDatabaseHelper(LoginActivity.this);
                cursor = myDB.checkLogin(username.getText().toString(), password.getText().toString());
                if(cursor.getCount()!=0) {
                    //*****************"Tutorial 07"***********************
                    Toast.makeText(LoginActivity.this,"You have Authenticated Successfully", Toast.LENGTH_SHORT).show();
                    //*******************"Tutorial 06"*******************
                    editor.putString("email",username.getText().toString().trim());
                    editor.commit();
                    //*******************"Tutorial 06"*******************
                    Intent intent = new Intent(getApplicationContext(), WelcomeUsersActivity.class);
                    intent.putExtra("temp",1);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,"Username or Password is incorrect",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void signupClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
        finish();
    }
}