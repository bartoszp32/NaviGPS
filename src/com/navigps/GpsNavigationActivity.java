package com.navigps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.navigps.R.id;
import com.navigps.models.MyLocation;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.receivers.LocationReceiver;
import com.navigps.receivers.NotificationReceiver;
import com.navigps.services.DateProvider;
import com.navigps.services.UsersService;
import com.navigps.tools.DataTools;

public class GpsNavigationActivity extends Activity{
    private MyLocationReceiver locationReceiver;
    private boolean flagTime = true;
    private String dateFirst;
    private Double maxVelocity = 0.0;
    private TextView textVelocity;
	private TextView textLatitude;
	private TextView textLongitude;
	private TextView textAltitude;
	private TextView textDistance;
	private TextView textAccuracy;
	private TextView textMaxVelocity;
	private TextView textAverageVelocity;
	private TextView textTime;	
	private PreferencesProvider preferencesProvider;
	private LocationManager locationManager;
	private CheckBox myGps;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps_navigation);
		preferencesProvider = new PreferencesProvider(this);
		textVelocity = (TextView)findViewById(id.textCreateAltitude);
		textLatitude = (TextView)findViewById(id.textCreateVelocity);
		textLongitude = (TextView)findViewById(id.textCreateLatitude);
		textAltitude = (TextView)findViewById(id.textCreateLongitude);
		textDistance = (TextView)findViewById(id.textCreateAccuracy);
		textAccuracy = (TextView)findViewById(id.textAccuracy);
		textMaxVelocity = (TextView)findViewById(id.textMaxVelocity);
		textAverageVelocity = (TextView)findViewById(id.textAverageVelocity);
		textTime = (TextView)findViewById(id.textTime);
		myGps = (CheckBox)findViewById(id.myBoxGps);
		myGps.setChecked(preferencesProvider.isLocationEnabled());
		myGps.setOnClickListener(tbGpsServiceListener);
		
		
		preferencesProvider = new PreferencesProvider(this);
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationReceiver = new MyLocationReceiver();
        this.registerReceiver(locationReceiver,locationReceiver.getIntentFilter());
        
        setUpService();
        //String str = avrVelocity("01:00:00", "60.0"); 
        //Log.d("AVR", str);
        UsersService.getInstance().getUser().setUserReuestDefined(false);
        UsersService.getInstance().getUser().setUserLastRouteId(UsersService.getInstance().getUser().getUserLastRouteId() + 1);
        
	}
	
	private void setUpService(){
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Intent i = new Intent(NaviService.REQUEST_LOCATION_UPDATE);
			if (!NaviService.isLocListener) {
				i.putExtra(NaviService.LOCATION_UPDATE, NaviService.LOCATION_UPDATE_START);
				preferencesProvider.setNotification(true);
			} 
//			else {
//				i.putExtra(NaviService.LOCATION_UPDATE, NaviService.LOCATION_UPDATE_STOP);
//				preferencesProvider.setNotification(false);
//			}
			sendBroadcast(i);
		} else {
			Toast.makeText(getBaseContext(), "Uruchom GPS",	Toast.LENGTH_SHORT).show();
			preferencesProvider.setNotification(false);
		}
		getContext().sendBroadcast(NotificationReceiver.sendIntent());
	}

    private Context getContext()
    {
        return this;
    }
	private OnClickListener tbGpsServiceListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Intent i = new Intent(NaviService.REQUEST_LOCATION_UPDATE);
				if (!NaviService.isLocListener) {
					i.putExtra(NaviService.LOCATION_UPDATE,
							NaviService.LOCATION_UPDATE_START);
					myGps.setChecked(true);
					preferencesProvider.setNotification(true);
				} else {
					i.putExtra(NaviService.LOCATION_UPDATE,
							NaviService.LOCATION_UPDATE_STOP);
					myGps.setChecked(false);
					preferencesProvider.setNotification(false);
				}
				sendBroadcast(i);
			} else {
				Toast.makeText(getBaseContext(), "Uruchom GPS",
						Toast.LENGTH_SHORT).show();
				preferencesProvider.setNotification(false);
			}

			getContext().sendBroadcast(NotificationReceiver.sendIntent());
		}
	};
    protected void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(locationReceiver);
    }

    private class MyLocationReceiver extends LocationReceiver
    {

        @Override
        public void onLocationChange(MyLocation location){//, float distance) {
                if(location == null)
                {
                    Toast.makeText(getContext(),"Wait on GPS data",Toast.LENGTH_SHORT).show();
                }
                else 
                {
                	if(flagTime)
                	{
						dateFirst = DateProvider.getDate();
						flagTime = false;
					}
                    sendToScreen(location);
                }
        }
    }
    
    private void sendToScreen(MyLocation location)
	{
		Double doubleVelocity = Double.valueOf(location.velocity)*3.6;
		String velocity = DataTools.RoundTo(doubleVelocity.toString(), ".", 1);
		String longitude = DataTools.RoundTo(location.longitude, ".", 5);
		String latitude = DataTools.RoundTo(location.latitude, ".", 5);
		/*String way = roundTo(String.valueOf(distance/1000.0), ".", 1);*/
		String altitude = DataTools.RoundTo(location.altitude,".",2);
		/*if(maxVelocity<Double.valueOf(velocity))
		{
			maxVelocity=Double.valueOf(velocity);
		}
		String thisTime = routeTime(dateFirst, location.date);
		String average = avrVelocity(thisTime, String.valueOf(distance/1000.0));
		
		average = roundTo(average, ".", 1);*/
		
		textVelocity.setText(velocity);
		textLongitude.setText("Szerokosc:  "+DataTools.WhatLongitude(longitude));
		textLatitude.setText("Dlugosc:  "+DataTools.WhatLatitude(latitude));
		textAltitude.setText("Wysokosc:  "+altitude+" m n.p.m.");
		//textDistance.setText("Przebyty dystans:  "+way+" km ");
		textAccuracy.setText("Dokladnosc:  "+location.accuracy+" m ");
		//textMaxVelocity.setText("Max predkosc:  "+maxVelocity+" km/h ");
		//textAverageVelocity.setText("Srednia predkosc:  "+average+" km/h ");
		//textTime.setText("Czas trasy:  "+thisTime+" ");
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
    public String routeTime(String firstTime, String currentTime) {
    	int[] timeFirst = new int[3];
    	int[] timeCurrent = new int[3];
    	int[] time = new int[3];
    	timeFirst[0] = Integer.parseInt(firstTime.substring(11, 13));
    	timeFirst[1] = Integer.parseInt(firstTime.substring(14, 16));
    	timeFirst[2] = Integer.parseInt(firstTime.substring(17, 19));
    	timeCurrent[0] = Integer.parseInt(currentTime.substring(11, 13));
    	timeCurrent[1] = Integer.parseInt(currentTime.substring(14, 16));
    	timeCurrent[2] = Integer.parseInt(currentTime.substring(17, 19));
    	
    	Log.d("TOOO Cu", String.valueOf(timeCurrent[0])+":"+String.valueOf(timeCurrent[1])+":"+String.valueOf(timeCurrent[2]));
    	Log.d("TOOO Fi", String.valueOf(timeFirst[0])+":"+String.valueOf(timeFirst[1])+":"+String.valueOf(timeFirst[2]));
    	time[0] = timeCurrent[0]-timeFirst[0];
    	time[1] = timeCurrent[1]-timeFirst[1];
    	time[2] = timeCurrent[2]-timeFirst[2];
    	Log.d("TOOO", String.valueOf(time[0])+":"+String.valueOf(time[1])+":"+String.valueOf(time[2]));
    	if(time[2]<0){
    		time[1]-=1;
    		time[2]=time[2]+60;
		}
    	if(time[1]<0){
    		time[0]-=1;
    		time[1]=time[1]+60;
    	}

    	Log.d("TOOO CF",String.valueOf(time[0]<=9?"0"+time[0]:time[0])+":"+(time[1]<=9?"0"+time[1]:time[1])+":"+(time[2]<=9?"0"+time[2]:time[2]));

    	return String.valueOf(time[0]==0?"00":time[0])+":"+(time[1]==0?"00":time[1])+":"+(time[2]==0?"00":time[2]);

	}
    public String avrVelocity(String time, String distance) {
    	double hour = Double.parseDouble(time.substring(0, 2))+Double.parseDouble(time.substring(3, 5))/60.0+Double.parseDouble(time.substring(6, 8))/3600.0;
		Log.d("TIME", String.valueOf(hour));
    	double dist = Double.parseDouble(distance);
    	Log.d("DIST", String.valueOf(dist));
    	double value = 0.0;
    	Log.d("DIST", String.valueOf(value));
    	if (dist>0.0&&hour>0.0) {
			value = dist/hour;
		}
    	Log.d("DIST2", String.valueOf(value));
    	return String.valueOf(value);
    			
	}
}

