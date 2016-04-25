package com.prance.app.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by shenbingbing on 16/4/19.
 */
public class FileUtil {

    //自定义文件夹名称
    public static final String DIR = "prance";

    /**
     * 获取Sd卡的根♂路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取程序默认缓存地址
     * 可能会被系统清除
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 获取程序默认文件地址
     * 可能会被系统清除
     *
     * @param context
     * @return
     */
    public static String getDiskFileDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(null).getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }

    /**
     * 获取自定义的目录文件夹
     * 不会被系统清除
     *
     * @return
     */
    public static String getCustomDir() {
        return getSDCardPath() + File.separator + DIR;
    }

}
