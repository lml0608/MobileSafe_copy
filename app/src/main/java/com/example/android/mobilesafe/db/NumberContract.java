package com.example.android.mobilesafe.db;

import android.provider.BaseColumns;

/**
 * Created by liubin on 2017/4/7.
 */

public class NumberContract {

    public static final class NumberEntry implements BaseColumns {


        public static final String TABLE_NAME = "blacknumber";


        public static final String COLUMN_BLACK_ID = "_id";

        public static final String COLUMN_BLACK_PHONE = "phone";
        public static final String COLUMN_BLACK_MODE = "mode";




    }
}
