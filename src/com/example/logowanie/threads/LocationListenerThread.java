package com.example.logowanie.threads;

import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;

import com.example.logowanie.listeners.MyLocationListener;
import com.example.logowanie.providers.PreferencesProvider;

/**
 * Created by Barti on 24.08.13.
 */
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
                if(!isListener&&locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    float minDistance = preferencesProvider.getMinDistance();
                    int minInterval  = preferencesProvider.getMinInterval();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minInterval,minDistance,locationListener);
                    isListener = true;
                }
            }
            else
            {
                locationManager.removeUpdates(locationListener);
                isListener = false;
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
    }
}
