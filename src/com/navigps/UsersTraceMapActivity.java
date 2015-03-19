package com.navigps;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.navigps.DefinedRouteMapActivity.LoadAllRoute;
import com.navigps.R.id;
import com.navigps.managers.LocalDataManager;
import com.navigps.models.DefinedRoute;
import com.navigps.models.Trace;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.services.UsersService;
import com.navigps.tools.Globals;
import com.navigps.tools.TransparentProgressDialog;

import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class UsersTraceMapActivity extends FragmentActivity {
	private TransparentProgressDialog progressDialog;
	private PreferencesProvider preferencesProvider;
	private LocalDataManager localDataManager;
	private String routeId;
	private GoogleMap mMap;
	private UiSettings mUiSettings;
	private LatLngBounds.Builder builder;
	private CheckBox btnShowTrace;
	private CheckBox btnShowOptions;
	private CheckBox btnHideOptions;
	private RadioGroup btnMapType;
	private Switch btnTraffic;
	private TextView routeName; 
	private LinearLayout optionsPanel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_trace);
		btnShowTrace = (CheckBox) findViewById(id.btnShowTrace);
		btnShowOptions = (CheckBox) findViewById(id.btnShowOptions);
		btnHideOptions = (CheckBox) findViewById(id.btnHideOptions);
		optionsPanel = (LinearLayout) findViewById(id.optionsPanel);
		btnMapType = (RadioGroup) findViewById(id.mapType);
		btnTraffic = (Switch) findViewById(id.switchTraffic);
		routeName = (TextView) findViewById(R.id.routeName);
		
		btnShowTrace.setOnClickListener(showTraceListener);
		btnShowOptions.setOnClickListener(showOptionsListener);
		btnHideOptions.setOnClickListener(hideOptionsListener);
		btnMapType.setOnCheckedChangeListener(mapTypeListener);
		btnTraffic.setOnCheckedChangeListener(trafficListener);
		
		progressDialog = new TransparentProgressDialog(this, R.drawable.progress3);
		preferencesProvider = new PreferencesProvider(this);
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		localDataManager = new LocalDataManager(this);
        routeId = getIntent().getStringExtra(Globals.DEFINED_ROUTE_MAP.TAG_ROUTE_ID);
        String traceName = getIntent().getStringExtra(Globals.DEFINED_ROUTE.TAG_NAME);
        routeName.setText(traceName);
		setUpMapIfNeeded();

		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		new LoadAllRoute().execute();
		UsersService.getInstance().getUser().setUserReuestDefined(false);
		UsersService.getInstance().getUser().setUserLastRouteId(UsersService.getInstance().getUser().getUserLastRouteId() + 1);
	}
	
	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				//mMap.setMyLocationEnabled(true);
				//mMap.setOnMyLocationButtonClickListener(myLocationButtonClickListener);
				mMap.setOnMarkerClickListener(onMarkerClickListener);
				mUiSettings = mMap.getUiSettings();
				mUiSettings.setZoomControlsEnabled(true);
	            mUiSettings.setCompassEnabled(true);
	            //mUiSettings.setMyLocationButtonEnabled(true);
	            mUiSettings.setScrollGesturesEnabled(true);
	            mUiSettings.setZoomGesturesEnabled(true);
	            mUiSettings.setTiltGesturesEnabled(true);
	            mUiSettings.setRotateGesturesEnabled(true);
			}
			
		}
	}
	
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
	
	private OnClickListener showTraceListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			LatLngBounds bounds = builder.build();
			mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
		}
	};
	
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
	
	class LoadAllRoute extends AsyncTask<String, Integer, List<DefinedRoute> > {

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
		protected List<DefinedRoute>  doInBackground(String... args) {
			return localDataManager.ExistedUserTrace(String.valueOf(UsersService.getInstance().getUser().getUserId()),routeId);
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(List<DefinedRoute> definedRoute) {
			
			LatLng firsPosition = definedRoute.get(0).convertToLatLng();
			LatLng lastPosition = definedRoute.get(definedRoute.size()-1).convertToLatLng();

			//Trace currentTrace = localDataManager.GetTrace(routeId);
			
			//routeName.setText(currentTrace.name);
			
			mMap.addMarker(new MarkerOptions()
				.position(firsPosition)
				.title("Pocz¹tek trasy")
				//.snippet(currentTrace.start_name)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			
			mMap.addMarker(new MarkerOptions()
				.position(lastPosition)
				.title("Koniec trasy")
				//.snippet(currentTrace.end_name)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

			PolylineOptions lineOptions = new PolylineOptions();
			builder = new LatLngBounds.Builder();

			LatLng point;
			for(DefinedRoute route : definedRoute){
				point = route.convertToLatLng();
				lineOptions.add(point);
				builder.include(point);
			}
			
			mMap.addPolyline(lineOptions
					.width(5)
					.color(Color.RED));
			LatLngBounds bounds = builder.build();
			mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
			
			progressDialog.dismiss();

		}
		
	}

}
