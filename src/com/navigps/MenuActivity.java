package com.navigps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.navigps.R.id;
import com.navigps.managers.ServicesManager;
import com.navigps.models.Trace;
import com.navigps.providers.JSONParser;
import com.navigps.providers.LocalDataProvider;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.receivers.NotificationReceiver;
import com.navigps.services.DatabaseService;
import com.navigps.services.UsersService;
import com.navigps.tools.Globals;

public class MenuActivity extends Activity implements OnClickListener {
	private CheckBox btnMapNavigation;
	private CheckBox btnGpsNavigation;
	private CheckBox btnDefinedRoute;
	private CheckBox btnCreateRoute;
	private CheckBox btnCreateRouteDesc;
	private CheckBox btnMyTraces;
	private CheckBox btnToSite;
	private CheckBox btnSettings;
	private CheckBox tbService;
	private CheckBox btnLogout;
	
    private ServicesManager servicesManager;
    private PreferencesProvider preferencesProvider;
    private LocalDataProvider localDataProvider;
    private LocationManager locationManager;
    private String serviceClassName= "";
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		Toast infoText = Toast.makeText(this, "Witaj "
				+ UsersService.getInstance().getUser().getUserName(),
				Toast.LENGTH_SHORT);
		infoText.show();
        serviceClassName = NaviService.class.getName();
			
		btnMapNavigation = (CheckBox) findViewById(id.myMap);
		btnGpsNavigation = (CheckBox) findViewById(id.myGps);
		btnDefinedRoute = (CheckBox) findViewById(id.myRoute);
		btnCreateRoute = (CheckBox) findViewById(id.myCreate);
		btnCreateRouteDesc = (CheckBox) findViewById(id.myRouteDescription);
		btnMyTraces = (CheckBox) findViewById(id.myTraces);
		btnToSite = (CheckBox) findViewById(id.mySite);
		btnSettings = (CheckBox) findViewById(id.mySettings);
		btnLogout = (CheckBox) findViewById(id.myLogout);
		
        servicesManager = new ServicesManager(this);
        localDataProvider = new LocalDataProvider(this);
        preferencesProvider = new PreferencesProvider(this);
        ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		btnMapNavigation.setOnClickListener(mapListener);
		btnGpsNavigation.setOnClickListener(dataListener);
		btnDefinedRoute.setOnClickListener(definedRouteListener);
		btnCreateRoute.setOnClickListener(createRouteListener);
		btnCreateRouteDesc.setOnClickListener(createRouteDescListener);
		btnMyTraces.setOnClickListener(myTracesListener);
		btnToSite.setOnClickListener(siteListener);
		btnSettings.setOnClickListener(settingsListener);
		btnLogout.setOnClickListener(logoutListener);

		//tbGpsService = (CheckBox) findViewById(id.myBoxGps);
		tbService = (CheckBox) findViewById(id.myBoxService);

        tbService.setChecked(servicesManager.isServiceRunning(serviceClassName));
        //tbGpsService.setChecked(preferencesProvider.isLocationEnabled());

		//tbGpsService.setOnClickListener(tbGpsServiceListener);
		tbService.setOnClickListener(tbServiceListener);
	}
	
	private Intent getServiceIntent()
    {
        return new Intent(this, NaviService.class);
    }
    private Context getContext()
    {
        return this;
    }

	/*private OnClickListener tbGpsServiceListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Intent i = new Intent(NaviService.REQUEST_LOCATION_UPDATE);
				if (!NaviService.isLocListener) {
					i.putExtra(NaviService.LOCATION_UPDATE,
							NaviService.LOCATION_UPDATE_START);
					tbGpsService.setChecked(true);
					preferencesProvider.setNotification(true);
				} else {
					i.putExtra(NaviService.LOCATION_UPDATE,
							NaviService.LOCATION_UPDATE_STOP);
					tbGpsService.setChecked(false);
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
	};*/

	private OnClickListener tbServiceListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(NaviService.REQUEST_LOCATION_UPDATE);
			if (servicesManager.isServiceRunning(serviceClassName)) {
				preferencesProvider.setNotification(false);
				getContext().sendBroadcast(NotificationReceiver.sendIntent());
				i.putExtra(NaviService.LOCATION_UPDATE,
						NaviService.LOCATION_UPDATE_STOP);
						//tbGpsService.setChecked(false);
						sendBroadcast(i);
				getContext().stopService(getServiceIntent());
				tbService.setChecked(false);
				preferencesProvider.setLocationEnabled(false);
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
			onBackPressed();
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
	
	private OnClickListener createRouteDescListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent i = new Intent(MenuActivity.this, CreateNewRouteActivity.class);
			//startActivity(i);
			Toast.makeText(getContext(), "Nie zaimplementowane jeszcze!", Toast.LENGTH_LONG).show();
		}
	};
	
	private OnClickListener myTracesListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent i = new Intent(MenuActivity.this, CreateNewRouteActivity.class);
			//startActivity(i);
			Toast.makeText(getContext(), "Nie zaimplementowane jeszcze!", Toast.LENGTH_LONG).show();
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
	@Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Intent i = new Intent(NaviService.REQUEST_LOCATION_UPDATE);
        if (servicesManager.isServiceRunning(serviceClassName)) {
			preferencesProvider.setNotification(false);
			getContext().sendBroadcast(NotificationReceiver.sendIntent());
			i.putExtra(NaviService.LOCATION_UPDATE,
					NaviService.LOCATION_UPDATE_STOP);
					//tbGpsService.setChecked(false);
					sendBroadcast(i);
			getContext().stopService(getServiceIntent());
			tbService.setChecked(false);
			preferencesProvider.setLocationEnabled(false);
        }
        
    }
	
	///TESTOWY
	private ProgressDialog pDialog;
	
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	// url to get all routes list
	private static String url_all_route = "http://www.navigps.cba.pl/menu/get_all_routes.php";
	
	// routes JSONArray
	JSONArray route = null;
	/**
	 * Background Async Task to Load all route by making HTTP Request
	 * */
	class LoadAllRoute extends AsyncTask<String, Integer, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MenuActivity.this);
			pDialog.setMessage("Wczytywanie listy tras...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All routes from url
		 * */
		protected String doInBackground(String... args) {
//			// Building Parameters
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			// getting JSON string from URL
//			JSONObject json = jParser.makeHttpRequest(url_all_route, "GET", params);
//			
//			localDataProvider.clearTable(DatabaseService.TABLE_TRACE);
//			
//			// Check your log cat for JSON reponse
//			Log.d("All Route: ", json.toString());
//
//			try {
//				// Checking for SUCCESS TAG
//				int success = json.getInt(Globals.DEFINED_ROUTE.TAG_SUCCESS);
//
//				if (success == 1) {
//					// routes found
//					// Getting Array of Routes
//					route = json.getJSONArray(Globals.DEFINED_ROUTE.TAG_ROUTES);
//
//					// looping through All Routes
//					for (int i = 0; i < route.length(); i++) {
//						JSONObject c = route.getJSONObject(i);
//						Trace trace = new Trace();
//						// Storing each json item in variable
//						trace.recId = c.getString(Globals.DEFINED_ROUTE.TAG_ROUTE_ID);
//						trace.name = c.getString(Globals.DEFINED_ROUTE.TAG_NAME);
//						trace.start_name = c.getString(Globals.DEFINED_ROUTE.TAG_START_NAME);
//						trace.end_name = c.getString(Globals.DEFINED_ROUTE.TAG_END_NAME);
//						trace.distance = c.getString(Globals.DEFINED_ROUTE.TAG_DISTANCE);
//						trace.designation = c.getString(Globals.DEFINED_ROUTE.TAG_DESIGNATION);
//
//						localDataProvider.insertTraceToDB(trace);
//						// creating new HashMap
////						HashMap<String, String> map = new HashMap<String, String>();
//
//						// adding each child node to HashMap key => value
////						map.put(Globals.DEFINED_ROUTE.TAG_ROUTE_ID, id);
////						map.put(Globals.DEFINED_ROUTE.TAG_NAME, name);
////						map.put(Globals.DEFINED_ROUTE.TAG_DETAILS_ROUTE, startName+" --> "+endName+" ("+distance+" km) ");
////						map.put(Globals.DEFINED_ROUTE.TAG_COLOR_ROUTE, "Oznaczenia "+designation);
//						
//
//						// adding HashList to ArrayList
//						//routeList.add(map);
//					}
//				} else {
//					// no routes found
//					// Launch Add New route Activity
//					Intent i = new Intent(getApplicationContext(),
//							MenuActivity.class);
//					// Closing all previous activities
//					//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(i);
//					//finish();
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all routes
			pDialog.dismiss();
			// updating UI from Background Thread
			List<Trace> allRoutes = localDataProvider.getAllTraces();
			Intent i = new Intent(MenuActivity.this, DefinedRouteActivity.class);
			startActivity(i);

		}
		
	}
}