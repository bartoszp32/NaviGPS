package com.navigps;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.navigps.R.drawable;
import com.navigps.R.id;
import com.navigps.managers.LocalDataManager;
import com.navigps.models.DefRoute;
import com.navigps.providers.PreferencesProvider;
import com.navigps.tools.Globals;
import com.navigps.tools.TransparentProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class DefinedRouteDetailActivity extends FragmentActivity {
	private TransparentProgressDialog progressDialog;
	private LocalDataManager localDataManager;
	private LocationManager locationManager;
	private PreferencesProvider preferencesProvider;
	private String routeId;
	private CheckBox btnGoMap;
	private ImageView imgTrace;
	private TextView tvTraceName;
	private TextView tvWay;
	private TextView tvDescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		InitializeComponent();
		
		preferencesProvider = new PreferencesProvider(this);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		progressDialog = new TransparentProgressDialog(this, R.drawable.progress);
		localDataManager = new LocalDataManager(this);
		routeId = getIntent().getStringExtra(Globals.DEFINED_ROUTE_MAP.TAG_ROUTE_ID);
		
		new LoadDataRoute().execute();
	}
	
	private void InitializeComponent(){
    	setContentView(R.layout.activity_defined_route_detail);
		
		btnGoMap = (CheckBox) findViewById(id.btnGoMap);
		imgTrace = (ImageView) findViewById(id.imgTrace);
		tvTraceName = (TextView) findViewById(id.tvNameTrace);
		tvWay = (TextView) findViewById(id.tvWay);
		tvDescription = (TextView) findViewById(id.tvDescription);

		btnGoMap.setOnClickListener(goMapListener);

    }
	
	private boolean isGpsEnabled() {
    	return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    
    private boolean isNetworkOnline() {
    	return preferencesProvider.isNetworkOnline();
    }
	
	private OnClickListener goMapListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isGpsEnabled()) {
				Toast.makeText(getBaseContext(), "Uruchom GPS",	Toast.LENGTH_SHORT).show();
			} else if (!isNetworkOnline()) {
				Toast.makeText(getBaseContext(), "Uruchom dane mobilne", Toast.LENGTH_SHORT).show();
			} else {
				Intent in = new Intent(getApplicationContext(),	DefinedRouteMapActivity.class);
				in.putExtra(Globals.DEFINED_ROUTE.TAG_ROUTE_ID, routeId);
				startActivityForResult(in, 100);
			}
		}
	};
	
	
	private class LoadDataRoute extends AsyncTask<String, String, DefRoute> {
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
	    }

		protected DefRoute doInBackground(String... args) {
			return localDataManager.GetDataForRoute(routeId);
		}

		protected void onPostExecute(DefRoute defRoute) {
			if (defRoute != null) {
				if (defRoute.image != null) {
					imgTrace.setImageBitmap(defRoute.image);
				}
					
				tvTraceName.setText(defRoute.name);
				tvWay.setText(Html.fromHtml(defRoute.way));
				tvDescription.setText(Html.fromHtml(defRoute.description));
				
			}
			progressDialog.dismiss();
		}
	}
	
	

}
