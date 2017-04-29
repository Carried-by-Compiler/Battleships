package com.example.battleships;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hmaug on 03/04/2017.
 */

public class DatabaseHelper  extends SQLiteOpenHelper{

    private static  final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = " people_table";
    private static  final String COL1 = "Score";
    private static  final String COL2 = "name";

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME + "(Score INTEGER, " + " name TEXT) " ;
        db.execSQL(createTable);
       /* db.execSQL("INSERT INTO " + TABLE_NAME + "(Score) VALUE(1000)"); */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public  boolean addData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        Log.d(TAG, "addData : Adding. " + item + " to " + TABLE_NAME);

        Long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM" + TABLE_NAME ;
        Cursor data = db.rawQuery(query, null);
        return data ;
    }

}
