package com.example.logowanie.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.logowanie.models.MyLocation;

/**
 * Created by Barti on 23.08.13.
 */
public abstract  class LocationBroadcastReceiver extends BroadcastReceiver  {
    public abstract void onLocationChange(MyLocation location);
    public static final String ACTION_BROADCAST_LOCATION = "Location_action";
    @Override
    public void onReceive(Context context, Intent intent) {

           Bundle bundle =  intent.getExtras();
           MyLocation location = bundle.getParcelable(MyLocation.LOCATION_KEY);
           onLocationChange(location);


    }
}
