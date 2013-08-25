package com.example.logowanie.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.example.logowanie.providers.PreferencesProvider;
import com.example.logowanie.services.ClassService;
import com.example.logowanie.services.ReceiverService;

/**
 * Created by Barti on 24.08.13.
 */
public class BatteryReceiver extends BroadcastReceiver implements ReceiverService{
    private int batteryLevel = -1;
    private int rawLevel = -1;
    private int status = -1;
    private int scale =-1;
    private PreferencesProvider preferencesProvider;
    private IntentFilter intentFilter;
    public BatteryReceiver(Context context) {
        preferencesProvider = new PreferencesProvider(context);
        intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
         rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
         scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
         status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
         batteryLevel = (rawLevel * 100) / scale;
        if(isBatteryLevelLow())
        {
            onLowLevel();
        }


    }
    private void onLowLevel()
    {
        ////
    }
    private boolean isBatteryLevelLow()
    {
        return batteryLevel < preferencesProvider.getLowBatteryLevel() && status == BatteryManager.BATTERY_STATUS_NOT_CHARGING;
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
}
