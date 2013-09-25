package com.navigps.providers;

import android.app.Activity;
import android.view.WindowManager;

public class ScreenProvider {
    public ScreenProvider() {
    }
    public static void setScreenOn(Activity activity, boolean screenOn)
    {
        if(screenOn)
        {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else {
            activity.getWindow().
                    clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
