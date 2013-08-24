package com.example.logowanie;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.logowanie.providers.PreferencesProvider;
import com.example.logowanie.receivers.ConnectionReceiver;
import com.example.logowanie.services.AppInfo;
import com.example.logowanie.services.UsersService;
import com.example.logowanie.threads.LocationListenerThread;

/**
 * Created by Barti on 23.08.13.
 */
public class NaviService extends Service {
    private static final String START_SERVICE = "service started";
    private static final String STOP_SERVICE = "service stopped";
    private MyConnectionReceiver myConnectionReceiver;
    private boolean isServiceDestroyed = true;
    private PreferencesProvider preferencesProvider;
    LocationListenerThread locationListenerThread;

    private Context getContext()
    {
        return this;
    }
    private void initialize()
    {
        myConnectionReceiver = new MyConnectionReceiver();
        UsersService.getInstance();
        preferencesProvider = new PreferencesProvider(getContext());
        LocationListenerThread locationListenerThread  = new LocationListenerThread(getContext());
        locationListenerThread.setServiceDestroyed(isServiceDestroyed);


    }
    @Override
    public void onCreate() {
        Log.d(AppInfo.getLogTag(), START_SERVICE);
        isServiceDestroyed = false;
        initialize();
        registerReceiver(myConnectionReceiver, myConnectionReceiver.getIntentFilter());
        startLocationThread();
        super.onCreate();
    }
    private void startLocationThread()
    {
        locationListenerThread.execute();
    }
    @Override
    public void onDestroy() {
        Log.d(AppInfo.getLogTag(), STOP_SERVICE);
        unregisterReceiver(myConnectionReceiver);
        isServiceDestroyed =true;
        locationListenerThread.setServiceDestroyed(isServiceDestroyed);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private class MyConnectionReceiver extends ConnectionReceiver
    {

        @Override
        public void onConnected() {
            Toast.makeText(getContext(),"Data connected",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisconnected() {
            Toast.makeText(getContext(),"Data disconnected",Toast.LENGTH_SHORT).show();
        }
    }

}
