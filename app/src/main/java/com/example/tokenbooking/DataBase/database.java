package com.example.tokenbooking.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class database extends SQLiteOpenHelper {
    static String Database_name = "Token_DB.db";
    static String Table_Name = "Token_No";

    public database(@Nullable Context context) {
        super(context, Database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Token_No (ID INTEGER PRIMARY KEY AUTOINCREMENT,token_no INTEGER,Date TEXT,email_id TEXT,first_name TEXT,phone_number TEXT,alloted_time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }
    public boolean insertdata(int token_no,String date,String email,String name,String phone_no,String time){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("token_no",token_no);
        contentValues.put("Date",date);
        contentValues.put("email_id",email);
        contentValues.put("first_name",name);
        contentValues.put("phone_number",phone_no);
        contentValues.put("alloted_time",time);
        long result=db.insert(Table_Name,null,contentValues);
        db.close();
        return result != -1;

    }
    public Cursor getAlldata(){
        SQLiteDatabase db= this.getWritableDatabase();
        return db.rawQuery("select * from "+Table_Name,null);
    }
}
