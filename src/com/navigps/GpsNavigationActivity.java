package com.navigps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.navigps.R.id;
import com.navigps.models.MyLocation;
import com.navigps.receivers.LocationReceiver;

public class GpsNavigationActivity extends Activity{
    private MyLocationReceiver locationReceiver;
    private TextView textVelocity;
	private TextView textLatitude;
	private TextView textLongitude;
	private TextView textAltitude;
	private TextView textDistance;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps_navigation);
		textVelocity = (TextView)findViewById(id.textVelocity);
		textLatitude = (TextView)findViewById(id.textWidth);
		textLongitude = (TextView)findViewById(id.textLength);
		textAltitude = (TextView)findViewById(id.textHeight);
		textDistance = (TextView)findViewById(id.textDistance);
        locationReceiver = new MyLocationReceiver();
        this.registerReceiver(locationReceiver,locationReceiver.getIntentFilter());
        Toast.makeText(getContext(),"Wait on GPS data",Toast.LENGTH_SHORT).show();
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
        public void onLocationChange(MyLocation location, float distance) {
                if(location == null)
                {
                    Toast.makeText(getContext(),"Wait on GPS data",Toast.LENGTH_SHORT).show();
                }
                else 
                {
                    sendToScreen(location, distance);
                }
        }
    }
    
    private void sendToScreen(MyLocation location, float distance)
	{
		Double doubleVelocity = Double.valueOf(location.velocity)*3.6;
		String velocity = roundTo(doubleVelocity.toString(), ".", 1);
		String longitude = roundTo(location.longitude, ".", 5);
		String latitude = roundTo(location.latitude, ".", 5);
		String way = roundTo(String.valueOf(distance), ".", 1);
		String altitude = roundTo(location.altitude,".",2);
		textVelocity.setText(velocity + " km/h");
		textLongitude.setText("Szerokosc:  "+whatLongitude(longitude));
		textLatitude.setText("Dlugosc:  "+whatLatitude(latitude));
		textAltitude.setText("Wysokosc:  "+altitude+" m n.p.m.");
		textDistance.setText("Przebyty dystans:  "+way+" km ");
		
	}

    public static String roundTo(String value, String symbol, int places){
    	if(value.contains(symbol)){

	    	String[] splitted = value.split("\\"+symbol);
	    	if(splitted[1].length() > places){
	    		return splitted[0] + "." + splitted[1].substring(0,places);
	    	}
	    	else{
	    		return value;
	    	}
    	}
    	else{
    		throw new IllegalArgumentException("String " + value + " does not contain " + symbol);
    	}
    }
    public static String whatLongitude(String longitude)
    {
    	double tempLongitude = Double.parseDouble(longitude);
    	if(tempLongitude > 0.0)
    	{
    		return longitude+ " E ";
    	}
    	else
    	{
    		return String.valueOf(-tempLongitude)+ " W ";
    	}
    }
    public static String whatLatitude(String latitude)
    {
    	double tempLatitude = Double.parseDouble(latitude);
    	if(tempLatitude > 0.0)
    	{
    		return latitude+ " N ";
    	}
    	else
    	{
    		return String.valueOf(-tempLatitude)+ " S ";
    	}
    }
    
}

