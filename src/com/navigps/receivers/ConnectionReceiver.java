package com.navigps.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.navigps.services.ClassService;
import com.navigps.services.ReceiverService;


public abstract class ConnectionReceiver extends BroadcastReceiver implements ReceiverService{
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
    public String getOnRegisterMessage() {
        return ClassService.getRegisterText(this.getClass());
    }

    @Override
    public String getOnUnregisterMessage() {
        return ClassService.getUnregisterName(this.getClass());
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
