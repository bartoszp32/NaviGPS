package com.example.logowanie;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.logowanie.managers.MyLocationManager;
import com.example.logowanie.providers.LocalLocationProvider;
import com.example.logowanie.providers.OnlineLocationProvider;
import com.example.logowanie.providers.PreferencesProvider;
import com.example.logowanie.receivers.BatteryReceiver;
import com.example.logowanie.receivers.ConnectionReceiver;
import com.example.logowanie.services.AppInfo;
import com.example.logowanie.services.UsersService;
import com.example.logowanie.threads.LocationListenerThread;


public class NaviService extends Service {
    private static final String START_SERVICE = "service started";
    private static final String STOP_SERVICE = "service stopped";
    private MyConnectionReceiver myConnectionReceiver;
    private boolean isServiceDestroyed = true;
    private PreferencesProvider preferencesProvider;
    private LocalLocationProvider localLocationProvider;
    private OnlineLocationProvider onlineLocationProvider;
    private BatteryReceiver batteryReceiver;

    LocationListenerThread locationListenerThread;

    private Context getContext() {
        return this;
    }

    private void initialize() {
        myConnectionReceiver = new MyConnectionReceiver();
        UsersService.getInstance();
        preferencesProvider = new PreferencesProvider(getContext());
        locationListenerThread = new LocationListenerThread(getContext());
        locationListenerThread.setServiceDestroyed(isServiceDestroyed);
        localLocationProvider = new LocalLocationProvider(getContext());
        onlineLocationProvider = new OnlineLocationProvider();
        batteryReceiver = new BatteryReceiver(getContext());


    }

    @Override
    public void onCreate() {
        Log.d(AppInfo.getLogTag(), START_SERVICE);
        isServiceDestroyed = false;
        initialize();

        registerReceiver(myConnectionReceiver, myConnectionReceiver.getIntentFilter());
        registerReceiver(batteryReceiver, batteryReceiver.getIntentFilter());
        startLocationThread();
        super.onCreate();
    }

    private void startLocationThread() {
        locationListenerThread.execute();
    }

    @Override
    public void onDestroy() {
        Log.d(AppInfo.getLogTag(), STOP_SERVICE);
        unregisterReceiver(myConnectionReceiver);
        unregisterReceiver(batteryReceiver);
        isServiceDestroyed = true;
        locationListenerThread.setServiceDestroyed(isServiceDestroyed);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyConnectionReceiver extends ConnectionReceiver {
        private boolean isConnected = false;

        @Override
        public void onConnected() {
            if (!isConnected) {
                MyLocationManager.getInstance().setService(onlineLocationProvider);
            }
            isConnected = true;
        }

        @Override
        public void onDisconnected() {
            if (isConnected) {
                MyLocationManager.getInstance().setService(localLocationProvider);
            }
            isConnected = false;
        }
    }

}
