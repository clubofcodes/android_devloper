package com.rku.tutorial05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import validations.Validation;

public class PermissionsActivity extends AppCompatActivity {

    EditText userNumContact, userSmsSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        userNumContact = findViewById(R.id.userNumContact);
        userSmsSend = findViewById(R.id.userSmsSend);
    }


    public void callUser(View view) {
        if (isCallPermissionAllowed()){
            if(userNumContact.getText().toString().length()!=10){
                userNumContact.setError("Enter your number");
                userNumContact.requestFocus();
            }else {
                call();
                Toast.makeText(this, "Calling ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + userNumContact.getText().toString().trim()));
        startActivity(intent);
    }

    private boolean isCallPermissionAllowed() {
        if(Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else{
                ActivityCompat.requestPermissions(PermissionsActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},11);
                return false;
            }
        }
        else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 11:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

                break;
            case 21:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sms();
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    public void sentTxtToUser(View view) {
        if(isSMSPermissionAllowed()){
            sms();
        }
    }

    private void sms() {
        String phonenum = userNumContact.getText().toString();
        String text_val = userSmsSend.getText().toString().trim();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phonenum,null,text_val,null,null);
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
    }

    private boolean isSMSPermissionAllowed() {
        if(Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else{
                ActivityCompat.requestPermissions(PermissionsActivity.this,new String[]{Manifest.permission.SEND_SMS},21);
                return false;
            }
        }
        else{
            return true;
        }
    }
}