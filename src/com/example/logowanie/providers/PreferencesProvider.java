package com.example.logowanie.providers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Barti on 23.08.13.
 */
public class PreferencesProvider {
    private SharedPreferences sharedPreferences;
    private static final String prefsName = "navi_gps_prefs";
    private static final String IS_LOCATION_ENABLED = "location_enabled";
    private static final String MIN_DISTANCE_KEY = "min_distance";
    private static final String MIN_INTERVAL_KEY = "min_interval";
    private static final String CHECK_INTERVAL = "check_interval";


    public PreferencesProvider(Context context) {
        sharedPreferences = context.getSharedPreferences(prefsName,0);
    }
    public boolean isLocationEnabled()
    {
        return sharedPreferences.getBoolean(IS_LOCATION_ENABLED,false);
    }
    public void setLocationEnabled(boolean isLocationEnabled)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOCATION_ENABLED,isLocationEnabled);
        editor.commit();

    }
    public float getMinDistance() {
        return sharedPreferences.getFloat(MIN_DISTANCE_KEY,100.0f) ;
    }
    public void setMinDistance(float minDistance)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(MIN_DISTANCE_KEY, minDistance);
        editor.commit();

    }
    public int getMinInterval() {
        return sharedPreferences.getInt(MIN_INTERVAL_KEY,2*1000);
    }
    public void setMinInterval(int minInterval)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MIN_INTERVAL_KEY,minInterval);
        editor.commit();

    }
    public int getCheckInterval() {
        return sharedPreferences.getInt(CHECK_INTERVAL,3*1000);
    }
    public void setCheckInterval(int minCheckInterval)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CHECK_INTERVAL,minCheckInterval);
        editor.commit();

    }



}
