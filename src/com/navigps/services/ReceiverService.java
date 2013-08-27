package com.navigps.services;

import android.content.IntentFilter;


public interface ReceiverService {
    public IntentFilter getIntentFilter();
    public String getOnRegisterMessage();
    public String getOnUnregisterMessage();

}
