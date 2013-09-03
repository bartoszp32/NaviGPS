package com.navigps;

import com.navigps.R.id;
import com.navigps.providers.PreferencesProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;


public class SettingsActivity extends Activity implements OnClickListener{
	
	private PreferencesProvider preferencesProvider;
	private Button buttonSave;
	private EditText editMinTime;
	private EditText editMinDistance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	
		preferencesProvider = new PreferencesProvider(this);
		editMinTime = (EditText)findViewById(id.editMinTime);
		editMinDistance	= (EditText)findViewById(id.editMinDistance);
		buttonSave = (Button)findViewById(id.buttonSave);
		buttonSave.setOnClickListener(saveListener);
		
	}
	
	private OnClickListener saveListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            int minTime = Integer.parseInt(editMinTime.getText().toString());
			float minDistance = Float.valueOf(editMinDistance.getText().toString());
			
			preferencesProvider.setMinInterval(minTime);
			preferencesProvider.setMinDistance(minDistance);
			
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

	

}
