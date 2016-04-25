package com.prance.app.shared;

import android.content.Context;

import com.prance.app.application.PranceApplication;
import com.prance.lib_sharedpreferences.BaseCommonShared;

/**
 * 用数据库代替SharedPreferences
 * 防止多线程\多进程操作数据丢失问题
 * 单例模式
 */
public class CommonShared extends BaseCommonShared{

    private static CommonShared mInstance;

    public static CommonShared getInstance()
    {
        if (mInstance == null)
        {
            synchronized (CommonShared.class)
            {
                if (mInstance == null)
                {
                    mInstance = new CommonShared(PranceApplication.getContext());
                }
            }
        }
        return mInstance;
    }

    public CommonShared(Context context) {
        super(context);
    }

    private final String LOCATION = "location";// 定位的城市

    public void setLocation(String str) {
        editor.putString(LOCATION, str);
        editor.commit();
    }

    public String getLocation() {
        return sp.getString(LOCATION, "北京");
    }

}
