package com.navigps.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.content.Context;
import android.util.Log;

import com.navigps.models.DefinedRoute;
import com.navigps.models.Trace;
import com.navigps.models.User;
import com.navigps.models.UserRoute;
import com.navigps.providers.JSONParser;
import com.navigps.providers.LocalDataProvider;
import com.navigps.services.DatabaseService;
import com.navigps.tools.DataTools;
import com.navigps.tools.Globals;

public class LocalDataManager {
	private Context context;
	private LocalDataProvider localDataProvider;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_USERS = "users";
	private static final String TAG_USER_ID = "user_id";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_PASSWORD = "password";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_USER_NAME = "user_name";
	private static final String TAG_IS_ADMIN = "is_admin";
	private static final String TAG_LAST_USER_ROUTE = "last_user_route";
	
	private JSONParser jParser = new JSONParser();
	private JSONArray jArray = null;
	private static String url_all_route = "http://www.navigps.cba.pl/menu/get_all_routes.php";
	private static String url_route_details = "http://www.navigps.cba.pl/menu/get_route_details.php";
	private static String url_check_user = "http://www.navigps.cba.pl/menu/check_user.php";
	private static String url_create_user = "http://www.navigps.cba.pl/menu/create_user.php";
	
	public LocalDataManager(Context context) {
		this.context = context;
		localDataProvider = new LocalDataProvider(this.context);
		definedTrace = new HashMap<String, List<DefinedRoute>>();
		trace = new HashMap<String, Trace>();
	}
	private Map<String, Trace> trace;
	public Trace GetTrace(String routeId){
		if (!trace.containsKey(routeId)){
			trace.put(routeId, localDataProvider.getTrace(routeId));
		}
		return trace.get(routeId);
	}
	
	private List<Trace> allDefinedTrace;
	public List<Trace> AllDefinedTrace(){
		if (allDefinedTrace == null){
			ReadAllDefinedTrace();
			allDefinedTrace = localDataProvider.getAllTraces();
		}
		return allDefinedTrace;
	}
	
	private void ReadAllDefinedTrace(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jParser.makeHttpRequest(url_all_route, "GET", params);

		try {
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) {
				jArray = json.getJSONArray(Globals.DEFINED_ROUTE.TAG_ROUTES);
				Trace trace;
				JSONObject c;
				for (int i = 0; i < jArray.length(); i++) {
					c = jArray.getJSONObject(i);
					trace = new Trace();

					trace.recId = c.getString(Globals.DEFINED_ROUTE.TAG_ROUTE_ID);
					trace.name = c.getString(Globals.DEFINED_ROUTE.TAG_NAME);
					trace.start_name = c.getString(Globals.DEFINED_ROUTE.TAG_START_NAME);
					trace.end_name = c.getString(Globals.DEFINED_ROUTE.TAG_END_NAME);
					trace.distance = c.getString(Globals.DEFINED_ROUTE.TAG_DISTANCE);
					trace.designation = c.getString(Globals.DEFINED_ROUTE.TAG_DESIGNATION);

					localDataProvider.insertTraceToDB(trace);
				}
			} else {
				Log.d("GetAllDefinedTrace()", "Problem reading data from database.");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private Map<String, List<DefinedRoute>> definedTrace;
	public List<DefinedRoute> DefinedTrace(String routeId){
		if (!definedTrace.containsKey(routeId)){
			definedTrace.put(routeId, ReadDefinedTrace(routeId));
		}
		return definedTrace.get(routeId);
	}
	
	private List<DefinedRoute> ReadDefinedTrace(String routeId){
		List<DefinedRoute> definedTrace = new ArrayList<DefinedRoute>(); 
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("mapId", routeId));
		JSONObject json = jParser.makeHttpRequest(url_route_details, "GET", params);

		try {
			int success = json.getInt(TAG_SUCCESS);
			if (success == 1) {

				jArray = json.getJSONArray(Globals.DEFINED_ROUTE_MAP.TAG_MAP_COORDINATE);
				DefinedRoute definedRoute;
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject c = jArray.getJSONObject(i);

					String id = c.getString(Globals.DEFINED_ROUTE_MAP.TAG_RECORD_ID);
					String latitude = c.getString(Globals.DEFINED_ROUTE_MAP.TAG_LATITUDE);
					String longitude = c.getString(Globals.DEFINED_ROUTE_MAP.TAG_LONGITUDE);
					
					definedRoute = new DefinedRoute();
					definedRoute.setRecId(id);
					definedRoute.setLatitude(latitude);
					definedRoute.setLongitude(longitude);
					
					definedTrace.add(definedRoute);
				}
			} else {
				Log.d("ReadDefinedTrace()", "Problem reading data from database.");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return definedTrace;
	}

	public int TryLogonUser(String login, String md5Password) {
		int retVal = 0;
		if(login.isEmpty()||md5Password.isEmpty()) {
			retVal = Globals.LOGIN_RESULT.LOGIN_OR_PASSWORD_IS_EMPTY;
		} else {
			User user;// = localDataProvider.getUser(login);
			//if (user != null && user.getUserPassword().equals(md5Password)){
			//	retVal = Globals.LOGIN_RESULT.LOGGED;
			//} else 
				{
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("login", login));
				params.add(new BasicNameValuePair("pass", md5Password));
				JSONObject json = jParser.makeHttpRequest(url_check_user, "GET", params);
				
				try {
					retVal = json.getInt(TAG_SUCCESS);
					if (retVal == Globals.LOGIN_RESULT.LOGGED) {
						localDataProvider.clearTable(DatabaseService.TABLE_USERS);
						jArray = json.getJSONArray(TAG_USERS);
						
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject c = jArray.getJSONObject(i);

							int idDB = c.getInt(TAG_USER_ID);
							String loginDB = c.getString(TAG_LOGIN);
							String passwordDB = c.getString(TAG_PASSWORD);
							String emailDB = c.getString(TAG_EMAIL);
							String userNameDB = c.getString(TAG_USER_NAME);
							String isAdminDB = c.getString(TAG_IS_ADMIN);
							int lastUserRouteDB = c.getInt(TAG_LAST_USER_ROUTE);
							
							user = new User();
							user.setUserId(idDB);
							user.setUserLogin(loginDB);
							user.setUserName(userNameDB);
							user.setUserPassword(passwordDB);
							user.setUserLastRouteId(lastUserRouteDB);
							user.setAdmin("Y".equals(isAdminDB));
							
							localDataProvider.insertUserToDB(user);
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		return retVal;
	}

	public int TryCreateUser(String login, String password, String password2, String email, String userName) {
		int retVal = 0;
		if (login.isEmpty())
			retVal = Globals.CREATE_USER_RESULT.LOGIN_IS_EMPTY;
		else if (password.isEmpty() || password2.isEmpty())
			retVal = Globals.CREATE_USER_RESULT.PASSWORD_IS_EMPTY;
		else if (email.isEmpty())
			retVal = Globals.CREATE_USER_RESULT.EMAIL_IS_EMPTY;
		else if (!password.equals(password2))
			retVal = Globals.CREATE_USER_RESULT.INCORRECT_PASSWORD;
		else if (!DataTools.isEmailValid(email))
			retVal = Globals.CREATE_USER_RESULT.EMAIL_INCORRECT;
		else {
			String passwordMD5 = DataTools.md5Converter(password);
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("login", login));
			params.add(new BasicNameValuePair("pass", passwordMD5));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("userName", userName.isEmpty() ? "" : userName));
			JSONObject json = jParser.makeHttpRequest(url_create_user, "GET", params);
			
			try {
				retVal = json.getInt(TAG_SUCCESS);				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return retVal;
	}
	
	public boolean IsUserRoute(String userId) {
		return localDataProvider.existAnyRecords(DatabaseService.TABLE_USER_ROUTE, DatabaseService.USER_ROUTE_USER_ID + " = " + userId);
	}
		
	public List<UserRoute> GetUserRoute(String userId){
		return localDataProvider.getUserRoute(userId);
	}

	public User GetUser(String login) {
		return localDataProvider.getUser(login);
	}
	
	public void cleanUserTablesToStartupApp() {
		localDataProvider.cleanUserTablesToStartupApp();
	}
	
	public void InsertUserRoutePoint(UserRoute userRoute) { 
		localDataProvider.insertUserRouteToDB(userRoute);
	}
	
	public void ClearUserRouteTable() {
		localDataProvider.clearTable(DatabaseService.TABLE_USER_ROUTE);
	}
}