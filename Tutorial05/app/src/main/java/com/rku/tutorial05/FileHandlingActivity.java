package com.rku.tutorial05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHandlingActivity extends AppCompatActivity {

    //*****************"Tutorial 09"**********************
    final String FILE_ASSETS = "data.json";
    final String FILE_INTERNAL = "testFile.txt";
    EditText editTextDataFile;
    TextView filesView;
    //*****************"Tutorial 09"**********************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_handling);

        //*****************"Tutorial 09"**********************
        editTextDataFile = findViewById(R.id.editTextDataFile);
        filesView = findViewById(R.id.filesView);
        //*****************"Tutorial 09"**********************

    }

    //*****************"Tutorial 09"**********************
    public void readAssets(View view) {
        try {
            InputStream inputStream = getAssets().open(FILE_ASSETS);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder strb = new StringBuilder();
            String temp="";
            while ((temp = bufferedReader.readLine()) !=null){
                strb.append(temp);
            }
            filesView.setText("Data from "+FILE_ASSETS+" :\n" + strb.toString());
            inputStream.close();
            reader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFiles(View view) {
        try {
            FileOutputStream fOut = openFileOutput(FILE_INTERNAL, Context.MODE_PRIVATE);
            String InputData = editTextDataFile.getText().toString();
            fOut.write(InputData.getBytes());
            fOut.close();
            editTextDataFile.setText("");
            Toast.makeText(this, "Data added to "+FILE_INTERNAL, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readFiles(View view) {
        try {
            FileInputStream fin = openFileInput(FILE_INTERNAL);
            int c;
            String temp = "";
            while((c = fin.read())!=-1){
                temp = temp+String.valueOf((char)c);
            }
            filesView.setText("Data from "+FILE_INTERNAL+" :\n"+temp);
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //*****************"Tutorial 09"**********************

}