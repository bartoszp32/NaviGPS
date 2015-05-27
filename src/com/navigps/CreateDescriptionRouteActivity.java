package com.navigps;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.navigps.R.id;
import com.navigps.managers.LocalDataManager;
import com.navigps.providers.PreferencesProvider;
import com.navigps.services.UsersService;
import com.navigps.tools.Globals;
import com.navigps.tools.TransparentProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateDescriptionRouteActivity extends Activity {
	private TransparentProgressDialog progressDialog;
	private LocalDataManager localDataManager;
	private PreferencesProvider preferencesProvider;
	
	private EditText etTraceName;
	private EditText etTraceStart;
	private EditText etTraceEnd;
	private EditText etTraceWay;
	private EditText etTraceDistance;
	private EditText etTraceDesignation;
	private EditText etTraceDescription;
	
	private CheckBox btnSaveRoute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_create_description_for_route);
		etTraceName = (EditText) findViewById(id.newRouteName);
		etTraceStart = (EditText) findViewById(id.newRouteStart);
		etTraceEnd = (EditText) findViewById(id.newRouteEnd);
		etTraceWay = (EditText) findViewById(id.newRouteWay);
		etTraceDistance = (EditText) findViewById(id.newRouteDistance);
		etTraceDesignation = (EditText) findViewById(id.newRouteColor);
		etTraceDescription = (EditText) findViewById(id.newRouteDescription);
		btnSaveRoute = (CheckBox) findViewById(id.btnNewRoute);

		btnSaveRoute.setOnClickListener(saveRouteListener);
		
		preferencesProvider = new PreferencesProvider(this);
		progressDialog = new TransparentProgressDialog(this, R.drawable.progress);
		localDataManager = new LocalDataManager(this);
	}
	
	private OnClickListener saveRouteListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (!isNetworkOnline()) {
				Toast.makeText(getBaseContext(), "Uruchom dane mobilne", Toast.LENGTH_SHORT).show();
			} else {
				new SaveDataDescription().execute();
			}
			
		}
	};
	
	private boolean isNetworkOnline() {
    	return preferencesProvider.isNetworkOnline();
    }
	
	private class SaveDataDescription extends AsyncTask<String, String, Boolean> {
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
	    }

		protected Boolean doInBackground(String... args) {
			return localDataManager.SaveDescriptionRoute(
					String.valueOf(UsersService.getInstance().getUser().getUserId()),
					etTraceName.getText().toString(),
					"1111",//___________________________________ZMIEÑ_________________________________________________________
					etTraceStart.getText().toString(),
					etTraceEnd.getText().toString(),
					etTraceWay.getText().toString(),
					etTraceDistance.getText().toString(),
					etTraceDesignation.getText().toString(),
					etTraceDescription.getText().toString());
		}

		protected void onPostExecute(Boolean saved) {
			progressDialog.dismiss();
		}
	}
	
	

}
