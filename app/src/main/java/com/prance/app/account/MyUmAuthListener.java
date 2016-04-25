package com.prance.app.account;

import android.app.Activity;
import android.widget.Toast;

import com.prance.app.util.ToastUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Umeng 登录 回调
 */
public class MyUmAuthListener implements UMAuthListener {

    private Activity context;

    public MyUmAuthListener(Activity context) {
        this.context = context;
    }

    @Override
    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
        Toast.makeText(context, "Authorize succeed", Toast.LENGTH_SHORT).show();
        switch (action) {
            case ACTION_AUTHORIZE:
                UMShareAPI mShareAPI = UMShareAPI.get(context.getApplicationContext());
                mShareAPI.getPlatformInfo(context, platform, this);
                break;
            case ACTION_GET_PROFILE:
                String nickname = data.get("screen_name");
                String gender = data.get("gender");
                String province = data.get("province");
                String city = data.get("city");
                String openid = data.get("openid");
                break;
        }
    }

    @Override
    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        ToastUtil.getInstance().showToast("打开失败/(ㄒoㄒ)/~~");
    }

    @Override
    public void onCancel(SHARE_MEDIA platform, int action) {
        switch (action) {
            case ACTION_AUTHORIZE:
                break;
        }
    }
}