package com.navigps;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import com.navigps.listeners.MyLocationListener;
import com.navigps.managers.MyLocationManager;
import com.navigps.providers.LocalLocationProvider;
import com.navigps.providers.OnlineLocationProvider;
import com.navigps.providers.PreferencesProvider;
import com.navigps.receivers.BatteryReceiver;
import com.navigps.receivers.ConnectionReceiver;
import com.navigps.receivers.NotificationReceiver;
import com.navigps.services.AppInfo;
import com.navigps.services.UsersService;
import com.navigps.threads.FromLocalToOnlineSenderThread;
import com.navigps.threads.LocationListenerThread;


public class NaviService extends Service {
	public static final String REQUEST_LOCATION_UPDATE = "com.barti.request.location";
    public static final int LOCATION_UPDATE_START = 1;
    public static final String LOCATION_UPDATE = "com.barti.location.extra";
    public static final int LOCATION_UPDATE_STOP= 2;
    public static boolean isLocListener = false;
    private static final String START_SERVICE = "service started";
    private static final String STOP_SERVICE = "service stopped";
    private MyConnectionReceiver myConnectionReceiver;
    private boolean isServiceDestroyed = true;
    private PreferencesProvider preferencesProvider;
    private LocalLocationProvider localLocationProvider;
    private OnlineLocationProvider onlineLocationProvider;
    private BatteryReceiver batteryReceiver;
    //private NotificationReceiver notificationReceiver;
    private LocationManager locationManager;
    private MyLocationListener locationListener;

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
        //notificationReceiver = new NotificationReceiver(getContext());
        locationListener = new MyLocationListener(getContext());
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);


    }

    @Override
    public void onCreate() {
        Log.d(AppInfo.getLogTag(), START_SERVICE);
        isServiceDestroyed = false;
        initialize();
         MyLocationManager.getInstance().setService(localLocationProvider);
        registerReceiver(myConnectionReceiver, myConnectionReceiver.getIntentFilter());
        registerReceiver(batteryReceiver, batteryReceiver.getIntentFilter());
        //registerReceiver(notificationReceiver, notificationReceiver.getIntentFilter());
       // startLocationThread();
        super.onCreate();
    }

/*    private void startLocationThread() {
        locationListenerThread.execute();
    }*/

    @Override
    public void onDestroy() {
        Log.d(AppInfo.getLogTag(), STOP_SERVICE);
        unregisterReceiver(myConnectionReceiver);
        unregisterReceiver(batteryReceiver);
        //unregisterReceiver(notificationReceiver);
        isServiceDestroyed = true;
        preferencesProvider.setLocationEnabled(false);
        locationListenerThread.setServiceDestroyed(isServiceDestroyed);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class LocationListenerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(REQUEST_LOCATION_UPDATE.equals(intent.getAction()))
            {
               int extra =  intent.getIntExtra(LOCATION_UPDATE,-1);
               if(extra==LOCATION_UPDATE_START)
               {
                   if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                   {
                	   float minDistance = preferencesProvider.getMinDistance();
                       int minInterval  = preferencesProvider.getMinInterval();
                   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minInterval, minDistance , locationListener);
                       isLocListener = true;
                   }
               }
                else if(extra == LOCATION_UPDATE_STOP)
               {
                   locationManager.removeUpdates(locationListener);
                   isLocListener = false;
               }

            }
        }
    }
    private class MyConnectionReceiver extends ConnectionReceiver {
        private boolean isConnected = false;

        @Override
        public void onConnected() {
          
            new FromLocalToOnlineSenderThread(getContext()).execute();
                MyLocationManager.getInstance().setService(onlineLocationProvider);
        
        }

        @Override
        public void onDisconnected() {
           
                MyLocationManager.getInstance().setService(localLocationProvider);
         
        }
    }

}
