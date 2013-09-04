package com.navigps.listeners;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.navigps.managers.MyLocationManager;
import com.navigps.models.MyLocation;
import com.navigps.receivers.LocationReceiver;
import com.navigps.services.DateProvider;
import com.navigps.services.UsersService;


public class MyLocationListener implements LocationListener {
    Context context;

    public MyLocationListener(Context context) {
        this.context = context;



    }

    @Override
    public void onLocationChanged(Location location) {
        MyLocation  myLocation = new MyLocation();
        myLocation.longitude = String.valueOf(location.getLongitude());
        myLocation.latitude = String.valueOf(location.getLatitude());
        myLocation.accuracy = String.valueOf(location.getAccuracy());
        myLocation.velocity = String.valueOf(location.getSpeed());
        myLocation.userId = UsersService.getInstance().getUser().getUserId();
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        myLocation.date = DateProvider.getInstance().getDate();
        myLocation.altitude = String.valueOf(location.getAltitude());
=======
        myLocation.date = DateProvider.getDate();
>>>>>>> 4cb2b0df5d0fcc0e80771e1fcf35c4770d2d3a43
=======
        myLocation.date = DateProvider.getDate();
>>>>>>> 4cb2b0df5d0fcc0e80771e1fcf35c4770d2d3a43
=======
        myLocation.date = DateProvider.getDate();
>>>>>>> 4cb2b0df5d0fcc0e80771e1fcf35c4770d2d3a43
=======
        myLocation.date = DateProvider.getDate();
>>>>>>> 4cb2b0df5d0fcc0e80771e1fcf35c4770d2d3a43
        Intent intent = new Intent(LocationReceiver.ACTION_BROADCAST_LOCATION);
        intent.putExtra(MyLocation.LOCATION_KEY,myLocation);
        MyLocationManager.getInstance().getService().saveLocation(myLocation);
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
