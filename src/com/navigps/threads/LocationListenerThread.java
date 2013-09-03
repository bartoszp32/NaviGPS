package com.navigps.threads;

import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;

import com.navigps.listeners.MyLocationListener;
import com.navigps.providers.PreferencesProvider;


public class LocationListenerThread extends AsyncTask<Void,Void,Void> {
    private MyLocationListener locationListener;
    private LocationManager locationManager;
    private PreferencesProvider preferencesProvider;
    private boolean isServiceDestroyed;
    public LocationListenerThread(Context context) {
        locationListener = new MyLocationListener(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        preferencesProvider = new PreferencesProvider(context);

    }
    public void setServiceDestroyed(boolean isServiceDestroyed)
    {
        this.isServiceDestroyed = isServiceDestroyed;

    }
    private boolean isListener = false;
    @Override
    protected Void doInBackground(Void... voids) {
        while (!isServiceDestroyed)
        {

            if(preferencesProvider.isLocationEnabled())
            {
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                   // Looper.prepare();
                    publishProgress();
                    isListener = true;
                }
            }
            else
            {
               if(isListener)
               {
                locationManager.removeUpdates(locationListener);
                isListener = false;
               }
            }
            try {
                Thread.sleep(preferencesProvider.getCheckInterval());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        return null;
    }



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        float minDistance = preferencesProvider.getMinDistance();
        int minInterval  = preferencesProvider.getMinInterval();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minInterval,minDistance,locationListener);

    }
}
