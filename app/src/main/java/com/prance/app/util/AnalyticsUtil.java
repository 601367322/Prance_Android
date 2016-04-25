package com.prance.app.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * 第三方统计类
 * Created by shenbingbing on 16/4/21.
 */
public class AnalyticsUtil {

    public static void onResume(Context context){
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context){
        MobclickAgent.onPause(context);
    }
}
