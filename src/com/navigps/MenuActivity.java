package com.navigps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.navigps.R.id;
import com.navigps.managers.ServicesManager;
import com.navigps.providers.PreferencesProvider;
import com.navigps.receivers.NotificationReceiver;
import com.navigps.services.UsersService;

public class MenuActivity extends Activity implements OnClickListener {
	private String login;
	private String password;
	private WakeLock wakeLock;
	private boolean notify; 
	private CheckBox buttonMapNavigation;
	private CheckBox buttonGpsNavigation;
	private CheckBox buttonDefinedRoute;
	private CheckBox buttonCreateRoute;
	private CheckBox buttonToSite;
	private CheckBox buttonSettings;
	private CheckBox tbService;
	private CheckBox tbGpsService;
    private ServicesManager servicesManager;
    private PreferencesProvider preferencesProvider;
    private LocationManager locationManager;
    private String serviceClassName= "";
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		Toast infoText = Toast.makeText(this, "Hello "
				+ (UsersService.getInstance().isUserAdmin() ? "beloved Admin"
						: UsersService.getInstance().getUser().getUserName()),
				Toast.LENGTH_SHORT);
		infoText.show();
        serviceClassName = NaviService.class.getName();
			
		buttonMapNavigation = (CheckBox) findViewById(id.myMap);
		buttonGpsNavigation = (CheckBox) findViewById(id.myGps);
		buttonDefinedRoute = (CheckBox) findViewById(id.myRoute);
		buttonCreateRoute = (CheckBox) findViewById(id.myCreate);
		buttonToSite = (CheckBox) findViewById(id.mySite);
		buttonSettings = (CheckBox) findViewById(id.mySettings);

        servicesManager = new ServicesManager(this);
        preferencesProvider = new PreferencesProvider(this);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		buttonMapNavigation.setOnClickListener(mapListener);
		buttonGpsNavigation.setOnClickListener(dataListener);
		buttonDefinedRoute.setOnClickListener(definedRouteListener);
		buttonCreateRoute.setOnClickListener(createRouteListener);
		buttonToSite.setOnClickListener(siteListener);
		buttonSettings.setOnClickListener(settingsListener);
		

		tbGpsService = (CheckBox) findViewById(id.myBoxGps);
		tbService = (CheckBox) findViewById(id.myBoxService);

        tbService.setChecked(servicesManager.isServiceRunning(serviceClassName));
        tbGpsService.setChecked(preferencesProvider.isLocationEnabled());

		tbGpsService.setOnClickListener(tbGpsServiceListener);
		tbService.setOnClickListener(tbServiceListener);

		
		
    
	}
	
	private Intent getServiceIntent()
    {
        return new Intent(this,NaviService.class);
    }
    private Context getContext()
    {
        return this;
    }
    private class OnStartListenClick implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {   Intent i = new Intent(NaviService.REQUEST_LOCATION_UPDATE);
                if(!NaviService.isLocListener){
                    i.putExtra(NaviService.LOCATION_UPDATE,NaviService.LOCATION_UPDATE_START);
                } else {
                    i.putExtra(NaviService.LOCATION_UPDATE,NaviService.LOCATION_UPDATE_STOP);
                }
                sendBroadcast(i);
            }
            else {
                Toast.makeText(getBaseContext(), "Uruchom GPS", Toast.LENGTH_SHORT).show();
            }
        }
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
					tbGpsService.setChecked(true);
				} else {
					i.putExtra(NaviService.LOCATION_UPDATE,
							NaviService.LOCATION_UPDATE_STOP);
					tbGpsService.setChecked(false);
				}
				sendBroadcast(i);
			} else {
				Toast.makeText(getBaseContext(), "Uruchom GPS",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private OnClickListener tbServiceListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (servicesManager.isServiceRunning(serviceClassName)) {
				getContext().stopService(getServiceIntent());
				tbService.setChecked(false);
				preferencesProvider.setLocationEnabled(false);
				// tbGpsService.setChecked(false);

			} else {

				getContext().startService(getServiceIntent());
				tbService.setChecked(true);

			}

		}
	};
	
	private OnClickListener logoutListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private OnClickListener mapListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(MenuActivity.this, MapNavigationActivity.class);
			startActivity(i);
		}
	};
	private OnClickListener dataListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				Intent i = new Intent(MenuActivity.this, GpsNavigationActivity.class);
				startActivity(i);
		}
	};
	private OnClickListener definedRouteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	
			Intent i = new Intent(MenuActivity.this, DefinedRouteActivity.class);
			startActivity(i);
		}
	};
	private OnClickListener createRouteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(MenuActivity.this, CreateNewRouteActivity.class);
			startActivity(i);
		}
	};
	private OnClickListener siteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Uri uri = Uri.parse("http://www.navigps.cba.pl");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
		}
	};
	private OnClickListener settingsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(MenuActivity.this, SettingsActivity.class);
			startActivity(i);
		}
	};

	public void showToast(String str)
	{
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}