package com.example.logowanie.managers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Barti on 25.08.13.
 */
public class ServicesManager {
    Context context;
    public ServicesManager(Context context)
    {
        this.context = context;
    }
    public boolean isServiceRunning(String className)
    {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {

            if (className.equals(service.service
                    .getClassName())) {

                return true;
            }
        }

        return false;
    }
    public boolean startServiceIfNotRunning(Class cClass)
    {
        if (!isServiceRunning(cClass.getName()))
        {
            context.startService(new Intent(context,cClass));
            return true;
        }
        return false;
    }
    public boolean stopService(Class cClass)
    {
        return context.stopService(new Intent(context,cClass));
    }
}
