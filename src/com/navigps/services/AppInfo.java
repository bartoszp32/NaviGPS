package com.navigps.services;


public class AppInfo {
    private static  AppInfo instance;



    private static final String LOG_TAG = "NAVI_GPS";
    public static AppInfo getInstance()
    {
        if(instance==null)
        {
            instance = new AppInfo();
        }
        return instance;
    }
    public AppInfo() {

    }
    public static String getLogTag() {
        return LOG_TAG;
    }
}
