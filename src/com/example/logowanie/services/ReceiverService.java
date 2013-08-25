package com.example.logowanie.services;

import android.content.IntentFilter;

/**
 * Created by Barti on 24.08.13.
 */
public interface ReceiverService {
    public IntentFilter getIntentFilter();
    public String getOnRegisterMessage();
    public String getOnUnregisterMessage();

}
