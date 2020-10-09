package com.rku.tutorial05;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    AlertDialog.Builder builder;
    //*******************"Tutorial 11 (class of volley library)"*******************
    RequestQueue requestQueue;
    StringRequest stringRequest;
    JsonArrayRequest jsonArrayRequest;
    ProgressDialog dialog;
    //*****************"Tutorial 11"***********************

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_users);

        databaseUserList = findViewById(R.id.lstDataView);
        onlineUserList = findViewById(R.id.onlineUsersLstView);

        //*******************"Tutorial 11 (Instantiate dialog object of ProgressDialog)"*******************
        dialog = new ProgressDialog(WelcomeUsersActivity.this,R.style.DialogTheme);
        //*****************"Tutorial 11"***********************

        //*****************"Tutorial 06"***********************
        preferences = getSharedPreferences("session", MODE_PRIVATE);
        editor = preferences.edit();
        //*******************"Tutorial 06"*******************


        //*******************"Tutorial 10 (onlineUsers click event)"*******************
        int temp = preferences.getInt("temp",0);

        if(temp == 3){
            editor.putString("closeApp", "no");
            editor.commit();
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mState = "HIDE_MENU"; // setting state
            setTitle("Online Users");
            databaseUserList.setVisibility(View.GONE);
            onlineUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(WelcomeUsersActivity.this, DataDisplayActivity.class);
                    intent.putExtra("userPosition",i);
                    editor.putInt("temp",4);
                    editor.commit();
                    startActivity(intent);
                    finish();

                }
            });
            if(MyUtil.isOnline(this)){
                //new MyAsyncTask().execute();
                //*****************"Tutorial 11(Online Users data view by StringRequest & JSONArray Request)"***********************
                volleyNetworkCall();
                //*****************"Tutorial 11"***********************
            }else {
                builder = new AlertDialog.Builder(this,R.style.DialogTheme);
                builder.setTitle("No Internet Connection")
                        .setMessage("You need to have Mobile Data or wifi to access this. Press Cancel to Exit")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putInt("temp",3);
                                editor.commit();
                                startActivity(new Intent(WelcomeUsersActivity.this, WelcomeUsersActivity.class));
                                finish();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog errorDialog = builder.create();
                errorDialog.show();
            }
        }
        //*******************"Tutorial 10"*******************
        else {
            editor.putString("closeApp", "yes");
            editor.commit();
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
                    editor.putInt("temp",2);
                    startActivity(intent);
                    finish();
                }
            });
            //*******************"Tutorial 08"*******************
        }
    }

    //*****************"Tutorial 11(External method for main logic)"***********************
    private void volleyNetworkCall() {
        //by using StringRequest
        stringRequest = new StringRequest(
                Request.Method.GET,
                MyUtil.URL_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            MyUtil.jsonArray = new JSONArray(response);
                            onlineDataAdapter = new CustomAdapter(WelcomeUsersActivity.this,MyUtil.jsonArray);
                            onlineUserList.setAdapter(onlineDataAdapter);
                            if(dialog.isShowing())dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(dialog.isShowing())dialog.dismiss();
                    }
                }
        );
        requestQueue = Volley.newRequestQueue(WelcomeUsersActivity.this);
        dialog.show();
        requestQueue.add(stringRequest);

        //by using JSONArray Request
//        jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                MyUtil.URL_USERS,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        MyUtil.jsonArray = response;
//                        onlineDataAdapter = new CustomAdapter(WelcomeUsersActivity.this, MyUtil.jsonArray);
//                        onlineUserList.setAdapter(onlineDataAdapter);
//                        if (dialog.isShowing()) dialog.dismiss();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (dialog.isShowing()) dialog.dismiss();
//                    }
//                }
//        );
//        requestQueue = Volley.newRequestQueue(WelcomeUsersActivity.this);
//        dialog.show();
//        requestQueue.add(jsonArrayRequest);
    }
    //*****************"Tutorial 11"**********************

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String closeApp = preferences.getString("closeApp","");
        if(closeApp == "no"){
            editor.putInt("temp",1);
            editor.commit();
            startActivity(new Intent(getApplicationContext(),WelcomeUsersActivity.class));
            finish();
        }
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
                editor.putInt("temp",3);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), WelcomeUsersActivity.class));
                finish();
                break;
            //*****************"Tutorial 10"***********************
            case R.id.logout_menu:
                editor.remove("email");
                editor.remove("temp");
                editor.remove("closeApp");
                editor.commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //*****************"Tutorial 06"***********************

    //*******************"Tutorial 10 (BackEnd Thread AsyncTask to read JSON file Data)"*******************
    class MyAsyncTask extends AsyncTask {
        StringBuilder strb;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(WelcomeUsersActivity.this,R.style.DialogTheme);
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