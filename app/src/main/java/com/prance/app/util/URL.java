package com.prance.app.util;

public class URL {

    //域名加端口
    public static final String URL = "http://" + UrlUtil.getPropertiesValue("ip") + ":" + UrlUtil.getPropertiesValue("port");
    //域名(暂无用)
    public static final String IP = UrlUtil.getPropertiesValue("ip");
}
