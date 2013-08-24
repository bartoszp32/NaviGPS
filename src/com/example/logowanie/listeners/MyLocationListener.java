package com.example.logowanie.listeners;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.example.logowanie.models.MyLocation;
import com.example.logowanie.receivers.LocationBroadcastReceiver;
import com.example.logowanie.services.DateProvider;
import com.example.logowanie.services.UsersService;

/**
 * Created by Barti on 24.07.13.
 */
public class MyLocationListener implements LocationListener {
    Context context;
    MyLocation myLocation;
    public MyLocationListener(Context context) {
        this.context = context;
         myLocation = new MyLocation();


    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation.locationLongitude = String.valueOf(location.getLongitude());
        myLocation.locationLatitude = String.valueOf(location.getLatitude());
        myLocation.locationAccuracy = String.valueOf(location.getAccuracy());
        myLocation.locationVelocity = String.valueOf(location.getSpeed());
        myLocation.userId = UsersService.getInstance().getUser().getUserId();
        myLocation.locationDate = DateProvider.getInstance().getDate();
        Intent intent = new Intent(LocationBroadcastReceiver.ACTION_BROADCAST_LOCATION);
      // intent.putExtra(LocationReceiver.RECEIVE_ACCURACY,location.getAccuracy());
     //   intent.putExtra(LocationReceiver.RECEIVE_LATITUDE,myLocation.latitude);
     ///   intent.putExtra(LocationReceiver.RECEIVE_LONGITUDE,myLocation.longitude);
     ///   intent.putExtra(LocationReceiver.RECEIVE_DATE,myLocation.date);
        context.sendBroadcast(intent);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
