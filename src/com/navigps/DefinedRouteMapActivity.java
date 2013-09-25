package com.navigps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.navigps.R.id;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class DefinedRouteMapActivity extends FragmentActivity {

	private static final String TAG_ROUTE_ID = "route_id";
	TextView textRoute;
	private PreferencesProvider preferencesProvider;
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_defined_route_map);
		preferencesProvider = new PreferencesProvider(this);
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());

		// getting product details from intent
		Intent i = getIntent();

		// getting product id (pid) from intent
		String routeId = i.getStringExtra(TAG_ROUTE_ID);

		textRoute = (TextView) findViewById(id.textTitle);
		textRoute.setText("Wybrana " + routeId + " trasa");
		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		// setUpLocationClientIfNeeded();
		// mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		/*
		 * if (mLocationClient != null) { mLocationClient.disconnect(); }
		 */
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.routeMap)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
			}
		}
	}
}
