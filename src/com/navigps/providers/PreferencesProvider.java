package com.navigps.providers;

import com.navigps.models.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class PreferencesProvider {
    private SharedPreferences sharedPreferences;
    private Context context;
    private static final String prefsName = "navi_gps_prefs";
    private static final String IS_LOCATION_ENABLED = "location_enabled";
    private static final String MIN_DISTANCE_KEY = "min_distance";
    private static final String MIN_INTERVAL_KEY = "min_interval";
    private static final String CHECK_INTERVAL = "check_interval";
    private static final String LOW_BATTERY_LEVEL = "low_battery_level";
    private static final String NOTIFICATION = "notification";
    private static final String LOG_IN_STRING = "log_in";
    private static final String LAST_USER_LOGIN = "user_login";
    private static final String LAST_USER_LOGIN_ID = "user_id";
    private static final String LAST_USER_NAME = "user_name";
    private static final String LAST_USER_IS_ADMIN = "user_is_admin";
    public static final String UNKNOWN_USER = "Unknown";
    private static final String SCREEN_ON = "screen_on";


    public PreferencesProvider(Context context) {
        sharedPreferences = context.getSharedPreferences(prefsName,0);
        this.context = context;
    }
    public boolean getScreenOn()
    {
        return sharedPreferences.getBoolean(SCREEN_ON,false);
    }
    public void setScreenOn(boolean screenOn)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SCREEN_ON, screenOn);
        editor.commit();
    }
    public void setLogIn(boolean logIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOG_IN_STRING, logIn);
        editor.commit();
    }

    public boolean getLogIn() {
        return sharedPreferences.getBoolean(LOG_IN_STRING, false);
    }
    public void setUserLogin(String userId, String login, String name, boolean isAdmin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_USER_LOGIN_ID, userId);
        editor.putString(LAST_USER_LOGIN, login);
        editor.putString(LAST_USER_NAME, name);
        editor.putBoolean(LAST_USER_IS_ADMIN, isAdmin);
        editor.commit();
    }

    public String getUserLogin() {
        return sharedPreferences.getString(LAST_USER_LOGIN, UNKNOWN_USER);
    }
    public String getUserId() {
        return sharedPreferences.getString(LAST_USER_LOGIN_ID, "");
    }
    public String getUserName() {
        return sharedPreferences.getString(LAST_USER_NAME, UNKNOWN_USER);
    }
    public boolean getUserIsAdmin() {
        return sharedPreferences.getBoolean(LAST_USER_IS_ADMIN, false);
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
        return sharedPreferences.getFloat(MIN_DISTANCE_KEY,100.0f);
    }
    public void setMinDistance(float minDistance)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(MIN_DISTANCE_KEY, minDistance);
        editor.commit();

    }
    public int getMinInterval() {
        return sharedPreferences.getInt(MIN_INTERVAL_KEY,3*1000);
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
    public int getLowBatteryLevel()
    {
        return sharedPreferences.getInt(LOW_BATTERY_LEVEL,20);
    }
    public void setLowBatteryLevel(int level)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LOW_BATTERY_LEVEL,level);
        editor.commit();
    }
   

    public boolean getNotification()
    {
       return sharedPreferences.getBoolean(NOTIFICATION,false);
    }
    public void setNotification(boolean notification)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NOTIFICATION, notification);
        editor.commit();
    }

    public boolean isNetworkOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    
	public void setUser(User user) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_USER_LOGIN_ID, String.valueOf(user.getUserId()));
        editor.putString(LAST_USER_LOGIN, user.getUserLogin());
        editor.putString(LAST_USER_NAME, user.getUserName());
        editor.putBoolean(LAST_USER_IS_ADMIN, user.isAdmin());
        editor.putBoolean(LAST_USER_IS_ADMIN, user.isAdmin());
        editor.commit();
		
	}
}
