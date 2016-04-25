package com.prance.app.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.prance.app.application.ActivityManager;
import com.prance.app.util.AnalyticsUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Activity基♂类
 * Created by shenbingbing on 16/4/18.
 */
public abstract class BaseActivity extends SwipeBackActivity {

    //上下文
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        //将此activity放入管理类,便于管理和查询
        ActivityManager.getActivityManager().pushActivity(this);

        //设置布局
        setContentView(layoutRes());

        //去掉actionBar阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setElevation(0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //返回按钮
        if (needBackBtn()) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        //启用注解
        if (autoBind()) {
            ButterKnife.bind(this);
        }

        //EventBus
        if (needEventBus()) {
            EventBus.getDefault().register(this);
        }

        init();
    }

    /**
     * 初始化操作
     * 之类不必再继承onCreate方法
     */
    protected abstract void init();

    /**
     * 界面布局
     *
     * @return
     */
    protected abstract int layoutRes();

    /**
     * 是否使用butterKnife自动注解
     *
     * @return 默认使用
     */
    protected boolean autoBind() {
        return true;
    }

    /**
     * 事件接收
     * 可用来代替部分广播业务
     */
    protected boolean needEventBus() {
        return false;
    }

    /**
     * 返回按钮显示
     *
     * @return
     */
    protected boolean needBackBtn() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://返回按钮点击事件
                if(getSwipeBackEnable()) {
                    scrollToFinishActivity();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsUtil.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyticsUtil.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将此activity移除管理类
        ActivityManager.getActivityManager().popActivity(this);
        //移除EventBus
        if (needEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

}
