package com.navigps;

import java.util.ArrayList;
import java.util.HashMap;

import com.navigps.DefinedRouteActivity.LoadAllRoute;
import com.navigps.R.id;
import com.navigps.managers.LocalDataManager;
import com.navigps.models.Trace;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.services.UsersService;
import com.navigps.tools.Globals;
import com.navigps.tools.TransparentProgressDialog;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class UsersTraceActivity extends ListActivity{
	private PreferencesProvider preferencesProvider;
	private LocalDataManager localDataManager;
	private TransparentProgressDialog progressDialog;
	private ArrayList<HashMap<String, String>> traceList;
	private TextView textViewRouteTitle;
	private ListView listView;
	private String traceId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_defined_route);
		textViewRouteTitle = (TextView) findViewById(id.textViewRouteTitle);
		
		preferencesProvider = new PreferencesProvider(this);
		localDataManager = new LocalDataManager(this);
		traceList = new ArrayList<HashMap<String,String>>();
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		progressDialog = new TransparentProgressDialog(this, R.drawable.progress3);
		textViewRouteTitle.setText("Przejechane trasy " + UsersService.getInstance().getUser().getUserLogin());
		new LoadAllTrace().execute(String.valueOf(UsersService.getInstance().getUser().getUserId()));
		listView = getListView();
		listView.setOnItemClickListener(itemClickListener);	
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			traceId = ((TextView) view.findViewById(R.id.idTrace)).getText().toString();
			String traceName = ((TextView) view.findViewById(R.id.nameTrace)).getText().toString();
			Intent in = new Intent(getApplicationContext(),	UsersTraceMapActivity.class);
			in.putExtra(Globals.DEFINED_ROUTE.TAG_ROUTE_ID, traceId);
			in.putExtra(Globals.DEFINED_ROUTE.TAG_NAME, traceName);
			startActivityForResult(in, 100);
		}
	};
	
	class LoadAllTrace extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		protected ArrayList<HashMap<String, String>> doInBackground(String... args) {
			traceList = new ArrayList<HashMap<String,String>>();
			for(Trace trace : localDataManager.AllDefinedUsersTrace(args[0])){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(Globals.DEFINED_ROUTE.TAG_ROUTE_ID, trace.recId);
				map.put(Globals.DEFINED_ROUTE.TAG_NAME, trace.name);
				
				traceList.add(map);
			}
			return traceList;
		}

		protected void onPostExecute(final ArrayList<HashMap<String, String>> traceList) {
			progressDialog.dismiss();
			
			runOnUiThread(new Runnable() {
				public void run() {
					ListAdapter adapter = new SimpleAdapter(
							UsersTraceActivity.this, traceList,
							R.layout.user_list_item, new String[] { Globals.DEFINED_ROUTE.TAG_ROUTE_ID,	Globals.DEFINED_ROUTE.TAG_NAME},
							new int[] { R.id.idTrace, R.id.nameTrace });
					setListAdapter(adapter);
				}
			});
		}
	}
}
