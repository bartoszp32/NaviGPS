package com.example.logowanie.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.logowanie.models.MyLocation;
import com.example.logowanie.services.ClassService;
import com.example.logowanie.services.ReceiverService;

/**
 * Created by Barti on 23.08.13.
 */
public abstract  class LocationReceiver extends BroadcastReceiver  implements ReceiverService{
     public LocationReceiver() {
        intentFilter = new IntentFilter(ACTION_BROADCAST_LOCATION);
        }

    public abstract void onLocationChange(MyLocation location);
    public static final String ACTION_BROADCAST_LOCATION = "Location_action";
    private IntentFilter intentFilter;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle =  intent.getExtras();
        MyLocation location = bundle.getParcelable(MyLocation.LOCATION_KEY);
        onLocationChange(location);


    }

    @Override
    public IntentFilter getIntentFilter() {
        return intentFilter;
    }

    @Override
    public String getOnRegisterMessage() {
        return ClassService.getRegisterText(this.getClass());
    }

    @Override
    public String getOnUnregisterMessage() {
        return ClassService.getUnregisterName(this.getClass());
    }

}
