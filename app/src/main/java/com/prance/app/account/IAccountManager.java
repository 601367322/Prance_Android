package com.prance.app.account;

import android.content.Context;

/**
 * 用户信息接口
 * 判断是否登录\获取用户信息
 * Created by shenbingbing on 16/4/19.
 */
public interface IAccountManager {

    /**
     * 是否登录
     *
     * @param context 上下文
     * @return true 已登录 false 未登录
     */
    boolean isLogin(Context context);

    /**
     * 获取用户信息
     *
     * @param context 上下文
     * @param clazz   类型
     * @param <T>     返回对象类型
     * @return 用户信息
     */
    <T> T getUserInfo(Class<T> clazz);

    void saveUserInfo(Object obj);

}
