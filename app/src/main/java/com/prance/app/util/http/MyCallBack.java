package com.prance.app.util.http;

import com.prance.app.util.GsonUtil;
import com.zhy.http.okhttp.callback.Callback;

import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by shenbingbing on 16/4/22.
 */
public class MyCallBack extends Callback<Serializable> {

    @Override
    public void onResponse(Serializable response) {

    }

    @Override
    public Serializable parseNetworkResponse(Response response) throws Exception {
        CallBackResult result = GsonUtil.jsonToBean(response.body().string(), CallBackResult.class);
        try {
            switch (result.errno) {
                case Errno.SUCCESS:
                    return GsonUtil.jsonToList(result.message, Serializable.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onError(Call call, Exception e) {

    }

}
