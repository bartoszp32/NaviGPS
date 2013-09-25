package com.navigps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.navigps.R;
import com.navigps.models.UserModel;
import com.navigps.models.User;
import com.navigps.providers.PreferencesProvider;
import com.navigps.providers.ScreenProvider;
import com.navigps.services.UsersService;

public class LoginActivity extends Activity implements OnClickListener{
	private EditText loginText;
	private EditText passwordText;
	private String userName;
	private String userPassword;
	private int userId;
	private PreferencesProvider preferencesProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		preferencesProvider = new PreferencesProvider(this);
		ScreenProvider.setScreenOn(this, preferencesProvider.getScreenOn());
		Button buttonLogin;
		loginText = (EditText)findViewById(R.id.loginText);
		passwordText = (EditText) findViewById(R.id.passText);
		buttonLogin = (Button)findViewById(R.id.log_in);
		
		buttonLogin.setOnClickListener((OnClickListener) this);
		loginText.setText("admin");
		passwordText.setText("admin");

	}

	public void onClick(View v) {
		// Pobieramy tekst z pola
	    String writeLogin = loginText.getText().toString();
	    String writePassword = passwordText.getText().toString();
        UserModel userAdmin = new UserModel();
        User admin = userAdmin.getUser();
        UserModel userModel = new UserModel(userName, userPassword, userId);
        User user = userModel.getUser();
        
	   
	    if(writeLogin.equals(admin.getUserName())&&writePassword.equals(admin.getUserPassword()))
        {
            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(i);
            UsersService.getInstance().setUser(admin);
            preferencesProvider.setLogIn(true);
            preferencesProvider.setUserLogin(admin.getUserName());
            
        }
	    else if(writeLogin.equals(user.getUserName())&&writePassword.equals(user.getUserPassword()))
	    {
	    	Intent i = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(i);
	    	UsersService.getInstance().setUser(user);
	    	preferencesProvider.setLogIn(true);
	    	preferencesProvider.setUserLogin(user.getUserName());
	    }
	    else
        {
        	Toast.makeText(this, "Wpisz poprawne dane u¿ytkownika", Toast.LENGTH_SHORT).show();
            
        }
	}
}
