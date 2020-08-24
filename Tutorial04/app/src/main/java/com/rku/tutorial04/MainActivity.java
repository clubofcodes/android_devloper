package com.rku.tutorial04;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        username=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        login=findViewById(R.id.Loginbutton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches() && password.getText().toString().length()>=8)
                {
                    Toast.makeText(MainActivity.this,"You have Authenticated Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    intent.putExtra ( "Name", username.getText().toString());
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Username or Password is incorrect",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}