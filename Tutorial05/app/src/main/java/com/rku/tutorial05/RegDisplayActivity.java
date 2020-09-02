package com.rku.tutorial05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RegDisplayActivity extends AppCompatActivity {
    TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_display);

        data = findViewById(R.id.DisplayTextView);

        String Fname,Lname,Email,Pass,Gender, City, prof_status = "Inactive";
        Boolean status;
        Intent intent = getIntent();
        Fname = intent.getStringExtra("fname");
        Lname = intent.getStringExtra("lname");
        Email = intent.getStringExtra("email");
        Pass = intent.getStringExtra("pass");
        Gender = intent.getStringExtra("gen");
        City = intent.getStringExtra("city");
        status = intent.getBooleanExtra("status",false);
        if(status){
            prof_status = "Active";
        }

        data.setText("First Name : " + Fname+"\n"+"Last Name : " + Lname+"\n"+"Username : " + Email+"\n"+"Password : " + Pass+"\n"+"Gender : " + Gender+"\n"+"City : " +City+"\n"+"Status : "+prof_status);


    }
}