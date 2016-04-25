package com.prance.app.util.http;

import java.io.Serializable;

/**
 * Created by shenbingbing on 16/4/22.
 */
public class CallBackResult implements Serializable {

    public int errno;
    public int error;
    public String message;
}
