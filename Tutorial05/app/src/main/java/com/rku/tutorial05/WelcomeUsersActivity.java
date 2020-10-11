package com.rku.tutorial05;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import classes.MyDatabaseHelper;
import classes.MyUtil;
import classes.OfllineDataAdapter;
import classes.OnlineDataAdapter;

public class WelcomeUsersActivity extends AppCompatActivity {
    //*****************"Tutorial 06"***********************
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //*****************"Tutorial 06"***********************

    //*******************"Tutorial 08"*******************
    RecyclerView AllTypeUserList; //Tut 10, 12
    OfllineDataAdapter offlineData;
    MyDatabaseHelper myDB;
    //*******************"Tutorial 08"*******************

    //*******************"Tutorial 10"*******************
    OnlineDataAdapter onlineData; //Tut 12
    //*******************"Tutorial 10"*******************
    String mState;
    AlertDialog.Builder builder;
    //*******************"Tutorial 11 (class of volley library)"*******************
    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest;
    ProgressDialog dialog;
    //*****************"Tutorial 11"***********************

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_users);

        AllTypeUserList = findViewById(R.id.AllTypeUsersList);
        //use this setting to improve performance if you know that changes
        //in content do not changes the layout size of the RecyclerView
        AllTypeUserList.setHasFixedSize(true);
        //use a linear layout manager
        AllTypeUserList.setLayoutManager(new LinearLayoutManager(this));
        //Add Divider
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(AllTypeUserList.getContext(),LinearLayoutManager.VERTICAL);
        Drawable whiteDivider = ContextCompat.getDrawable(getApplicationContext(), R.drawable.line_divider);
        dividerItemDecoration.setDrawable(whiteDivider);
        AllTypeUserList.addItemDecoration(dividerItemDecoration);

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
            //*******************"Tutorial 12(RecyclerView Java class for fetching online data using Volley)"*******************
            if(MyUtil.isOnline(this)){
                //new MyAsyncTask().execute();
                //*****************"Tutorial 11(Online Users data view by StringRequest & JSONArray Request)"***********************
                volleyNetworkCall();
                //*****************"Tutorial 11"***********************

                //*******************"Tutorial 12(RecyclerView Java class for fetching online data using Volley)"*******************
            }
            else {
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
        else{
        //*******************"Tutorial 10"*******************
            editor.putString("closeApp", "yes");
            editor.commit();
            //*******************"Tutorial 08"*******************
            myDB = new MyDatabaseHelper(this);
            ArrayList<String> list = myDB.getUserList();
            offlineData = new OfllineDataAdapter(WelcomeUsersActivity.this,list);
            AllTypeUserList.setAdapter(offlineData);
            //*******************"Tutorial 08"*******************
        }
    }

    //*****************"Tutorial 11(External method for onlineData fetching logic)"***********************
    private void volleyNetworkCall() {
        //by using JSONArray Request
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MyUtil.URL_USERS,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        MyUtil.jsonArray = response;
                        //*****************"Tutorial 12(Adapter Object instantiate)"***********************
                        onlineData = new OnlineDataAdapter(WelcomeUsersActivity.this,response);
                        AllTypeUserList.setAdapter(onlineData);
                        onlineData.notifyDataSetChanged();
                        //*****************"Tutorial 12"***********************
                        if(dialog.isShowing())dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(dialog.isShowing())dialog.dismiss();
                    }
                }
        );
//        dialog.show();
        requestQueue.add(jsonArrayRequest);
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
            //*****************"Tutorial 10, 12(To add into menu list)"***********************
            case R.id.asyncTask:
                editor.putInt("temp",3);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), WelcomeUsersActivity.class));
                finish();
                break;
            //*****************"Tutorial 10, 12"***********************
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
            onlineData = new OnlineDataAdapter(getApplicationContext(),MyUtil.jsonArray);
            AllTypeUserList.setAdapter(onlineData);
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
    //*******************"Tutorial 10"*******************
}