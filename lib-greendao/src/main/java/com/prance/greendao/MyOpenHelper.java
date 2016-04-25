package com.prance.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * 自定义类继承OpenHelper
 * 可以操作更新数据库
 * Created by shenbingbing on 16/4/18.
 */
public class MyOpenHelper extends DaoMaster.OpenHelper {

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 更新数据库
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                break;
        }
    }
}
