package com.navigps.managers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.navigps.LoginActivity;
import com.navigps.MenuActivity;
import com.navigps.R;
import com.navigps.models.UserModel;
import com.navigps.providers.PreferencesProvider;
import com.navigps.services.UsersService;
import com.navigps.tools.DataTools;
import com.navigps.tools.Globals;
import com.navigps.tools.TransparentProgressDialog;

public class UserManager {
	private Context context;
	private String login;
	private String password;
	private String password2;
	private String email;
	private String userName;
	private TransparentProgressDialog progressDialog; 
	private LocalDataManager localDataManager;
	private PreferencesProvider preferencesProvider;
	
	
	public UserManager(Context context, String login, String password){
		this.context = context;
		this.login = login;
		this.password = password;
		progressDialog = new TransparentProgressDialog(context, R.drawable.progress);
		localDataManager = new LocalDataManager(context);
		preferencesProvider = new PreferencesProvider(context);
	}
	
	public UserManager(Context context, String login, String password, String password2, String email, String userName){
		this.context = context;
		this.login = login;
		this.password = password;
		this.password2 = password2;
		this.email = email;
		this.userName = userName;
		progressDialog = new TransparentProgressDialog(context, R.drawable.progress);
		localDataManager = new LocalDataManager(context);
		preferencesProvider = new PreferencesProvider(context);
	}
	
	public int TryLoginUser(){
		int retVal = -1;
		try {
			retVal = new LoginUser().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	public int TryCreateUser(){
		int retVal = -1;
		try {
			retVal = new CreateUser().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	public enum LogonResult {
	    LOGGED,
	    USER_NOT_EXIST,
	    INCORRECT_PASWORD,
	    LOGIN_OR_PASSWORD_IS_EMPTY
	}
	
	
    /**
	 * Background Async Task to Load all route by making HTTP Request
	 * */
	class LoginUser extends AsyncTask<String, Integer, Integer> {
	
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setTitle("Logowanie do systemu...");
			progressDialog.show();
		}
	
		/**
		 * getting All routes from url
		 * */
		protected Integer doInBackground(String... args) {
			return localDataManager.TryLogonUser(login, DataTools.md5Converter(password));
		}
	
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(Integer loginResult) {
			switch (loginResult) {
			case Globals.LOGIN_RESULT.LOGGED:
				//Toast.makeText(context, "Zalogowano.", Toast.LENGTH_LONG).show();
				
				break;
			case Globals.LOGIN_RESULT.USER_NOT_EXIST:
				Toast.makeText(context, "U¿ytkownik nie istnieje!", Toast.LENGTH_LONG).show();
				break;
			case Globals.LOGIN_RESULT.INCORRECT_PASWORD:
				Toast.makeText(context, "Nieprawid³owe has³o!", Toast.LENGTH_LONG).show();
				break;
			case Globals.LOGIN_RESULT.LOGIN_OR_PASSWORD_IS_EMPTY:
				Toast.makeText(context, "Login lub/i has³o jest puste!", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
			progressDialog.dismiss();
	
		}
	}
	
	/**
	 * Background Async Task to Load all route by making HTTP Request
	 * */
	class CreateUser extends AsyncTask<String, Integer, Integer> {
	
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setTitle("Rejestracja nowego u¿ytkownika...");
			progressDialog.show();
		}
	
		/**
		 * getting All routes from url
		 * */
		protected Integer doInBackground(String... args) {
			return localDataManager.TryCreateUser(login, password, password2, email, userName);
		}
	
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(Integer loginResult) {
			switch (loginResult) {
			case Globals.CREATE_USER_RESULT.CREATED:
				Toast.makeText(context, "Stworzono u¿ytkownika.", Toast.LENGTH_LONG).show();
				break;
			case Globals.CREATE_USER_RESULT.USER_ALREADY_EXISTS:
				Toast.makeText(context, "Taki u¿ytkownik ju¿ istnieje!", Toast.LENGTH_LONG).show();
				break;
			case Globals.CREATE_USER_RESULT.INCORRECT_PASSWORD:
				Toast.makeText(context, "Nieprawid³owe has³o!", Toast.LENGTH_LONG).show();
				break;
			case Globals.CREATE_USER_RESULT.EMAIL_INCORRECT:
				Toast.makeText(context, "E-mail jest niepoprawny!", Toast.LENGTH_LONG).show();
				break;
			case Globals.CREATE_USER_RESULT.LOGIN_IS_EMPTY:
				Toast.makeText(context, "Login jest pusty!", Toast.LENGTH_LONG).show();
				break;
			case Globals.CREATE_USER_RESULT.PASSWORD_IS_EMPTY:
				Toast.makeText(context, "Has³o jest puste!", Toast.LENGTH_LONG).show();
				break;
			case Globals.CREATE_USER_RESULT.EMAIL_IS_EMPTY:
				Toast.makeText(context, "E-mail jest pusty!", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
			progressDialog.dismiss();
	
		}		
	}
}
