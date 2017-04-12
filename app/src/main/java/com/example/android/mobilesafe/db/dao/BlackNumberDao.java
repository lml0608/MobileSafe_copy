package com.example.android.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.mobilesafe.db.BlackNumberOpenHelper;
import com.example.android.mobilesafe.db.NumberContract;
import com.example.android.mobilesafe.db.NumberContract.NumberEntry;
import com.example.android.mobilesafe.db.domain.BlackNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubin on 2017/4/8.
 */

public class BlackNumberDao {

    private static BlackNumberDao sBlackNumberDao;

    private Context mContext;
    private final SQLiteDatabase mDatabase;

    private BlackNumberDao(Context context) {

        mContext = context.getApplicationContext();

        mDatabase = new BlackNumberOpenHelper(mContext)
                .getWritableDatabase();
    }

    public static BlackNumberDao getInstance(Context context) {

        if (sBlackNumberDao == null) {

            sBlackNumberDao = new BlackNumberDao(context);
        }
        return sBlackNumberDao;
    }

    //添加记录

    public void addBlackNumber(BlackNumber b) {

        ContentValues values = getContentValues(b);

        mDatabase.insert(NumberEntry.TABLE_NAME, null, values);
    }

    //删除

    public void deleteBlackNumber(BlackNumber b){

        mDatabase.delete(NumberEntry.TABLE_NAME, "phone = ?", new String[]{b.getPhone()});

    }

    //更新

    public void updateBlackNumber(BlackNumber b){

        String phoneNumber = b.getPhone();
        ContentValues Values = getContentValues(b);

        mDatabase.update(NumberEntry.TABLE_NAME, Values, "phone = ?", new String[]{phoneNumber});

    }

    /**
     * @return	查询到数据库中所有的号码以及拦截类型所在的集合
     */
    public List<BlackNumber> getBlackNumber(){

        List<BlackNumber> blackNumberList = new ArrayList<BlackNumber>();
        Cursor cursor = mDatabase.query(NumberEntry.TABLE_NAME, new String[]{"phone","mode"}, null, null, null, null, "_id desc");

        while(cursor.moveToNext()){
            BlackNumber blackNumber = new BlackNumber();
            blackNumber.setPhone(cursor.getString(0));
            blackNumber.setMode(cursor.getString(1));
            blackNumberList.add(blackNumber);
        }
        cursor.close();

        return blackNumberList;
    }


    /**
     * 分页查询，每次20条
     * @return	查询到数据库中所有的号码以及拦截类型所在的集合
     */
    public List<BlackNumber> getNumbers(int index){

        List<BlackNumber> NumberList = new ArrayList<BlackNumber>();
        Cursor cursor = mDatabase.rawQuery("select * from " + NumberEntry.TABLE_NAME + " order by " +
                NumberEntry.COLUMN_BLACK_ID + " desc limit ?,20;", new String[]{index + ""});

        while(cursor.moveToNext()){
            BlackNumber blackNumber = new BlackNumber();
            blackNumber.setPhone(cursor.getString(1));
            blackNumber.setMode(cursor.getString(2));
            NumberList.add(blackNumber);
        }
        cursor.close();

        return NumberList;
    }

    /**
     * 根据电话号码查询拦截的模式
     * @param phone
     * @return
     */
    public int getMode(String phone) {

        int mode = 0;
        Cursor cursor = mDatabase.query(NumberEntry.TABLE_NAME, new String[]{"mode"}, NumberEntry.COLUMN_BLACK_PHONE + " =?", new String[]{phone}, null, null, null);

        while(cursor.moveToNext()) {
            mode = cursor.getInt(0);
        }

        cursor.close();

        return mode;
    }


    /**
     * 获取数据库中数据的总条数
     * @return
     */
    public int getCounts(){


        Cursor cursor = mDatabase.rawQuery("select count(*) from " + NumberEntry.TABLE_NAME + ";", null);

        int count = 0;
        while(cursor.moveToNext()){

            count = cursor.getInt(0);
        }
        cursor.close();

        return count;
    }




    private static ContentValues getContentValues(BlackNumber blackNumber) {

        ContentValues values = new ContentValues();
        values.put(NumberEntry.COLUMN_BLACK_PHONE, blackNumber.getPhone());
        values.put(NumberEntry.COLUMN_BLACK_MODE, blackNumber.getMode());

        return values;
    }
}
