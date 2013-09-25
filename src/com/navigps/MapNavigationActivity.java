package com.navigps;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MapNavigationActivity extends FragmentActivity{// implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	private PreferencesProvider preferencesProvider;
	private GoogleMap mMap;
	//private LocationClient mLocationClient;

	// These settings are the same as the settings for the map. They will in
	// fact give you updates at
	// the maximal rates currently possible.
	/*private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_navigation);
		preferencesProvider = new PreferencesProvider(this);
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		setUpMapIfNeeded();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		//setUpLocationClientIfNeeded();
		//mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		/*if (mLocationClient != null) {
			mLocationClient.disconnect();
		}*/
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
			}
		}
	}

	/*private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	*//**
	 * Implementation of {@link LocationListener}.
	 *//*
	@Override
	public void onLocationChanged(Location location) {
		
	}

	*//**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 *//*
	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	*//**
	 * Callback called when disconnected from GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 *//*
	@Override
	public void onDisconnected() {
		// Do nothing
	}

	*//**
	 * Implementation of {@link OnConnectionFailedListener}.
	 *//*
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}*/
}
