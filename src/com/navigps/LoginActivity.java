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
import com.navigps.models.AdminModel;
import com.navigps.models.User;
import com.navigps.services.UsersService;

public class LoginActivity extends Activity implements OnClickListener{
	private EditText loginText;
	private EditText passwordText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


		Button buttonLogin;
		loginText = (EditText)findViewById(R.id.loginText);
		passwordText = (EditText) findViewById(R.id.passText);
		buttonLogin = (Button)findViewById(R.id.zaloguj);
		
		buttonLogin.setOnClickListener((OnClickListener) this);
	}

	public void onClick(View v) {
		// Pobieramy tekst z pola
	    String writeLogin = "admin";//loginText.getText().toString();
	    String writePassword = "admin";//passwordText.getText().toString();
	    //String wpisanyText = "Login "+wpisanyLogin+"\nHaslo "+wpisanyHaslo;
        AdminModel adminModel = new AdminModel();
	    User admin = adminModel.getAdmin();
	    if(writeLogin.equals(admin.getUserName())&&writePassword.equals(admin.getUserPassword()))
        {
            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(i);
            UsersService.getInstance().setUser(admin);
            
        }
        else
        {
        	Toast.makeText(this, "Wpisz dane administaratora", Toast.LENGTH_SHORT).show();
            
        }

		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
    
	
	

}
