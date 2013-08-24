package com.example.logowanie.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * Created by Barti on 23.08.13.
 */
public abstract class ConnectionReceiver extends BroadcastReceiver {
    public abstract void onConnected();
    public abstract void onDisconnected();
    private IntentFilter intentFilter;
    private boolean noConnectivity;
    public ConnectionReceiver() {
        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        noConnectivity = false;


    }
    public IntentFilter getIntentFilter()
    {
        return intentFilter;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if(noConnectivity)
        {
            onDisconnected();
        }
        else
        {
            onConnected();
        }

    }


}
