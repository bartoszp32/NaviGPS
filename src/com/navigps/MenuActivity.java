package com.navigps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.navigps.R.id;
import com.navigps.managers.ServicesManager;
import com.navigps.providers.PreferencesProvider;
import com.navigps.services.UsersService;

public class MenuActivity extends Activity implements OnClickListener {
	private String login;
	private String password;
	private Button buttonMapNavigation;
	private Button buttonGpsNavigation;
	private Button buttonDefinedRoute;
	private Button buttonCreateRoute;
	private Button buttonToSite;
	private Button buttonSettings;
	private ToggleButton tbService;
	private ToggleButton tbGpsService;
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
			
		buttonMapNavigation = (Button) findViewById(id.buttonMap);
		buttonGpsNavigation = (Button) findViewById(id.buttonData);
		buttonDefinedRoute = (Button) findViewById(id.buttonDefinedRoute);
		buttonCreateRoute = (Button) findViewById(id.buttonCreateNewRoute);
		buttonToSite = (Button) findViewById(id.buttonToSite);
		buttonSettings = (Button) findViewById(id.buttonSetting);

        servicesManager = new ServicesManager(this);
        preferencesProvider = new PreferencesProvider(this);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		buttonMapNavigation.setOnClickListener(mapListener);
		buttonGpsNavigation.setOnClickListener(dataListener);
		buttonDefinedRoute.setOnClickListener(definedRouteListener);
		buttonCreateRoute.setOnClickListener(createRouteListener);
		buttonToSite.setOnClickListener(siteListener);
		buttonSettings.setOnClickListener(settingsListener);
		

		tbGpsService = (ToggleButton) findViewById(id.toggleButtonGPS);
		tbService = (ToggleButton) findViewById(id.toggleButtonService);

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
	private OnClickListener tbGpsServiceListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if(servicesManager.isServiceRunning(serviceClassName))
            {
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    if(preferencesProvider.isLocationEnabled())
                    {
                        preferencesProvider.setLocationEnabled(false);
                        showToast("Disabling listener");
                        tbGpsService.setChecked(false);
                    }
                    else
                    {
                        preferencesProvider.setLocationEnabled(true);
                        showToast("Enabling listener");
                        tbGpsService.setChecked(true);
                    }
                }
                else
                {
                    showToast("GPS NOT enabled");
                    tbGpsService.setChecked(false);
                }
            }
            else {
                showToast("Service NOT running");
                tbGpsService.setChecked(false);
            }
		}
	};
	
	private OnClickListener tbServiceListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			 if(servicesManager.isServiceRunning(serviceClassName))
             {
                 getContext().stopService(getServiceIntent());
                 tbService.setChecked(false);
                 tbGpsService.setChecked(false);
             }
            else
             {
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
			Uri uri = Uri.parse("http://www.sledz.webd.pl/ai");
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