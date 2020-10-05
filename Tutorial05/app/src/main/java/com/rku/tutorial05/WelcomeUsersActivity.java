package com.rku.tutorial05;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import classes.CustomAdapter;
import classes.MyDatabaseHelper;
import classes.MyUtil;

public class WelcomeUsersActivity extends AppCompatActivity {
    //*****************"Tutorial 06"***********************
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //*****************"Tutorial 06"***********************

    //*******************"Tutorial 08"*******************
    ListView databaseUserList;
    MyDatabaseHelper myDB;
    ArrayAdapter<String> adapter;
//    String data[]={"XYZ","ABC"};
    //*******************"Tutorial 08"*******************

    //*******************"Tutorial 10"*******************
    CustomAdapter onlineDataAdapter;
    ListView onlineUserList;
    //*******************"Tutorial 10"*******************
    String mState;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_users);

        databaseUserList = findViewById(R.id.lstDataView);
        onlineUserList = findViewById(R.id.onlineUsersLstView);

        //*****************"Tutorial 06"***********************
        preferences = getSharedPreferences("session", MODE_PRIVATE);
        editor = preferences.edit();
        //*******************"Tutorial 06"*******************


        //*******************"Tutorial 10 (onlineUsers click event)"*******************
        int temp = getIntent().getIntExtra("temp",0);

        if(temp == 3){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //*****************"Extra session management (For setting menu for multiActivity)"**********************
//            editor.putString("onlinedata","off");
//            editor.commit();
            //*****************"Extra session management"**********************
            mState = "HIDE_MENU"; // setting state
            setTitle("Online Users");
            databaseUserList.setVisibility(View.GONE);
            onlineUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(WelcomeUsersActivity.this, DataDisplayActivity.class);
                    intent.putExtra("userPosition",i);
                    intent.putExtra("temp",4);
                    startActivity(intent);
                }
            });
            new MyAsyncTask().execute();
        }
        //*******************"Tutorial 10"*******************
        else {
            //*****************"Extra session management (For setting menu for multiActivity)"**********************
//            editor.putString("onlinedata","on");
//            editor.commit();
            //*****************"Extra session management"**********************
            //*******************"Tutorial 08"*******************
            onlineUserList.setVisibility(View.GONE);
            myDB = new MyDatabaseHelper(this);

            adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
//                data,
                    myDB.getUserList()
            ) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Get the Item from ListView
                    View view = super.getView(position, convertView, parent);

                    // Initialize a TextView for ListView each Item
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);

                    // Set the text color of TextView (ListView Item)
                    tv.setTextColor(Color.RED);

                    // Generate ListView Item using TextView
                    return view;
                }
            };
            databaseUserList.setAdapter(adapter);
            databaseUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String username = ((TextView) view).getText().toString();
                    Intent intent = new Intent(WelcomeUsersActivity.this, DataDisplayActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("temp", 2);
                    startActivity(intent);
                }
            });
            //*******************"Tutorial 08"*******************
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(getApplicationContext(),WelcomeUsersActivity.class);
        backIntent.putExtra("temp",1);
        startActivity(backIntent);
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //*****************"Tutorial 06"***********************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        if(mState == "HIDE_MENU"){
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.abt_menu:
                Toast.makeText(WelcomeUsersActivity.this, "You clicked about us..", Toast.LENGTH_SHORT).show();
                break;
            //*****************"Tutorial 09"***********************
            case R.id.manageFiles:
                startActivity(new Intent(getApplicationContext(),FileHandlingActivity.class));
                break;
            //*****************"Tutorial 09"***********************
            //*****************"Tutorial 10"***********************
            case R.id.asyncTask:
                Intent intent = new Intent(getApplicationContext(), WelcomeUsersActivity.class);
                intent.putExtra("temp",3);
                startActivity(intent);
                break;
            //*****************"Tutorial 10"***********************
            case R.id.logout_menu:
                editor.remove("email");
                editor.commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //*****************"Tutorial 06"***********************

    //*******************"Tutorial 10 (AsyncTask to read JSON file Data)"*******************
    class MyAsyncTask extends AsyncTask {
        ProgressDialog dialog;
        StringBuilder strb;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(WelcomeUsersActivity.this);
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL url = new URL(MyUtil.URL_USERS);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(reader);

                strb = new StringBuilder();
                String onlineData = "";
                while((onlineData = br.readLine())!=null){
                    strb.append(onlineData);
                }
                Log.i("jsonString",strb.toString());
                MyUtil.jsonArray = new JSONArray(strb.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            onlineDataAdapter = new CustomAdapter(WelcomeUsersActivity.this,MyUtil.jsonArray);
            onlineUserList.setAdapter(onlineDataAdapter);
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
    //*******************"Tutorial 10"*******************
}