package com.prance.app.application;

import android.app.Application;
import android.content.Context;

import com.prance.app.BuildConfig;
import com.prance.app.shared.CommonShared;
import com.prance.app.util.Constants;
import com.prance.greendao.MyDatabaseLoader;
import com.umeng.socialize.PlatformConfig;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

/**
 * 万能的Application
 * Created by shenbingbing on 16/4/18.
 */
public class PranceApplication extends Application {

    public static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;

        //初始化数据库
        MyDatabaseLoader.init(this);

        //第三方初始化配置
        //微信 appid appsecret
        PlatformConfig.setWeixin(Constants.WECHAT_APPID, Constants.WECHAT_SECRET);
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone(Constants.QQ_APPID, Constants.QQ_APPKEY);

        //初始化Http
        //使用debug模式方便调试
        if(BuildConfig.DEBUG) {
            OkHttpUtils.getInstance().debug(Constants.OKHTTP_DEBUG_TAG).setConnectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        }

        //使用https，但是默认信任全部证书
        OkHttpUtils.getInstance().setCertificates();

        //初始化CommonShared
        CommonShared.getInstance();
    }
}
