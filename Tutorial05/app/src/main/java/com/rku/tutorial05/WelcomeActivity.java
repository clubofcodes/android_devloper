package com.rku.tutorial05;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class WelcomeActivity extends AppCompatActivity {
    TextView welUser;
    //*****************"Tutorial 06"***********************
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //*****************"Tutorial 06"***********************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welUser = findViewById(R.id.Welcomemsg);

        //*****************"Tutorial 06"***********************
        preferences = getSharedPreferences("session", MODE_PRIVATE);
        welUser.setText("Welcome, "+preferences.getString("email",""));
        editor = preferences.edit();
        //*******************"Tutorial 06"*******************
    }

    //**************"Used by Tutorial 06"****************
//    public void logout(View view) {
//        editor.remove("email");
//        editor.commit();
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent);
//        finish();
//    }

    //*****************"Tutorial 06"***********************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.abt_menu:
                Toast.makeText(WelcomeActivity.this, "You clicked about us..", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_menu:
                editor.remove("email");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //*****************"Tutorial 06"***********************
}