package com.navigps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.navigps.managers.LocalDataManager;
import com.navigps.managers.UserManager;
import com.navigps.models.User;
import com.navigps.models.UserModel;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.services.UsersService;
import com.navigps.tools.Globals;

public class LoginActivity extends Activity implements OnClickListener{
	private EditText loginText;
	private EditText passwordText;
	private Button buttonLogin;
	private Button buttonNewAccount;

	private PreferencesProvider preferencesProvider;
	private LocalDataManager localDataManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		preferencesProvider = new PreferencesProvider(this);
		localDataManager = new LocalDataManager(this);
		localDataManager.cleanUserTablesToStartupApp();
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		
		loginText = (EditText)findViewById(R.id.loginText);
		passwordText = (EditText) findViewById(R.id.passText);
		buttonLogin = (Button)findViewById(R.id.log_in);
		buttonNewAccount = (Button)findViewById(R.id.new_account);
		
		buttonLogin.setOnClickListener((OnClickListener) this);
		buttonNewAccount.setOnClickListener(newAccountListener);
		
		/////----TO WYWALIÆ-----------------------------
		loginText.setText("admin");
		passwordText.setText("admin");
		/////----TO WYWALIÆ-----------------------------
	}
	
	private Intent getServiceIntent()
    {
        return new Intent(this, NaviService.class);
    }
    private Context getContext()
    {
        return this;
    }
	
	private OnClickListener newAccountListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(LoginActivity.this, NewAccountActivity.class);
			startActivityForResult(i, 1);
		}
	};
	
	
	public void onClick(View v) {
		if (preferencesProvider.isNetworkOnline()) {
			String userName = loginText.getText().toString();
		    String userPassword = passwordText.getText().toString();
		   
		    UserManager userManager = new UserManager(this, userName, userPassword);
		    if (userManager.TryLoginUser() == Globals.LOGIN_RESULT.LOGGED){
		    	User user = localDataManager.GetUser(userName);
				UsersService.getInstance().setUser(user);//admin
				preferencesProvider.setLogIn(true);
				//preferencesProvider.setUser(user);
				preferencesProvider.setUserLogin(user.getUserLogin(), user.getUserName(), String.valueOf(user.getUserId()), user.isAdmin());
				getContext().startService(getServiceIntent());
				Intent i = new Intent(this, MenuActivity.class);
		    	startActivity(i);
		    }
		} else
			Toast.makeText(this, "Problem z po³¹czeniem Internetowym!", Toast.LENGTH_LONG).show();
	}

	public void StartMenuActivity(){
		Intent i = new Intent(LoginActivity.this, MenuActivity.class);
		startActivityForResult(i, 1);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		this.finish();
	}
}
