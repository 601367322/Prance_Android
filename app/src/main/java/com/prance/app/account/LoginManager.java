package com.prance.app.account;

import android.app.Activity;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 登录工具类
 * Created by shenbingbing on 16/4/20.
 */
public class LoginManager {

    public static void login(Activity context, SHARE_MEDIA loginType, MyUmAuthListener callback) {
        UMShareAPI mShareAPI = UMShareAPI.get(context.getApplicationContext());
        SHARE_MEDIA platform = loginType;
        if (callback == null) {
            callback = new MyUmAuthListener(context);
        }
        mShareAPI.doOauthVerify(context, platform, callback);
    }


}
