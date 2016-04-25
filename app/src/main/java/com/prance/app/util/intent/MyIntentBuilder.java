package com.prance.app.util.intent;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * 生成Intent工具类
 */
public class MyIntentBuilder extends ActivityIntentBuilder<MyIntentBuilder> {

    private android.app.Fragment fragment_;
    private android.support.v4.app.Fragment fragmentSupport_;

    public MyIntentBuilder(Context context, Class<?> clazz) {
        super(context, clazz);
    }

    public MyIntentBuilder(android.app.Fragment fragment, Class<?> clazz) {
        super(fragment.getActivity(), clazz);
        fragment_ = fragment;
    }

    public MyIntentBuilder(android.support.v4.app.Fragment fragment, Class<?> clazz) {
        super(fragment.getActivity(), clazz);
        fragmentSupport_ = fragment;
    }

    @Override
    public void startForResult(int requestCode) {
        if (fragmentSupport_ != null) {
            fragmentSupport_.startActivityForResult(intent, requestCode);
        } else {
            if (fragment_ != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    fragment_.startActivityForResult(intent, requestCode, lastOptions);
                } else {
                    fragment_.startActivityForResult(intent, requestCode);
                }
            } else {
                if (context instanceof Activity) {
                    Activity activity = ((Activity) context);
                    ActivityCompat.startActivityForResult(activity, intent, requestCode, lastOptions);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        context.startActivity(intent, lastOptions);
                    } else {
                        context.startActivity(intent);
                    }
                }
            }
        }
    }

}