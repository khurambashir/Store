package com.example.android.store.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.store.data.StoreContract.StoreEntry;

public class StoreDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "store.db";

    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //the SQLight quarry that will create the table of Perfumes
        String SQL_CREATE_PERFUME_TABLE = " CREATE TABLE " +StoreEntry.TABLE_NAME_PERFUME + "( "
                + StoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +StoreEntry.COLUMN_PERFUME_NAME + " TEXT NOT NULL ,"
                +StoreEntry.COLUMN_PERFUME_PRICE+ " INTEGER NOT NULL, "
                +StoreEntry.COLUMN_PERFUME_QUANTITY+ " INTEGER NOT NULL  DEFAULT 0, "
                +StoreEntry.COLUMN_PERFUME_SUPPLIER_NAME + " TEXT NOT NULL, "
                +StoreEntry.COLUMN_PERFUME_SUPPLIER_CONTACT  + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PERFUME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
