package com.navigps.listeners;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import com.navigps.managers.MyLocationManager;
import com.navigps.models.MyLocation;
import com.navigps.receivers.LocationReceiver;
import com.navigps.services.DateProvider;
import com.navigps.services.UsersService;

public class MyLocationListener implements LocationListener {
    Context context;
	Location lastLocation = null;
	private static float distance;
    public MyLocationListener(Context context) {
        this.context = context;
        distance = 0.0f;
    }

    @Override
    public void onLocationChanged(Location location) {
        MyLocation  myLocation = new MyLocation();
        myLocation.longitude = String.valueOf(location.getLongitude());
        myLocation.latitude = String.valueOf(location.getLatitude());
        myLocation.accuracy = String.valueOf(location.getAccuracy());
        myLocation.velocity = String.valueOf(location.getSpeed());
        myLocation.userId = UsersService.getInstance().getUser().getUserId();
        myLocation.userRouteId = UsersService.getInstance().getUser().getUserLastRouteId();
        myLocation.date = DateProvider.getDate();
        myLocation.altitude = String.valueOf(location.getAltitude());
        myLocation.reuestDefined = UsersService.getInstance().getUser().getUserReuestDefined() ? "Y" : "N";

      //  myLocation.altitude = String.valueOf(location.getAltitude()); 
	   
        //MyLocationManager.getInstance().getService().saveLocation(myLocation);
        new SaveAsync(myLocation).execute();
        
        if(lastLocation != null)
        {
        	distance += location.distanceTo(lastLocation);
        }

        Intent intent = new Intent(LocationReceiver.ACTION_BROADCAST_LOCATION);
        intent.putExtra(MyLocation.LOCATION_KEY,myLocation);
        intent.putExtra("distance",distance);
        context.sendBroadcast(intent);
        lastLocation = location;
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
    private class SaveAsync extends AsyncTask<Void,Void,Void>{
    	private MyLocation location;
    	public SaveAsync(MyLocation location)
    	{
    		this.location = location;
    	}
    	protected Void doInBackground(Void... voids) {
    		MyLocationManager.getInstance().getService().saveLocation(location);
    		return null;
    	}
    }

}



