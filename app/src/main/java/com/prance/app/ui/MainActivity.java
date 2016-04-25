package com.prance.app.ui;

import android.view.View;
import android.widget.Button;

import com.prance.app.R;
import com.prance.app.base.BaseActivity;
import com.prance.app.ui.login.LoginActivity;
import com.prance.app.ui.setting.TestSetting;
import com.prance.app.util.Constants;
import com.prance.app.util.Utils;
import com.prance.app.util.intent.MyIntentBuilder;
import com.prance.app.util.secret.Main;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 程序主窗体
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.test)
    Button test;
    @Bind(R.id.login)
    Button login;

    @Override
    protected void init() {
        //禁止滑动返回
        setSwipeBackEnable(false);

        try {
            String msg = Utils.getMetaValue(mContext, Constants.TEST_SETTING);
            if (msg.equals(Constants.OPEN)) {
                test.setVisibility(View.VISIBLE);
            } else {
                test.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("userid", "152255855");
        params.put("phone", "18965621420");

        try {
            Main.client(params);

            Main.server();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.login)
    public void onClick() {
        new MyIntentBuilder(mContext, LoginActivity.class).start();
    }

    @OnClick(R.id.test)
    public void onTestClick() {
        new MyIntentBuilder(mContext, TestSetting.class).start();
    }

    /**
     * 隐藏返回按钮
     *
     * @return
     */
    @Override
    protected boolean needBackBtn() {
        return false;
    }

    @OnClick(R.id.request)
    public void onRequestClick() {
        try {
            OkHttpUtils.post().url("https://www.baidu.com").build().execute(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
