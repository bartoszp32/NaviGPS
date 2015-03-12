package com.navigps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.navigps.managers.UserManager;
import com.navigps.providers.LocalDataProvider;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.tools.Globals;

public class NewAccountActivity extends Activity{
	private PreferencesProvider preferencesProvider;
	private LocalDataProvider localDataProvider;
	private EditText etLogin;
	private EditText etPassword;
	private EditText etPassword2;
	private EditText etEmail;
	private EditText etUserName;
	private Button btnSave;
	private Button btnCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		preferencesProvider = new PreferencesProvider(this);
		localDataProvider = new LocalDataProvider(this);
		localDataProvider.cleanUserTablesToStartupApp();
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		
		etLogin = (EditText) findViewById(R.id.etLogin);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etPassword2 = (EditText) findViewById(R.id.etPassword2);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etUserName = (EditText) findViewById(R.id.etUserName);
		btnSave = (Button)findViewById(R.id.btn_create_account);
		btnCancel = (Button)findViewById(R.id.btn_cancel_account);

		btnSave.setOnClickListener(btnSaveListener);
		btnCancel.setOnClickListener(btnCancelListener);
	}
	
	private Context getContext()
	{
		return this;
	}
	
	private OnClickListener btnSaveListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			UserManager userManager = new UserManager(getContext(), etLogin.getText().toString(), etPassword.getText().toString(), 
					etPassword2.getText().toString(), etEmail.getText().toString(), etUserName.getText().toString());
			if (userManager.TryCreateUser() == Globals.CREATE_USER_RESULT.CREATED){
				onBackPressed();
			}
		}
	};
	
	private OnClickListener btnCancelListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();			
		}
	};
}
