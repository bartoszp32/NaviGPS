package com.navigps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.navigps.models.MyLocation;
import com.navigps.receivers.LocationReceiver;


public class GpsNavigationActivity extends Activity{
    private MyLocationReceiver locationReceiver;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps_navigation);
        locationReceiver = new MyLocationReceiver();
        this.registerReceiver(locationReceiver,locationReceiver.getIntentFilter());
	}
    protected void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(locationReceiver);
    }
    private Context getContext()
    {
        return this;
    }
    private class MyLocationReceiver extends LocationReceiver
    {

        @Override
        public void onLocationChange(MyLocation location) {
                if(location == null)
                {
                    Toast.makeText(getContext(),"NULL",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(),location.date,Toast.LENGTH_SHORT).show();
                }
        }
    }


}
