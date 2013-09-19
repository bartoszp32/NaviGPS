package com.navigps;

import com.navigps.R.id;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class DefinedRouteMapActivity extends Activity{
	private static final String TAG_ROUTE_ID = "route_id";
	TextView textRoute;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_defined_route_map);
		
		// getting product details from intent
		Intent i = getIntent();
		
		// getting product id (pid) from intent
		String routeId = i.getStringExtra(TAG_ROUTE_ID);
		
		textRoute = (TextView)findViewById(id.textIdRoute);
		textRoute.setText("Wybrana "+routeId+" trasa");
	}

	

}
