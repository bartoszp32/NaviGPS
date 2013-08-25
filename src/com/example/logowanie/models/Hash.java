package com.example.logowanie.models;

import android.content.BroadcastReceiver;

/**
 * Created by Barti on 24.08.13.
 */
public class Hash {


    private String tag;
   private BroadcastReceiver broadcastReceiver;

    public Hash(String tag, BroadcastReceiver broadcastReceiver) {
        this.tag = tag;
        this.broadcastReceiver = broadcastReceiver;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    public void setBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        this.broadcastReceiver = broadcastReceiver;
    }

}
