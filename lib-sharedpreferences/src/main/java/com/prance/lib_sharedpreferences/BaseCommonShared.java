package com.prance.lib_sharedpreferences;

import android.content.Context;

/**
 * 用数据库代替SharedPreferences
 * 防止多线程\多进程操作数据丢失问题
 * 单例模式
 */
public class BaseCommonShared {

    protected SharedDataUtil sp;
    protected SharedDataUtil.SharedDataEditor editor;

    private static BaseCommonShared commonShared;

    public static synchronized BaseCommonShared getInstance(Context context) {
        if (context == null) {
            return null;
        }
        if (commonShared == null) {
            commonShared = new BaseCommonShared(context);
        }
        return commonShared;
    }

    public BaseCommonShared(Context context) {
        sp = SharedDataUtil.getInstance(context);
        editor = sp.getSharedDataEditor();
    }

}
