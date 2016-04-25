package com.prance.app.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.prance.app.application.PranceApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shenbingbing on 16/4/18.
 */
public class UrlUtil {

    /**
     * 获取Assets目录下ban.properties中的属性
     *
     * @param key
     * @return
     */
    public static String getAssetsProperties(String key) {

        Context context = PranceApplication.getContext();
        String result = "";
        try {
            Properties properties = new Properties();
            InputStream is = context.getAssets().open(getPathName());
            properties.load(is);
            if (properties != null) {
                if (properties.containsKey(key)) {
                    result = properties.get(key).toString();
                }
            }
        } catch (Exception e) {
            LogUtil.d("getAssetsProperties e[" + e + "]");
        }
        return result;
    }

    /**
     * 读取本地SD卡文件下ban.properties中的属性
     *
     * @param
     * @return
     */
    private static String getSDcardProperties(String key) {

        Context context = PranceApplication.getContext();
        String result = "";
        try {
            Properties properties = new Properties();
            InputStream is = new FileInputStream(getBaseDir(context) + getPathName());
            properties.load(is);
            if (properties != null) {
                if (properties.containsKey(key)) {
                    result = properties.get(key).toString();
                }
            }
        } catch (Exception e) {

        }
        return result;
    }


    /**
     * 初始化 ban.properties中的属性
     *
     * @param key
     * @return
     */
    public static String getPropertiesValue(String key) {

        Context context = PranceApplication.getContext();
        String result = "";

        boolean settingopen = false;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString(Constants.TEST_SETTING);
            if (msg.equals(Constants.OPEN)) {
                settingopen = true;
            } else {
                settingopen = false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (checkSaveLocationExists() && fileIsExists(getBaseDir(context) + getPathName()) && settingopen) {
            result = getSDcardProperties(key);

            if (result.equals("")) {
                result = getAssetsProperties(key);
            }
        } else {
            result = getAssetsProperties(key);
        }
        return result;
    }


    /**
     * 判断文件是否存在
     *
     * @return
     */
    public static boolean fileIsExists(String pathName) {
        try {
            File f = new File(pathName);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 初始属性的文件名
     *
     * @return
     */
    public static String getPathName() {
        return "prance.properties";
    }

    /**
     * 检查是否安装SD卡
     *
     * @return
     */
    public static boolean checkSaveLocationExists() {
        String sDCardStatus = Environment.getExternalStorageState();
        boolean status;
        if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
            status = true;
        } else
            status = false;
        return status;
    }

    /**
     * 初始化文件保存基本目录
     *
     * @param context
     * @return
     */
    public static String getBaseDir(Context context) {
        return FileUtil.getCustomDir();
    }


}
