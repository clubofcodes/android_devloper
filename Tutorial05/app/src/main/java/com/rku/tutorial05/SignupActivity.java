package com.rku.tutorial05;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import validations.Validation;

public class SignupActivity extends AppCompatActivity {
    Button signup;
    EditText fname,lname,email,pass;
    Switch field;
    RadioGroup gen_grp;
    RadioButton gender;
    Spinner city;
    CheckBox status;
    Validation val = new Validation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //EditText Fields
        fname = findViewById(R.id.signupName);
        lname = findViewById(R.id.signupSurname);
        email = findViewById(R.id.signupEmail);
        pass = findViewById(R.id.signupPass);
        field = findViewById(R.id.signupFieldSwitch);
        gen_grp = findViewById(R.id.genderRadioGroup);
        city = findViewById(R.id.citySpinner);
        status = findViewById(R.id.statusCheckBox);
        signup = findViewById(R.id.signupbtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstN = fname.getText().toString();
                String lastN = lname.getText().toString();
                String mailId = email.getText().toString();
                String password = pass.getText().toString();
                boolean branch = field.isChecked();
                int id = gen_grp.getCheckedRadioButtonId();
                gender = findViewById(id);
                String loc = city.getSelectedItem().toString(); //loc=location
                Boolean active = status.isChecked();
                if(!val.empty(firstN) && !val.empty(lastN) && !val.empty(mailId) && !val.empty(password) && Patterns.EMAIL_ADDRESS.matcher(mailId).matches() && password.length()>=8){
                    Intent i = new Intent(SignupActivity.this,RegDisplayActivity.class);
                    i.putExtra("fname",firstN);
                    i.putExtra("lname",lastN);
                    i.putExtra("email",mailId);
                    i.putExtra("pass",password);
                    i.putExtra("gen",gender.getText().toString());
                    i.putExtra("field",branch);
                    i.putExtra("city",loc);
                    i.putExtra("status",active);
                    startActivity(i);
                }else{
                    if(val.empty(firstN)){
                        fname.setError("Enter your first name");
                    }
                    if(val.empty(lastN)){
                        lname.setError("Enter your last name");
                    }
                    if(val.empty(mailId)){
                        email.setError("Enter your email id");
                    }else {
                        if(!Patterns.EMAIL_ADDRESS.matcher(mailId).matches()){
                            email.setError("Enter a valid email address");
                            email.requestFocus();
                        }
                    }
                    if(val.empty(password)){
                        pass.setError("Enter your password");
                    }else{
                        if(password.length()<8){
                            pass.setError("Password should be more than 8 char long");
                        }
                    }
                }

            }
        });
    }
}