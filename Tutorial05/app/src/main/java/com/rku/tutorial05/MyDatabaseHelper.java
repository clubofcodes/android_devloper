//*****************"Tutorial 07"***********************
package com.rku.tutorial05;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "student.db";
    //common column names....
    private static final String COLUMN_ID = "id";
    private static  final String user_id = "email";
    private static  final String pass = "password";
    //columns for registration...
    private static  final String Signup_Table_Name = "registration";
    private static  final String fname = "first_name";
    private static  final String lname = "last_name";
    private static  final String branch = "branch";
    String ce_it = "Other";
    private static  final String gen = "gender";
    private static  final String location = "city";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " +
                Signup_Table_Name + " ("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                fname + " TEXT, " +
                lname + " TEXT, " +
                user_id + " TEXT, " +
                pass + " TEXT, " +
                branch + " TEXT, " +
                gen + " TEXT, " +
                location + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Signup_Table_Name);
        onCreate(db);
    }

    public boolean reg_insert(String firstname, String lastname, String email, String password,Boolean field, String gender, String city){
        SQLiteDatabase db = this.getWritableDatabase();
        if(field){
            ce_it = "Branch CE/IT";
        }
        ContentValues values = new ContentValues();
        values.put(fname,firstname);
        values.put(lname,lastname);
        values.put(user_id,email);
        values.put(pass,password);
        values.put(branch,ce_it);
        values.put(gen,gender);
        values.put(location,city);

        long res = db.insert(Signup_Table_Name,null,values);
        return (res==-1)?false:true;
    }

    public Cursor checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query =  "select * from registration where email='"+ username +"' and password='"+ password+"'";;
        Cursor cursor = null;
        if(db!=null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    //*****************"Tutorial 07"***********************

    //*****************"Tutorial 08"***********************
    public ArrayList<String> getUserList(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.query(
                Signup_Table_Name,
                new String[]{user_id},
                null,
                null,
                null,
                null,
                null
        );
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public Cursor getPartUserData(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                Signup_Table_Name,
                null,
                "email=?",
                new String[]{username},
                null,
                null,
                null
        );
        return cursor;
    }
    //*****************"Tutorial 08"***********************
}
