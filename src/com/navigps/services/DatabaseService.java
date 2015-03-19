package com.navigps.services;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseService {
    public SQLiteDatabase db;
    private Context mContext;
    private DBHelper dbHelper;
    private static final String DEBUG_TAG = "DatabaseHelper";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "navigps.db";
    
    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String OPTIONS_STRING = "TEXT NOT NULL";
    public static final String OPTIONS_INT = "INTEGER NOT NULL";
    
    public static final String TABLE_USERS = "Users";
    public static final String USER_ID = "user_id";
    public static final String USER_LOGIN = "login";
    public static final String USER_NAME = "name";
    public static final String USER_PASSWORD = "password";
	public static final String USER_IS_ADMIN = "is_admin";
	public static final String USER_LAST_USER_ROUTE = "last_route_id";

    public static final String TABLE_LOCATIONS = "Locations";
    public static final String LOCATION_LONGITUDE = "longitude";
    public static final String LOCATION_LATITUDE = "latitude";
    public static final String LOCATION_ACCURACY = "accuracy";
    public static final String LOCATION_DATE = "date";
    public static final String LOCATION_ALTITUDE = "altitude";
    public static final String LOCATION_VELOCITY = "velocity";
    public static final String LOCATION_USER_ID = "user_id";
    public static final String LOCATION_ROUTE_ID = "user_route_id";
    public static final String LOCATION_REQUEST_DEF = "request_defined";
    
    public static final String TABLE_TRACE = "Trace";
    public static final String TRACE_RECID = "recId";
    public static final String TRACE_NAME = "name";
    public static final String TRACE_START_NAME = "start_name";
    public static final String TRACE_END_NAME = "end_name";
    public static final String TRACE_DISTANCE = "distance";
    public static final String TRACE_DESIGNATION = "designation";
    
    public static final String TABLE_USER_ROUTE = "UserRoute";
    public static final String USER_ROUTE_NAME = "route_name";
    public static final String USER_ROUTE_USER_ID = "user_id";
    public static final String USER_ROUTE_DISTANCE = "distance";
    public static final String USER_ROUTE_LONGITUDE = "longitude";
    public static final String USER_ROUTE_LATITUDE = "latitude";
    
    public static final String TABLE_DEFINED_TRACE = "DefinedTrace";
    public static final String DEFINED_TRACE_TRACE_ID = "trace_id";    
    public static final String DEFINED_TRACE_LONGITUDE = "longitude";
    public static final String DEFINED_TRACE_LATITUDE = "latitude";
    
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "( "
            + KEY_ID + " " + ID_OPTIONS 
            + ", " + USER_ID + " " + OPTIONS_STRING
            + ", " + USER_LOGIN + " " + OPTIONS_STRING
            + ", " + USER_NAME + " " + OPTIONS_STRING
            + ", " + USER_PASSWORD + " " + OPTIONS_STRING
            + ", " + USER_IS_ADMIN + " " + OPTIONS_STRING
            + ", " + USER_LAST_USER_ROUTE + " " + OPTIONS_STRING
            + ");";
    
    private static final String DROP_TABLE_USERS = "DROP TABLE IF EXISTS " + TABLE_USERS;
    
    private static final String CREATE_TABLE_LOCATIONS = "CREATE TABLE " + TABLE_LOCATIONS + "( "
            + KEY_ID + " " + ID_OPTIONS
            + ", " + LOCATION_LONGITUDE + " " + OPTIONS_STRING
            + ", " + LOCATION_LATITUDE + " " + OPTIONS_STRING
            + ", " + LOCATION_ACCURACY + " " + OPTIONS_STRING
            + ", " + LOCATION_ALTITUDE + " " + OPTIONS_STRING
            + ", " + LOCATION_VELOCITY + " " + OPTIONS_STRING
            + ", " + LOCATION_DATE + " " + OPTIONS_STRING
            + ", " + LOCATION_USER_ID + " " + OPTIONS_STRING
            + ", " + LOCATION_ROUTE_ID + " " + OPTIONS_STRING
            + ", " + LOCATION_REQUEST_DEF + " " + OPTIONS_STRING
            + ");";
    
    private static final String DROP_TABLE_LOCATIONS = "DROP TABLE IF EXISTS " + TABLE_LOCATIONS;
    
    private static final String CREATE_TABLE_TRACE = "CREATE TABLE " + TABLE_TRACE + "( "
            + KEY_ID + " " + ID_OPTIONS
            + ", " + TRACE_RECID + " " + OPTIONS_STRING
            + ", " + TRACE_NAME + " " + OPTIONS_STRING
            + ", " + TRACE_START_NAME + " " + OPTIONS_STRING
            + ", " + TRACE_END_NAME + " " + OPTIONS_STRING
            + ", " + TRACE_DISTANCE + " " + OPTIONS_STRING
            + ", " + TRACE_DESIGNATION + " " + OPTIONS_STRING
            + ");";
    
    private static final String DROP_TABLE_TRACE = "DROP TABLE IF EXISTS " + TABLE_TRACE;
    
    private static final String CREATE_TABLE_USER_ROUTE = "CREATE TABLE " + TABLE_USER_ROUTE + "( "
            + KEY_ID + " " + ID_OPTIONS
            + ", " + USER_ROUTE_NAME + " " + OPTIONS_STRING
            + ", " + USER_ROUTE_USER_ID + " " + OPTIONS_STRING
            + ", " + USER_ROUTE_DISTANCE + " " + OPTIONS_STRING
            + ", " + USER_ROUTE_LONGITUDE + " " + OPTIONS_STRING
            + ", " + USER_ROUTE_LATITUDE + " " + OPTIONS_STRING
            + ");";
    
    private static final String DROP_TABLE_USER_ROUTE = "DROP TABLE IF EXISTS " + TABLE_USER_ROUTE;
    
    private static final String CREATE_TABLE_DEFINED_TRACE = "CREATE TABLE " + TABLE_DEFINED_TRACE + "( "
            + KEY_ID + " " + ID_OPTIONS
            + ", " + DEFINED_TRACE_TRACE_ID + " " + OPTIONS_STRING
            + ", " + DEFINED_TRACE_LONGITUDE + " " + OPTIONS_STRING
            + ", " + DEFINED_TRACE_LATITUDE + " " + OPTIONS_STRING
            + ");";
    
    private static final String DROP_TABLE_DEFINED_TRACE = "DROP TABLE IF EXISTS " + TABLE_DEFINED_TRACE;
    
    public DatabaseService(Context context) {
        mContext = context;
    }

    public void open() {
        dbHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        dbHelper.close();
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	db.execSQL(CREATE_TABLE_USERS);
        	db.execSQL(CREATE_TABLE_LOCATIONS);
        	db.execSQL(CREATE_TABLE_TRACE);
        	db.execSQL(CREATE_TABLE_USER_ROUTE);
        	db.execSQL(CREATE_TABLE_DEFINED_TRACE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	db.execSQL(DROP_TABLE_USERS);
        	db.execSQL(DROP_TABLE_LOCATIONS);
        	db.execSQL(DROP_TABLE_TRACE);
        	db.execSQL(DROP_TABLE_USER_ROUTE);
        	db.execSQL(DROP_TABLE_DEFINED_TRACE);
            onCreate(db);
        }

    }
}
