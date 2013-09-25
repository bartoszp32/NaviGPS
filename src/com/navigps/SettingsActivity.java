package com.navigps;

import com.navigps.R.id;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class SettingsActivity extends Activity implements OnClickListener{
	
	private static final String DEFAULT_MIN_TIME = "3000";
	private static final String DEFAULT_MIN_DISTANCE = "100.0";
	private static final String DEFAULT_MIN_TIME_CHECK = "3000";
	private PreferencesProvider preferencesProvider;
	private Button buttonSave;
	private	Button buttonCancel;
	private Button buttonSetDefault;
	private EditText editMinTime;
	private EditText editMinDistance;
	private EditText editCheckTime;
	private CheckBox checkBoxScreen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	
		preferencesProvider = new PreferencesProvider(this);
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		editMinTime = (EditText)findViewById(id.editMinTime);
		editMinTime.setText(String.valueOf(preferencesProvider.getMinInterval()));
		editMinDistance	= (EditText)findViewById(id.editMinDistance);
		editMinDistance.setText(String.valueOf(preferencesProvider.getMinDistance()));
		editCheckTime = (EditText)findViewById(id.editSequenceSettings);
		editCheckTime.setText(String.valueOf(preferencesProvider.getCheckInterval()));
		
		checkBoxScreen = (CheckBox) findViewById(id.checkBoxScreenOn);
		checkBoxScreen.setChecked(preferencesProvider.getScreenOn());
		checkBoxScreen.setOnClickListener(screenListener);
		
		buttonSave = (Button)findViewById(id.buttonSave);
		buttonSave.setOnClickListener(saveListener);
		buttonCancel = (Button)findViewById(id.buttonCancel);
		buttonCancel.setOnClickListener(cancelListener);
		buttonSetDefault = (Button)findViewById(id.buttonSetDefault);
		buttonSetDefault.setOnClickListener(setDefaultListener);

        
		
	}
	private OnClickListener screenListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
   			//preferencesProvider.setScreenOn(!preferencesProvider.getScreenOn());
   			//checkBoxScreen.setChecked(preferencesProvider.getScreenOn());
		}
	};
	private OnClickListener cancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
   			finish();
		}
	};
	
	private OnClickListener setDefaultListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int minTime = Integer.parseInt(DEFAULT_MIN_TIME);
			float minDistance = Float.valueOf(DEFAULT_MIN_DISTANCE);
			int checkTime = Integer.parseInt(DEFAULT_MIN_TIME_CHECK);
			
			preferencesProvider.setMinInterval(minTime);
			preferencesProvider.setMinDistance(minDistance);
			preferencesProvider.setCheckInterval(checkTime);
			preferencesProvider.setScreenOn(false);
			
			
			editMinTime.setText(String.valueOf(preferencesProvider.getMinInterval()));
			editMinDistance.setText(String.valueOf(preferencesProvider.getMinDistance()));
			editCheckTime.setText(String.valueOf(preferencesProvider.getCheckInterval()));
			checkBoxScreen.setChecked(false);
			
			makeToast("Przywrócono domyœlne!");
		}
	};
	
	private OnClickListener saveListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            int minTime = Integer.parseInt(editMinTime.getText().toString());
			float minDistance = Float.valueOf(editMinDistance.getText().toString());
			int checkTime = Integer.parseInt(editCheckTime.getText().toString());
			
			preferencesProvider.setMinInterval(minTime);
			preferencesProvider.setMinDistance(minDistance);
			preferencesProvider.setCheckInterval(checkTime);
			preferencesProvider.setScreenOn(checkBoxScreen.isChecked());
			
			finish();
			
		}
	};
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	public void makeToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();		
	}
	

}
