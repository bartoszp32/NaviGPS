package com.navigps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.navigps.R.id;
import com.navigps.models.MyLocation;
import com.navigps.receivers.LocationReceiver;


public class GpsNavigationActivity extends Activity{
    private MyLocationReceiver locationReceiver;
    private TextView editVelocity;
	private TextView editWidth;
	private TextView editLength;
	private TextView editHight;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps_navigation);
		editVelocity = (TextView)findViewById(id.textVelocity);
		editWidth = (TextView)findViewById(id.textWidth);
		editLength = (TextView)findViewById(id.textLength);
		editHight = (TextView)findViewById(id.textHeight);
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
                else 
                {
                    sendToScreen(location);
                }
        }
    }
    
    private void sendToScreen(MyLocation location)
	{
		Double doubleVelocity = Double.valueOf(location.velocity)*3.6;
		int intVelocity = (int) (doubleVelocity*10);
		doubleVelocity = (double) intVelocity/10;
		String velocity = doubleVelocity.toString();
		
		Double doubleLongitude = Double.valueOf(location.longitude);
		int intLongitude = (int) (doubleLongitude*100000);
		doubleLongitude = (double) intLongitude/100000;
		String longitude = doubleLongitude.toString();
		
		Double doubleLatitude = Double.valueOf(location.latitude);
		int intLatitude = (int) (doubleLatitude*100000);
		doubleLatitude = (double) intLatitude/100000;
		String latitude = doubleLatitude.toString();
		
		editVelocity.setText(velocity + " km/h");
		editLength.setText("Szerokoœæ:  "+longitude);
		editWidth.setText("D³ugoœæ:  "+latitude);
		editHight.setText("Wysokoœæ:  "+location.altitude+" m n.p.m.");
	}

}


