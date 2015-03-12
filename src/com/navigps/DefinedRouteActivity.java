package com.navigps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.navigps.managers.LocalDataManager;
import com.navigps.models.Trace;
import com.navigps.providers.JSONParser;
import com.navigps.providers.LocalDataProvider;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.tools.Globals;
import com.navigps.tools.TransparentProgressDialog;

public class DefinedRouteActivity extends ListActivity{
	private PreferencesProvider preferencesProvider;
	private LocalDataManager localDataManager;
	private TransparentProgressDialog progressDialog;
	private ArrayList<HashMap<String, String>> routeList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_defined_route);
		preferencesProvider = new PreferencesProvider(this);
		localDataManager = new LocalDataManager(this);
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		routeList = new ArrayList<HashMap<String, String>>();
		progressDialog = new TransparentProgressDialog(this, R.drawable.progress3);
		new LoadAllRoute().execute();

		ListView listView = getListView();

		// on seleting single route
		// launching Map Route Screen
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String routeId = ((TextView) view.findViewById(R.id.idRoute)).getText().toString();
				Intent in = new Intent(getApplicationContext(),	DefinedRouteMapActivity.class);
				in.putExtra(Globals.DEFINED_ROUTE.TAG_ROUTE_ID, routeId);
				
				startActivityForResult(in, 100);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}

	
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
			progressDialog.show();
		}

		/**
		 * getting All routes from url
		 * */
		protected String doInBackground(String... args) {
			for(Trace trace : localDataManager.AllDefinedTrace()){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(Globals.DEFINED_ROUTE.TAG_ROUTE_ID, trace.recId);
				map.put(Globals.DEFINED_ROUTE.TAG_NAME, trace.name);
				map.put(Globals.DEFINED_ROUTE.TAG_DETAILS_ROUTE, trace.start_name+" --> "+trace.end_name+" ("+trace.distance+" km) ");
				map.put(Globals.DEFINED_ROUTE.TAG_COLOR_ROUTE, "Oznaczenia "+trace.designation);
				
				routeList.add(map);
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			progressDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							DefinedRouteActivity.this, routeList,
							R.layout.list_item, new String[] { Globals.DEFINED_ROUTE.TAG_ROUTE_ID,
									Globals.DEFINED_ROUTE.TAG_NAME, Globals.DEFINED_ROUTE.TAG_DETAILS_ROUTE, Globals.DEFINED_ROUTE.TAG_COLOR_ROUTE},
							new int[] { R.id.idRoute, R.id.nameRoute, R.id.detailsRoute, R.id.colorRoute });
					setListAdapter(adapter);
				}
			});

		}
		
	}
}