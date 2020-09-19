package com.rku.tutorial05;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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

import org.w3c.dom.Text;
import org.w3c.dom.ls.LSException;

public class WelcomeActivity extends AppCompatActivity {
    //*****************"Tutorial 06"***********************
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //*****************"Tutorial 06"***********************

    //*******************"Tutorial 08"*******************
    ListView lstData;
    MyDatabaseHelper myDB;
    ArrayAdapter<String> adapter;
//    String data[]={"XYZ","ABC"};
    //*******************"Tutorial 08"*******************

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //*****************"Tutorial 06"***********************
        preferences = getSharedPreferences("session", MODE_PRIVATE);
        editor = preferences.edit();
        //*******************"Tutorial 06"*******************

        //*******************"Tutorial 08"*******************
        lstData = findViewById(R.id.lstDataView);
        myDB = new MyDatabaseHelper(this);

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
//                data,
                myDB.getUserList()
        ){@Override
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
        lstData.setAdapter(adapter);
        lstData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String username = ((TextView)view).getText().toString();
                Intent intent = new Intent(WelcomeActivity.this,RegDisplayActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);

            }
        });
        //*******************"Tutorial 08"*******************
    }

    //*****************"Tutorial 06"***********************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.abt_menu:
                Toast.makeText(WelcomeActivity.this, "You clicked about us..", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_menu:
                editor.remove("email");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //*****************"Tutorial 06"***********************
}