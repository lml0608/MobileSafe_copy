package com.example.android.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.mobilesafe.db.NumberContract.NumberEntry;

/**
 * Created by liubin on 2017/4/7.
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "blacknumber.db";

    private static final int DATABASE_VERSION = 1;

    public BlackNumberOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BLACKNUMBER_TABLE =

                "CREATE TABLE " + NumberEntry.TABLE_NAME + " (" +


                        NumberEntry.COLUMN_BLACK_ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        NumberEntry.COLUMN_BLACK_PHONE       + " VARCHAR(20), "                 +

                        NumberEntry.COLUMN_BLACK_MODE + "  VARCHAR(5)" + ");";

        Log.i("BlackNumberOpenHelper", SQL_CREATE_BLACKNUMBER_TABLE);

        db.execSQL(SQL_CREATE_BLACKNUMBER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
