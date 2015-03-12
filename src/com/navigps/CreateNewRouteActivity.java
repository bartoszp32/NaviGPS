package com.navigps;

import com.navigps.R.id;
import com.navigps.models.MyLocation;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.receivers.LocationReceiver;
import com.navigps.receivers.NotificationReceiver;
import com.navigps.services.DateProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CreateNewRouteActivity extends Activity {
	private MyLocationReceiver locationReceiver;
	private ToggleButton tbStartStop;
	private PreferencesProvider preferencesProvider;
	private LocationManager locationManager;
	private TextView textVelocity;
	private TextView textLongitude;
	private TextView textLatitude;
	private TextView textAltitude;
	private TextView textAccuracy;
	private TextView textDistance;
	private boolean flagTime = true;
	private String dateFirst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_route);
		tbStartStop = (ToggleButton) findViewById(id.tbOnOff);
		textAccuracy = (TextView)findViewById(id.textCreateAccuracy);
		textAltitude = (TextView)findViewById(id.textCreateAltitude);
		textDistance = (TextView)findViewById(id.textCreateDistance);
		textLatitude = (TextView)findViewById(id.textCreateLatitude);
		textLongitude = (TextView)findViewById(id.textCreateLongitude);
		textVelocity = (TextView)findViewById(id.textCreateVelocity);
		
		tbStartStop.setOnClickListener(startStopListener);
		
		
		preferencesProvider = new PreferencesProvider(this);
		tbStartStop.setChecked(NaviService.isLocListener);
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationReceiver = new MyLocationReceiver();
        this.registerReceiver(locationReceiver,locationReceiver.getIntentFilter());
	}

	private Context getContext() {
		return this;
	}

	private OnClickListener startStopListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Intent i = new Intent(NaviService.REQUEST_LOCATION_UPDATE);
				if (!NaviService.isLocListener) {
					i.putExtra(NaviService.LOCATION_UPDATE,
							NaviService.LOCATION_UPDATE_START);
					tbStartStop.setChecked(true);
					preferencesProvider.setNotification(true);
				} else {
					i.putExtra(NaviService.LOCATION_UPDATE,
							NaviService.LOCATION_UPDATE_STOP);
					tbStartStop.setChecked(false);
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

	private class MyLocationReceiver extends LocationReceiver {

		@Override
		public void onLocationChange(MyLocation location) {// , float distance)
															// {
			if (location == null) {
				Toast.makeText(getContext(), "Zaczekaj na wspó³rzêdne GPS",
						Toast.LENGTH_SHORT).show();
			} else {
				if (flagTime) {
					dateFirst = DateProvider.getDate();
					flagTime = false;
				}
				sendToScreen(location);// , distance);
			}
		}
	}

	private void sendToScreen(MyLocation location)// , float distance)
	{
		Double doubleVelocity = Double.valueOf(location.velocity) * 3.6;
		String velocity = roundTo(doubleVelocity.toString(), ".", 1);
		String longitude = roundTo(location.longitude, ".", 5);
		String latitude = roundTo(location.latitude, ".", 5);
		/* String way = roundTo(String.valueOf(distance/1000.0), ".", 1); */
		String altitude = roundTo(location.altitude, ".", 2);
		/*
		 * if(maxVelocity<Double.valueOf(velocity)) {
		 * maxVelocity=Double.valueOf(velocity); } String thisTime =
		 * routeTime(dateFirst, location.date); String average =
		 * avrVelocity(thisTime, String.valueOf(distance/1000.0));
		 * 
		 * average = roundTo(average, ".", 1);
		 */

		textVelocity.setText(velocity + " km/h");
		textLongitude.setText("Szerokosc:  " + whatLongitude(longitude));
		textLatitude.setText("Dlugosc:  " + whatLatitude(latitude));
		textAltitude.setText("Wysokosc:  " + altitude + " m n.p.m.");
		// textDistance.setText("Przebyty dystans:  "+way+" km ");
		textAccuracy.setText("Dokladnosc:  " + location.accuracy + " m ");
		// textMaxVelocity.setText("Max predkosc:  "+maxVelocity+" km/h ");
		// textAverageVelocity.setText("Srednia predkosc:  "+average+" km/h ");
		// textTime.setText("Czas trasy:  "+thisTime+" ");
	}

	public static String roundTo(String value, String symbol, int places) {
		if (value.contains(symbol)) {

			String[] splitted = value.split("\\" + symbol);
			if (splitted[1].length() > places) {
				return splitted[0] + "." + splitted[1].substring(0, places);
			} else {
				return value;
			}
		} else {
			throw new IllegalArgumentException("String " + value
					+ " does not contain " + symbol);
		}
	}

	public static String whatLongitude(String longitude) {
		double tempLongitude = Double.parseDouble(longitude);
		if (tempLongitude > 0.0) {
			return longitude + " E ";
		} else {
			return String.valueOf(-tempLongitude) + " W ";
		}
	}

	public static String whatLatitude(String latitude) {
		double tempLatitude = Double.parseDouble(latitude);
		if (tempLatitude > 0.0) {
			return latitude + " N ";
		} else {
			return String.valueOf(-tempLatitude) + " S ";
		}
	}
}
