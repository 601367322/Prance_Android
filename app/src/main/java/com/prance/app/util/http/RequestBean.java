package com.prance.app.util.http;

import java.io.Serializable;

/**
 * Created by shenbingbing on 16/4/25.
 */
public class RequestBean implements Serializable {

    public String data;

    public String key;

    public RequestBean() {
    }

    public RequestBean(String data, String key) {
        this.data = data;
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
