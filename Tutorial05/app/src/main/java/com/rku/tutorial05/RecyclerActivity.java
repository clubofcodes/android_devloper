package com.rku.tutorial05;
//*******************"Tutorial 12(RecyclerView Java class for fetching online data using Volley)"*******************

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import classes.MyUtil;
import classes.RecyclerAdapter;

public class RecyclerActivity extends AppCompatActivity {

    RecyclerView rcvUsers;
    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest;
    ProgressDialog dialog;

    RecyclerAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        dialog = new ProgressDialog(RecyclerActivity.this,R.style.DialogTheme);
        rcvUsers = findViewById(R.id.rcvUsers);
        //use this setting to improve performance if you know that changes
        //in content do not changes the layout size of the RecyclerView
        rcvUsers.setHasFixedSize(true);

        //use a linear layout manager
        rcvUsers.setLayoutManager(new LinearLayoutManager(this));

        //Add Divider
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(rcvUsers.getContext(),LinearLayoutManager.VERTICAL);
        rcvUsers.addItemDecoration(dividerItemDecoration);

        //Animation
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation =
                AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        rcvUsers.setLayoutAnimation(animation);


        volleyNetworkCallAPI();
    }

    private void volleyNetworkCallAPI() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MyUtil.URL_USERS,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        MyUtil.jsonArray = response;
                        userAdapter = new RecyclerAdapter(response);
                        rcvUsers.setAdapter(userAdapter);
                        userAdapter.notifyDataSetChanged();
                        dialog = new ProgressDialog(RecyclerActivity.this,R.style.DialogTheme);
                        if(dialog.isShowing()) dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(dialog.isShowing()) dialog.dismiss();
                    }
                }
        );
        dialog.show();
        requestQueue.add(jsonArrayRequest);
    }
    //*******************"Tutorial 12"*******************
}