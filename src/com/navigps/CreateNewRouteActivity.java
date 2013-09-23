package com.navigps;

import com.navigps.R.id;
import com.navigps.providers.PreferencesProvider;
import com.navigps.receivers.NotificationReceiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;


public class CreateNewRouteActivity extends Activity{
	private ToggleButton tbStartStop;
    private PreferencesProvider preferencesProvider;
    private LocationManager locationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_route);
		tbStartStop = (ToggleButton) findViewById(id.tbOnOff);
		tbStartStop.setOnClickListener(startStopListener);
		preferencesProvider = new PreferencesProvider(this);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	private Context getContext()
    {
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
}
