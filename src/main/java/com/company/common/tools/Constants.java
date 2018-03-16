package com.company.common.tools;

public class Constants {
    //集合性很强的用枚举类型。但是只有1，2类型还是写成常量

    //Grobal
    public static final String FAIL = "fail";
    public static final String SUCCESS = "success";

    //handler param
    public static final String COOKILE_TOKEN = "token";
    public static final String APP_NAME = "appName";
    public static final String APP_VERSION = "appVersion";

    //mq
    public static final String ORDER_CACHE_QUEUE_NAME = "order.cache";
    public static final String ORDER_DEAD_LETTER_QUEUE_NAME = "order.dlx";
}