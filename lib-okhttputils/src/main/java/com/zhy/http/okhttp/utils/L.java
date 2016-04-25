package com.zhy.http.okhttp.utils;

import android.util.Log;

/**
 * Created by shenbingbing on 16/4/22.
 */
public class L
{
    private static boolean debug = false;

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e("OkHttp", msg);
        }
    }

}

