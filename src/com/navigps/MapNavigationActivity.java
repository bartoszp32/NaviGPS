package com.navigps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.navigps.R.id;
import com.navigps.managers.LocalDataManager;
import com.navigps.models.DefinedRoute;
import com.navigps.models.MyLocation;
import com.navigps.models.UserRoute;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.receivers.LocationReceiver;
import com.navigps.receivers.NotificationReceiver;
import com.navigps.services.DateProvider;
import com.navigps.services.UsersService;
import com.navigps.tools.DataTools;
import com.navigps.tools.TransparentProgressDialog;

public class MapNavigationActivity extends FragmentActivity {

	private PreferencesProvider preferencesProvider;
	private MyLocationReceiver locationReceiver;
	private LocationManager locationManager;
	private LocalDataManager localDataManager;
	
	private GoogleMap mMap;
	private UiSettings mUiSettings;
	
	private TransparentProgressDialog progressDialog;
	private TextView tvSpeed;
	private TextView tvDistance;
	private CheckBox btnShowOptions;
	private CheckBox btnHideOptions;
	private LinearLayout optionsPanel;
	private RadioGroup btnMapType;
	private Switch btnTraffic;
	private TextView routeName; 
	private Button btnNewTrace;
	private CheckBox btnShowTrace;
	
	private boolean flagTime = true;
	private boolean startMarker = true;
	private String dateFirst;
	private double distance = 0;
	private String userRouteName;
	private LatLngBounds.Builder builder;
	
	private MyLocation currentLocation = null;
	private MyLocation lastLocation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		InitializeComponent();
		progressDialog = new TransparentProgressDialog(this, R.drawable.progress);
		preferencesProvider = new PreferencesProvider(this);
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationReceiver = new MyLocationReceiver();
        this.registerReceiver(locationReceiver, locationReceiver.getIntentFilter());
        localDataManager = new LocalDataManager(getContext());
        setUpService();
        
		setUpMapIfNeeded();

		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		new LoadUserRoute().execute();
		UsersService.getInstance().getUser().setUserReuestDefined(false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();	
	}
	
    protected void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(locationReceiver);
    }

    private void InitializeComponent(){
    	setContentView(R.layout.activity_map_navigation);
		
		tvSpeed = (TextView) findViewById(R.id.tvSpeed);
		tvDistance = (TextView) findViewById(R.id.tvDistance);
		btnShowOptions = (CheckBox) findViewById(id.btnShowOptions);
		btnHideOptions = (CheckBox) findViewById(id.btnHideOptions);
		optionsPanel = (LinearLayout) findViewById(id.optionsPanel);
		btnMapType = (RadioGroup) findViewById(id.mapType);
		btnTraffic = (Switch) findViewById(id.switchTraffic);
		routeName = (TextView) findViewById(R.id.routeName);
		btnNewTrace = (Button) findViewById(id.btnNewTrace);
		btnShowTrace = (CheckBox) findViewById(id.btnShowTrace);
		
		btnShowOptions.setOnClickListener(showOptionsListener);
		btnHideOptions.setOnClickListener(hideOptionsListener);
		btnMapType.setOnCheckedChangeListener(mapTypeListener);
		btnTraffic.setOnCheckedChangeListener(trafficListener);
		btnNewTrace.setOnClickListener(newTraceListener);
		btnShowTrace.setOnClickListener(showTraceListener);
		
		tvSpeed.setText("0");
		tvDistance.setText("0.0");
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
		
	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
				mMap.setOnMyLocationButtonClickListener(myLocationButtonClickListener);
				mMap.setOnMarkerClickListener(onMarkerClickListener);
				mUiSettings = mMap.getUiSettings();
				mUiSettings.setZoomControlsEnabled(true);
	            mUiSettings.setCompassEnabled(true);
	            mUiSettings.setMyLocationButtonEnabled(true);
	            mUiSettings.setScrollGesturesEnabled(true);
	            mUiSettings.setZoomGesturesEnabled(true);
	            mUiSettings.setTiltGesturesEnabled(true);
	            mUiSettings.setRotateGesturesEnabled(true);
			}
			
		}
	}
		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU){
			btnShowOptions.setVisibility(View.INVISIBLE);
			optionsPanel.setVisibility(View.VISIBLE);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private OnClickListener showOptionsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			btnShowOptions.setVisibility(View.INVISIBLE);
			optionsPanel.setVisibility(View.VISIBLE);
		}
	};
	
	private OnClickListener hideOptionsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			btnShowOptions.setVisibility(View.VISIBLE);
			optionsPanel.setVisibility(View.GONE);
		}
	};
	
	private OnClickListener showTraceListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (builder != null) {
				LatLngBounds bounds = builder.build();
				mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
			}
		}
	};
	
	private OnMarkerClickListener onMarkerClickListener = new OnMarkerClickListener() {
		@Override
		public boolean onMarkerClick(final Marker marker) {
			final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1500;

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = Math.max(1 - interpolator
                            .getInterpolation((float) elapsed / duration), 0);
                    marker.setAnchor(0.5f, 1.0f + 2 * t);

                    if (t > 0.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
			return false;
		}
	};
	
	private OnClickListener newTraceListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			DateFormat df = new SimpleDateFormat("yyyy.MM.dd'-'HH.mm.ss");
			String date = df.format(Calendar.getInstance().getTime());
			routeName.setText(preferencesProvider.getUserLogin()+ "-" +date);
			
			mMap.clear();
			setUpMapIfNeeded();
			
			localDataManager.ClearUserRouteTable();
			
			flagTime = true;
			startMarker = true;
			distance = 0;
			UsersService.getInstance().getUser().setUserLastRouteId(UsersService.getInstance().getUser().getUserLastRouteId() + 1);
		}
	};
	
	private OnCheckedChangeListener mapTypeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId)
			{
				case R.id.map_normal:
					mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					break;
				case R.id.map_satelite:
					mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					break;
				case R.id.map_terrain:
					mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
					break;
				case R.id.map_hybrid:
					mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
					break;
			}
		}
	};
	
	private android.widget.CompoundButton.OnCheckedChangeListener trafficListener = new android.widget.CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			mMap.setTrafficEnabled(isChecked);
		}
	};
	
    private class MyLocationReceiver extends LocationReceiver
    {
        @Override
        public void onLocationChange(MyLocation location){
        	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(location.latitude), Double.parseDouble(location.longitude)), mMap.getCameraPosition().zoom));
        	currentLocation = location;
			
        	if(!flagTime) {
        		LatLng point = new LatLng(Double.parseDouble(location.latitude), Double.parseDouble(location.longitude)); 
        		if(startMarker && lastLocation != null)	{
    				mMap.addMarker(new MarkerOptions().position(point).title("Start trasy"));
    				startMarker = false;
    			}
        		builder.include(point);
        		sendToScreen(location);
        		lastLocation = location;
        		UserRoute userRoute = new UserRoute(userRouteName, preferencesProvider.getUserId(), new DefinedRoute("-1", location.latitude, location.longitude), String.valueOf(distance));
        		localDataManager.InsertUserRoutePoint(userRoute);
        	} else {
				dateFirst = DateProvider.getDate();
				flagTime = false;
			}
        }
    }
    
    private void sendToScreen(MyLocation location)
	{    	
    	if (lastLocation != null && currentLocation != null){
    		double lastLatitude = Double.parseDouble(lastLocation.latitude);
    		double lastLongitude = Double.parseDouble(lastLocation.longitude);
    		double currentLatitude = Double.parseDouble(currentLocation.latitude);
    		double currentLongitude = Double.parseDouble(currentLocation.longitude);
    		
    		LatLng lastPoint = new LatLng(lastLatitude, lastLongitude);
    		LatLng currentPoint = new LatLng(currentLatitude, currentLongitude); 
        	mMap.addPolyline(DataTools.getPolyline(lastPoint, currentPoint, Color.BLUE));
        	double calc = CalcGeoDistance(lastLatitude, lastLongitude, currentLatitude, currentLongitude);
        	
        	distance = distance + (calc * 0.001);
    	}
        
    	Double speed = (Double.parseDouble(location.velocity) * 3.6);
		String speedText = DataTools.RoundTo(speed.toString(), ".", 1);
		tvSpeed.setText(speedText);
		
		tvDistance.setText(DataTools.RoundTo(String.valueOf(distance), ".", 1));
	}
    
    private double CalcGeoDistance(final double lat1, final double lon1, final double lat2, final double lon2)
    {
        double distance = 0.0;
        try
        {
            final float[] results = new float[3];
            Location.distanceBetween(lat1, lon1, lat2, lon2, results);
            distance = results[0];
        }
        catch (final Exception ex)
        {
            distance = 0.0;
        }
        return distance;
    }
    
    private Context getContext()
    {
        return this;
    }
    
    private OnMyLocationButtonClickListener myLocationButtonClickListener = new OnMyLocationButtonClickListener() {
		
		@Override
		public boolean onMyLocationButtonClick() {
			if(currentLocation != null){
	        	mMap.setLocationSource(null);
	        	double currentLatitude = Double.parseDouble(currentLocation.latitude);
	    		double currentLongitude = Double.parseDouble(currentLocation.longitude);
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 16));
				return true;
			}
	        return false;
		}
	};

	class LoadUserRoute extends AsyncTask<String, Integer, List<UserRoute>> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		/**
		 * getting All routes from url
		 * */
		protected List<UserRoute> doInBackground(String... args) {
			if (localDataManager.IsUserRoute(preferencesProvider.getUserId()))
				return localDataManager.GetUserRoute(preferencesProvider.getUserId());
			else
				return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(List<UserRoute> userRoute) {

			if (userRoute != null) {
				userRouteName = userRoute.get(0).routeName;
				
				distance = Double.valueOf(userRoute.get(userRoute.size()-1).distance);
				
				DefinedRoute definedRoute = userRoute.get(0).route;
				mMap.addMarker(new MarkerOptions()
				.position(definedRoute.convertToLatLng())
				.title("Pocz¹tek trasy")
				//.snippet(currentTrace.start_name)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				startMarker = false;
				
				PolylineOptions lineOptions = new PolylineOptions();
				builder = new LatLngBounds.Builder();

				LatLng point;
				for(UserRoute route : userRoute){
					point = route.route.convertToLatLng();
					lineOptions.add(point);
					builder.include(point);
				}
				
				mMap.addPolyline(lineOptions
						.width(5)
						.color(Color.BLUE));
				LatLngBounds bounds = builder.build();
				mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
				
				LatLng lastPoint = userRoute.get(userRoute.size()-1).route.convertToLatLng();
				MyLocation myLocation = new MyLocation();
				myLocation.latitude = String.valueOf(lastPoint.latitude);
				myLocation.longitude = String.valueOf(lastPoint.longitude);
				lastLocation = myLocation;
				UsersService.getInstance().getUser().setUserLastRouteId(UsersService.getInstance().getUser().getUserLastRouteId() + 1);
			} else {
				DateFormat df = new SimpleDateFormat("yyyy.MM.dd'-'HH.mm.ss");
				String date = df.format(Calendar.getInstance().getTime());
				userRouteName = preferencesProvider.getUserLogin()+ "-" +date;
				distance = 0;
				startMarker = true;
				builder = new LatLngBounds.Builder();
			}
			
			routeName.setText(userRouteName);
			tvDistance.setText(DataTools.RoundTo(String.valueOf(distance), ".", 1));
			
			progressDialog.dismiss();

		}
	}
}
