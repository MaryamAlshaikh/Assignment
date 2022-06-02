package com.example.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="cust.db";
    public static final String TABLE_NAME="customer_table";
    public static final String COL_1="ID"; //customer ID
    public static final String COL_2="NAME"; //customer name
    public static final String COL_3="DAYS"; //number of days to stay
    public static final String COL_4="STATE"; //type of room to be booked
    public static final String COL_5="TOTAL"; //total price of the booking



    //invoking constructor
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1 );
    }

    public void onCreate(SQLiteDatabase DB)
    { //creating table with its data
        DB.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DAYS INTEGER, STATE TEXT, TOTAL TEXT)");    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion)
    {
        DB.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(DB);
    }
        //inserting data into table
    public boolean insertData(String name, String days, String state, String total) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,days);
        contentValues.put(COL_4,state);
        contentValues.put(COL_5,total);

        long result = DB.insert(TABLE_NAME,
                null,
                contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }


    public boolean UpdateData(String id, String name, String days, String state, String total) {
    //updating data of a table
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,days);
        contentValues.put(COL_4,state);
        contentValues.put(COL_5,total);
        DB.update(TABLE_NAME, contentValues, "ID = ?", new String[] { id });
        return true;


    }

    public Integer deleteData(String id) { //deleting a table
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.delete(TABLE_NAME, "ID=?",new String[] { id });
    }

    public Cursor getAllData() { //getting the data from DB
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor r = DB.rawQuery("select * from " +TABLE_NAME, null);
        return r;
    }
}
