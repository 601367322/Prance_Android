package com.prance.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MyDatabaseLoader {

    private static final String DATABASE_NAME = "Prance-db";
    private static DaoSession daoSession;

    /**
     * 在Application中调用,初始化数据库
     * @param context
     */
    public static void init(Context context) {
        MyOpenHelper helper = new MyOpenHelper(context, DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
