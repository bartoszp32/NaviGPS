package com.navigps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.navigps.providers.JSONParser;

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
import android.widget.Toast;

public class DefinedRouteActivity extends ListActivity{

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> routeList;

	// url to get all routes list
	private static String url_all_route = "http://www.navigps.cba.pl/menu/get_all_routes.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ROUTES = "routes";
	private static final String TAG_ROUTE_ID = "route_id";
	private static final String TAG_NAME = "name";
	private static final String TAG_START_NAME = "start_name";
	private static final String TAG_END_NAME = "end_name";
	private static final String TAG_DISTANCE = "distance";
	private static final String TAG_DESIGNATION = "designation";
	private static final String TAG_DETAILS_ROUTE = "details_route";
	private static final String TAG_COLOR_ROUTE = "color_route";

	// routes JSONArray
	JSONArray route = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_defined_route);

		// Hashmap for ListView
		routeList = new ArrayList<HashMap<String, String>>();
		
		// Loading routes in Background Thread
		new LoadAllRoute().execute();

		// Get listview
		ListView listView = getListView();

		// on seleting single route
		// launching Map Route Screen
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String routeId = ((TextView) view.findViewById(R.id.idRoute)).getText().toString();
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						DefinedRouteMapActivity.class);
				// sending pid to next activity
				in.putExtra(TAG_ROUTE_ID, routeId);
				
				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});

	}

	// Response from Map Route Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// if result code 100 is received 
			// means user edited/deleted route
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}

	/**
	 * Background Async Task to Load all route by making HTTP Request
	 * */
	class LoadAllRoute extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DefinedRouteActivity.this);
			pDialog.setMessage("Loading routes. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All routes from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_route, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All Route: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// routes found
					// Getting Array of Routes
					route = json.getJSONArray(TAG_ROUTES);

					// looping through All Routes
					for (int i = 0; i < route.length(); i++) {
						JSONObject c = route.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_ROUTE_ID);
						String name = c.getString(TAG_NAME);
						String startName = c.getString(TAG_START_NAME);
						String endName = c.getString(TAG_END_NAME);
						String distance = c.getString(TAG_DISTANCE);
						String designation = c.getString(TAG_DESIGNATION);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ROUTE_ID, id);
						map.put(TAG_NAME, name);
						map.put(TAG_DETAILS_ROUTE, startName+" --> "+endName+" ("+distance+" km) ");
						map.put(TAG_COLOR_ROUTE, "Oznaczenia "+designation);
						

						// adding HashList to ArrayList
						routeList.add(map);
					}
				} else {
					// no routes found
					// Launch Add New route Activity
					Intent i = new Intent(getApplicationContext(),
							NewProductActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all routes
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							DefinedRouteActivity.this, routeList,
							R.layout.list_item, new String[] { TAG_ROUTE_ID,
									TAG_NAME, TAG_DETAILS_ROUTE, TAG_COLOR_ROUTE},
							new int[] { R.id.idRoute, R.id.nameRoute, R.id.detailsRoute, R.id.colorRoute });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}