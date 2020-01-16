package com.example.phototagger.server;

public class NetDefine {
    private static final String ES_PATH = "<Your ES Address>";

    public static String getBasicPath() {
        return "https://" + ES_PATH;
    }
}