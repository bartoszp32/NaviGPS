package com.example.logowanie.services;

/**
 * Created by Barti on 23.08.13.
 */
public class AppInfo {
    AppInfo instance;



    private static final String LOG_TAG = "NAVI_GPS";
    public AppInfo getInstance()
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