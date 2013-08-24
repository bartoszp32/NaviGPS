package com.example.logowanie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.logowanie.models.AdminModel;
import com.example.logowanie.models.User;
import com.example.logowanie.services.UsersService;

public class MainActivity extends Activity implements OnClickListener{
	private EditText login;
	private EditText haslo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);


		Button zaloguj;
		login = (EditText)findViewById(R.id.loginText);
		haslo = (EditText) findViewById(R.id.passText);
		zaloguj = (Button)findViewById(R.id.zaloguj);
		
		zaloguj.setOnClickListener((OnClickListener) this);
	}

	public void onClick(View v) {
		// Pobieramy tekst z pola
	    String wpisanyLogin = login.getText().toString();
	    String wpisanyHaslo = haslo.getText().toString();
	    String wpisanyText = "Login "+wpisanyLogin+"\nHaslo "+wpisanyHaslo;
        AdminModel adminModel = new AdminModel();
	    User admin = adminModel.getAdmin();
	    if(wpisanyLogin.equals(admin.getUserName())&&wpisanyHaslo.equals(admin.getUserPassword()))
        {
            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(i);
            UsersService.getInstance().setUser(admin);
            startService(getServiceIntent());
        }
        else
        {
            stopService(getServiceIntent());
        }

		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
    private Intent getServiceIntent()
    {
        return new Intent(this,NaviService.class);
    }
	
	

}
