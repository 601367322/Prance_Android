package com.prance.app.ui.login;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.prance.app.R;
import com.prance.app.account.LoginManager;
import com.prance.app.account.MyUmAuthListener;
import com.prance.app.base.BaseActivity;
import com.prance.app.ui.reg.RegActivity;
import com.prance.app.util.intent.MyIntentBuilder;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 登录界面
 * Created by shenbingbing on 16/4/19.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.username)
    EditText mUserNameText;
    @Bind(R.id.password)
    EditText mPassWordText;
    @Bind(R.id.login)
    Button mLoginBtn;
    @Bind(R.id.qq_login)
    Button mQQLoginBtn;
    @Bind(R.id.wechat_login)
    Button mWeChatLoginBtn;

    @Override
    protected int layoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {

    }

    /**
     * 登录按钮点击事件
     *
     * @param view
     */
    @OnClick({R.id.login, R.id.qq_login, R.id.wechat_login})
    public void onLoginBtnClick(View view) {
        SHARE_MEDIA type = SHARE_MEDIA.GENERIC;
        switch (view.getId()) {
            case R.id.login:
                break;
            case R.id.qq_login:
                type = SHARE_MEDIA.QQ;
                break;
            case R.id.wechat_login:
                type = SHARE_MEDIA.WEIXIN;
                break;
        }
        LoginManager.login(this, type, new MyUmAuthListener(this) {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                super.onComplete(platform, action, data);
            }
        });
    }

    /**
     * 标题右侧注册按钮
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_reg://注册按钮点击事件
                new MyIntentBuilder(mContext, RegActivity.class).start();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * QQ回调
     * 必须添加
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getApplicationContext()).onActivityResult(requestCode, resultCode, data);
    }
}
