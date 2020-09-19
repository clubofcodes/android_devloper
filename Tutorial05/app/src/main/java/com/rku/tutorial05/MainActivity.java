package com.rku.tutorial05;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    TextView signUp;
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
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        login=findViewById(R.id.Loginbutton);
        signUp=findViewById(R.id.signUpLink);

        //*******************"Tutorial 06"*******************
        preferences = getSharedPreferences("session", MODE_PRIVATE);
        editor = preferences.edit();
        String pref_email = preferences.getString("email","");
        if(!pref_email.equals("")){
            Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
        //*******************"Tutorial 06"*******************


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //*****************"Tutorial 07"***********************
                myDB = new MyDatabaseHelper(MainActivity.this);
                cursor = myDB.checkLogin(username.getText().toString(), password.getText().toString());
                if(cursor.getCount()!=0) {
                    //*****************"Tutorial 07"***********************
                    Toast.makeText(MainActivity.this,"You have Authenticated Successfully", Toast.LENGTH_SHORT).show();
                    //*******************"Tutorial 06"*******************
                    editor.putString("email",username.getText().toString().trim());
                    editor.commit();
                    //*******************"Tutorial 06"*******************
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this,"Username or Password is incorrect",Toast.LENGTH_LONG).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}