package com.zhy.http.okhttp.builder;

import java.util.Map;

/**
 * Created by shenbingbing on 16/4/22.
 */
public interface HasParamsable
{
    public abstract OkHttpRequestBuilder params(Map<String, String> params);

    public abstract OkHttpRequestBuilder addParams(String key, String val);

}
