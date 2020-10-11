package com.rku.tutorial05;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
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

import com.hbb20.CountryCodePicker;

import classes.MyDatabaseHelper;
import validations.Validation;

public class SignupActivity extends AppCompatActivity {
    Button signup, update;
    TextView reg_title,loginLink;
    EditText fname,lname,email,pass,number;
    Switch field;
    RadioGroup gen_grp;
    RadioButton gender;
    Spinner city;
    CheckBox status;
    Validation val = new Validation();
    //*****************"Tutorial 07***********************
    MyDatabaseHelper myDb;
    //*****************"Tutorial 07"***********************
    CountryCodePicker ccp;
    String numWithCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Join " + getString(R.string.app_name));

        //EditText Fields
        reg_title = findViewById(R.id.signupTitleText);
        loginLink = findViewById(R.id.loginLink);
        fname = findViewById(R.id.signupName);
        lname = findViewById(R.id.signupSurname);
        email = findViewById(R.id.signupEmail);
        pass = findViewById(R.id.signupPass);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        number = findViewById(R.id.signupNumber);
        field = (Switch) findViewById(R.id.signupFieldSwitch);
        gen_grp = findViewById(R.id.genderRadioGroup);
        city = findViewById(R.id.citySpinner);
        status = findViewById(R.id.CheckBoxT_C);
        signup = findViewById(R.id.signupbtn);
        update = findViewById(R.id.updatebtn);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                numWithCode = ccp.getSelectedCountryCodeWithPlus();
            }
        });

        int update_per = getIntent().getIntExtra("update",0);
        if(update_per == 1){
            setTitle("Update Data");
            reg_title.setText("Update your details");
            String username_val = getIntent().getStringExtra("username");
            String password_val = getIntent().getStringExtra("pwd");
            String firstname_val = getIntent().getStringExtra("firstname");
            String lastname_val = getIntent().getStringExtra("lastname");
            String number_val = getIntent().getStringExtra("number");
            String field_val = getIntent().getStringExtra("field").trim();
            String gender_val = getIntent().getStringExtra("gender").trim();
            String city_val = getIntent().getStringExtra("city");
            fname.setText(firstname_val);
            lname.setText(lastname_val);
            email.setText(username_val);
            pass.setText(password_val);
            int numCode = Integer.parseInt(number_val.substring(1,3));
            ccp.setCountryForPhoneCode(numCode);
            number.setText(number_val.replace("+91 ",""));
            if(field_val.equals("Branch CE/IT")){
                field.setChecked(true);
            }else if(field_val == "Other") {
                field.setChecked(false);
            }
            if(gender_val.equals("Male")){
                RadioButton Mgen = findViewById(R.id.MaleRadioButton);
                Mgen.setChecked(true);
            }else{
                RadioButton Fgen = findViewById(R.id.FemaleRadioButton);
                Fgen.setChecked(true);
            }
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.city,android.R.layout.simple_spinner_dropdown_item);
            city.setAdapter(adapter);
            if(city_val != null){
                int spinnerPos = adapter.getPosition(city_val);
                city.setSelection(spinnerPos);
            }
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String firstN = fname.getText().toString();
                    String lastN = lname.getText().toString();
                    String mailId = email.getText().toString();
                    String password = pass.getText().toString();
                    String phone = numWithCode + " " + number.getText().toString();
                    boolean branch = field.isChecked();
                    int id = gen_grp.getCheckedRadioButtonId();
                    gender = findViewById(id);
                    String loc = city.getSelectedItem().toString(); //loc=location
                    myDb = new MyDatabaseHelper(SignupActivity.this);
                    Boolean res=myDb.update(firstN,lastN,mailId,password,branch,gender.getText().toString(),loc,phone);
                    if(res){
                        startActivity(new Intent(getApplicationContext(), WelcomeUsersActivity.class));
                        finish();
                        Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            signup.setVisibility(View.GONE);
            loginLink.setText("Cancel");
            update.setVisibility(View.VISIBLE);
        }
        else {
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String firstN = fname.getText().toString();
                    String lastN = lname.getText().toString();
                    String mailId = email.getText().toString();
                    String password = pass.getText().toString();
                    String phone = numWithCode + " " + number.getText().toString();
                    boolean branch = field.isChecked();
                    int id = gen_grp.getCheckedRadioButtonId();
                    gender = findViewById(id);
                    String loc = city.getSelectedItem().toString(); //loc=location
                    Boolean active = status.isChecked();
                    if(!val.empty(firstN) && !val.empty(lastN) && !val.empty(mailId) && !val.empty(password) && Patterns.EMAIL_ADDRESS.matcher(mailId).matches() && password.length()>=8 && phone.length()==14){
                        //*****************"Tutorial 07"***********************
                        myDb = new MyDatabaseHelper(SignupActivity.this);
                        Boolean res=myDb.reg_insert(firstN,lastN,mailId,password,branch,gender.getText().toString(),loc,phone);
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        if(res){
                            startActivity(i);
                            finish();
                            Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        //*****************"Tutorial 07"***********************
                    }
                    else{
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
                        if (val.empty(phone)){
                            number.setError("Enter your contact number");
                        }else {
                            if (phone.length()>14 || phone.length()<14){
                                number.setError("Number must be upto 10 digits only");
                            }
                        }
                    }
                }
            });
        }
    }

    public void loginClick(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}