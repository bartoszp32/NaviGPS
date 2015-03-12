package com.navigps.providers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.navigps.models.DefinedRoute;
import com.navigps.models.Trace;
import com.navigps.models.User;
import com.navigps.models.UserRoute;
import com.navigps.services.DatabaseService;

public class LocalDataProvider {
	private DatabaseService databaseService;
	public LocalDataProvider(Context context){
		databaseService = new DatabaseService(context);
	}
	
	public void cleanUserTablesToStartupApp(){
		try {
			databaseService.open();
			databaseService.db.delete(DatabaseService.TABLE_TRACE, null, null);
		} catch (Exception e) {
			Log.d("LocalDataProvider.cleanUserTablesToStartupApp()", e.toString());
		} finally {
			databaseService.close();
		}
	}
	
	public boolean clearTable(String tableName){		
		int result = 0; 
		try {
			databaseService.open();
			result = databaseService.db.delete(tableName, null, null);
		} catch (Exception e) {
			Log.d("LocalDataProvider.clearTable()", e.toString());
		} finally {
			databaseService.close();
		}
		return result > 0;
	}
	
	public boolean clearTable(String tableName, String where){		
		int result = 0; 
		try {
			databaseService.open();
			result = databaseService.db.delete(tableName, where, null);
		} catch (Exception e) {
			Log.d("LocalDataProvider.clearTable()", e.toString());
		} finally {
			databaseService.close();
		}
		return result > 0;
	}
	
	public boolean insertTraceToDB(Trace trace) {
		long result = 0;
		try {
			databaseService.open();
			result = databaseService.db.insert(DatabaseService.TABLE_TRACE, null, insertContentValues(trace));
		} catch (Exception e) {
			Log.d("LocalDataProvider.insertTraceToDB()", e.toString());
		} finally {
			databaseService.close();
		}
        return result != -1;
    }
	
	public boolean insertDefinedRouteToDB(DefinedRoute definedRoute) {
		long result = 0;
		try {
			databaseService.open();
			result = databaseService.db.insert(DatabaseService.TABLE_DEFINED_TRACE, null, insertContentValues(definedRoute));
		} catch (Exception e) {
			Log.d("LocalDataProvider.insertDefinedRouteToDB()", e.toString());
		} finally {
			databaseService.close();
		}
        return result != -1;
    }
	
	public boolean insertUserToDB(User user) {
		long result = 0;
		try {
			databaseService.open();
			result = databaseService.db.insert(DatabaseService.TABLE_USERS, null, insertContentValues(user));
		} catch (Exception e) {
			Log.d("LocalDataProvider.insertUserToDB()", e.toString());
		} finally {
			databaseService.close();
		}
        return result != -1;
    }
	
	public boolean insertUserRouteToDB(UserRoute userRoute) {
		long result = 0;
		try {
			databaseService.open();
			result = databaseService.db.insert(DatabaseService.TABLE_USER_ROUTE, null, insertContentValues(userRoute));
		} catch (Exception e) {
			Log.d("LocalDataProvider.insertUserRouteToDB()", e.toString());
		} finally {
			databaseService.close();
		}
        return result != -1;
    }
	
	public List<Trace> getAllTraces() {
		
        List<Trace> traces = new ArrayList<Trace>();
        databaseService.open();
        Cursor c = databaseService.db.rawQuery("SELECT * FROM " + DatabaseService.TABLE_TRACE + ";",null);
        if(c!=null && c.moveToFirst())
        {
            do
            {
            	Trace trace = new Trace();
            	
            	trace.recId  = c.getString(1);
            	trace.name = c.getString(2);
            	trace.start_name = c.getString(3);
            	trace.end_name = c.getString(4);
            	trace.distance = c.getString(5);
            	trace.designation = c.getString(6);

            	traces.add(trace);
            }
            while (c.moveToNext());
        }
        c.close();
        databaseService.close();
        return traces;

    }
	
	public Trace getTrace(String routeId) {
		
		Trace trace = new Trace();
        databaseService.open();
        Cursor c = databaseService.db.rawQuery("SELECT * FROM " + DatabaseService.TABLE_TRACE + " WHERE " + 
    			DatabaseService.TRACE_RECID + " = ? ;", new String[] {routeId});
        if(c!=null && c.moveToFirst())
        {
        	trace.recId  = c.getString(1);
        	trace.name = c.getString(2);
        	trace.start_name = c.getString(3);
        	trace.end_name = c.getString(4);
        	trace.distance = c.getString(5);
        	trace.designation = c.getString(6);
        }
        c.close();
        databaseService.close();
        return trace;

    }
	
	public List<DefinedRoute> getRoute(String routeId) {
		
        List<DefinedRoute> definedRoute = new ArrayList<DefinedRoute>();
        databaseService.open();
        Cursor c = databaseService.db.rawQuery("SELECT * FROM " + DatabaseService.TABLE_DEFINED_TRACE +" WHERE " + 
    			DatabaseService.DEFINED_TRACE_TRACE_ID + " = ? ;", new String[] {routeId});
        if(c!=null && c.moveToFirst())
        {
            do
            {
            	DefinedRoute route = new DefinedRoute();
            	
            	route.setRecId(routeId);
            	route.setLatitude(c.getString(2));
            	route.setLongitude(c.getString(3));

            	definedRoute.add(route);
            }
            while (c.moveToNext());
        }
        c.close();
        databaseService.close();
        return definedRoute;

    }
	
	public List<UserRoute> getUserRoute(String userId) {
		
        List<UserRoute> userRoute = new ArrayList<UserRoute>();
        databaseService.open();
        Cursor c = databaseService.db.rawQuery("SELECT * FROM " + DatabaseService.TABLE_USER_ROUTE +" WHERE " + 
    			DatabaseService.USER_ROUTE_USER_ID + " = ? ;", new String[] {userId});
        if(c!=null && c.moveToFirst())
        {
            do
            {
            	UserRoute route = new UserRoute();
            	route.routeName = c.getString(1);
            	route.userId = c.getString(2);
            	route.distance = c.getString(3);
            	route.route.setRecId("-1");
            	route.route.setLatitude(c.getString(4));
            	route.route.setLongitude(c.getString(5));

            	userRoute.add(route);
            }
            while (c.moveToNext());
        }
        c.close();
        databaseService.close();
        return userRoute;

    }
	
	private ContentValues insertContentValues(Trace trace) {
		
        ContentValues cv = new ContentValues();

        cv.put(DatabaseService.TRACE_RECID, trace.recId);
        cv.put(DatabaseService.TRACE_NAME, trace.name);
        cv.put(DatabaseService.TRACE_START_NAME, trace.start_name);
        cv.put(DatabaseService.TRACE_END_NAME, trace.end_name);
        cv.put(DatabaseService.TRACE_DISTANCE, trace.distance);
        cv.put(DatabaseService.TRACE_DESIGNATION, trace.designation);
        
        return cv;
    }
	
	private ContentValues insertContentValues(DefinedRoute definedRoute) {
		
        ContentValues cv = new ContentValues();

        cv.put(DatabaseService.DEFINED_TRACE_TRACE_ID, definedRoute.getRecId());
        cv.put(DatabaseService.DEFINED_TRACE_LATITUDE, definedRoute.getLatitude());
        cv.put(DatabaseService.DEFINED_TRACE_LONGITUDE, definedRoute.getLongitude());
        
        return cv;
    }
	
	private ContentValues insertContentValues(User user) {
        ContentValues cv = new ContentValues();

        cv.put(DatabaseService.USER_ID, user.getUserId());
        cv.put(DatabaseService.USER_LOGIN, user.getUserLogin());
        cv.put(DatabaseService.USER_NAME, user.getUserName());
        cv.put(DatabaseService.USER_PASSWORD, user.getUserPassword());
        cv.put(DatabaseService.USER_IS_ADMIN, user.isAdmin() ? "Y" : "N");
        cv.put(DatabaseService.USER_LAST_USER_ROUTE, user.getUserLastRouteId());
        
        return cv;
    }
	
	private ContentValues insertContentValues(UserRoute userRoute) {
		
		ContentValues cv = new ContentValues();

        cv.put(DatabaseService.USER_ROUTE_NAME, userRoute.routeName);
        cv.put(DatabaseService.USER_ROUTE_USER_ID, userRoute.userId);
        cv.put(DatabaseService.USER_ROUTE_DISTANCE, userRoute.distance);
        cv.put(DatabaseService.USER_ROUTE_LATITUDE, userRoute.route.getLatitude());
        cv.put(DatabaseService.USER_ROUTE_LONGITUDE, userRoute.route.getLongitude());
        
        return cv;
	}


	public boolean existAnyRecords(String table) {
		int result = 0; 
		try {
			databaseService.open();
			Cursor c = databaseService.db.rawQuery("SELECT Count(1) FROM " + table + ";",null);
			if(c!=null && c.moveToFirst()){
				result = c.getInt(0);
	        }
		} catch (Exception e) {
			Log.d("LocalDataProvider.existAnyRecords()", e.toString());
		} finally {
			databaseService.close();
		}
		return result > 0;
	}
	
	public boolean existAnyRecords(String table, String where) {
		int result = 0; 
		try {
			databaseService.open();
			Cursor c = databaseService.db.rawQuery("SELECT Count(1) FROM " + table + " WHERE " + where + ";",null);
			if(c!=null && c.moveToFirst()){
				result = c.getInt(0);
	        }
		} catch (Exception e) {
			Log.d("LocalDataProvider.existAnyRecords()", e.toString());
		} finally {
			databaseService.close();
		}
		return result > 0;
	}

	public User getUser(String login) {
		User user = null;
        databaseService.open();
        Cursor c = databaseService.db.rawQuery("SELECT * FROM " + DatabaseService.TABLE_USERS + " WHERE " + 
    			DatabaseService.USER_NAME + " = ? ;", new String[] {login});
        if(c!=null && c.moveToFirst())
        {
        	user = new User();
        	user.setUserId(c.getInt(1));
        	user.setUserName(c.getString(2));
        	user.setUserPassword(c.getString(3));

        }
        c.close();
        databaseService.close();
        return user;
	}


}
