package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Project.db";

    public DBHelper(Context context) {
        super(context, "Project.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, email TEXT, fullname TEXT, customer_id TEXT, available_amount REAL)");
        MyDB.execSQL("create Table buses(id TEXT primary key, departure TEXT, arrival TEXT, date TEXT, total_seats TEXT)");
        MyDB.execSQL("create Table admin(username TEXT primary key, password TEXT, email TEXT, fullname TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists buses");
        MyDB.execSQL("drop table if exists admin");
    }

    public Boolean insertData(String fullname, String email, String username, String password)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullname", fullname);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("customer_id", "");
        contentValues.put("available_amount", 0);  // Initial amount
        long result = MyDB.insert("users",null,contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean insertadmin(String fullname, String email, String username, String password)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullname", fullname);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("admin",null,contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public boolean checkusername(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public boolean checkadminusername(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from admin where username = ?", new String[] {username});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public boolean checkadminusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from admin where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean insertBus(String id, String departure, String arrival, String date, String total_seats)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("departure", departure);
        contentValues.put("arrival", arrival);
        contentValues.put("date", date);
        contentValues.put("total_seats", total_seats);
        long result = MyDB.insert("buses",null,contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean updateBus(String id, String departure, String arrival, String date, String total_seats)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("departure", departure);
        contentValues.put("arrival", arrival);
        contentValues.put("date", date);
        contentValues.put("total_seats", total_seats);
        Cursor cursor = MyDB.rawQuery("Select * from buses where id = ?", new String[] {id});
        if (cursor.getCount()>0){
        long result = MyDB.update("buses", contentValues,"id=?", new String[] {id});
        if(result==-1) return false;
        else
            return true;
        }
        else
            {
                return false;
            }
    }

    public Boolean deleteBus(String id)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from buses where id = ?", new String[] {id});
        if (cursor.getCount()>0){
            long result = MyDB.delete("buses","id=?", new String[] {id});
            if(result==-1) return false;
            else
                return true;
        }
        else
            {
                return false;
            }
    }

    public Cursor viewbuses()
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from buses", null);
        return cursor;
    }

    /*public Cursor getListContents(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor data = MyDB.rawQuery("Select * from buses", null);
        return data;*/


    public boolean checkid(String id){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from buses where id = ?", new String[] {id});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Cursor getUserData(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        String[] projection = {"customer_id", "fullname", "email", "available_amount"};
        String selection = "username=?";
        String[] selectionArgs = {username};
        return MyDB.query("users", projection, selection, selectionArgs, null, null, null);
    }

    public String generateCustomerId() {
        return "CUST" + System.currentTimeMillis();
    }
    public void updateCustomerId(String username, String customerId) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customerId);
        MyDB.update("users", contentValues, "username=?", new String[] {username});
    }
    public Cursor getUserDataByCustomerId(String customerId) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        String[] projection = {"customer_id", "fullname", "email"};
        String selection = "customer_id=?";
        String[] selectionArgs = {customerId};
        return MyDB.query("users", projection, selection, selectionArgs, null, null, null);
    }
    // Add method to update the available amount
    public void updateAvailableAmount(String username, double amount) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("available_amount", amount);
        MyDB.update("users", contentValues, "username=?", new String[]{username});
    }

    // Add method to get the available amount
    public double getAvailableAmount(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT available_amount FROM users WHERE username=?", new String[]{username});
        if (cursor.moveToFirst()) {
            return cursor.getDouble(cursor.getColumnIndex("available_amount"));
        } else {
            return 0.0; // Return 0 if no record found
        }
    }


}
