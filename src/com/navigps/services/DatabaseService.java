package com.navigps.services;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Barti on 24.08.13.
 */
public class DatabaseService {
    public SQLiteDatabase db;
    private Context mContext;
    private DBHelper dbHelper;
    private static final String DEBUG_TAG = "DatabaseHelper";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "navigps.db";
    public static final String DB_TABLE_LOCATIONS = "Locations";
    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String OPTIONS_STRING = "TEXT NOT NULL";
    public static final String OPTIONS_INT = "INTEGER NOT NULL";

    public static final String LOCATION_LONGITUDE = "longitude";
    public static final String LOCATION_LATITUDE = "latitude";
    public static final String LOCATION_ACCURACY = "accuracy";
    public static final String LOCATION_DATE = "date";
    public static final String LOCATION_ALTITUDE = "long";
    public static final String LOCATION_VELOCITY = "lat";
    public static final String LOCATION_USER_ID = "acc";

    private static final String DB_CREATE_LOCATIONS = "CREATE TABLE " + DB_TABLE_LOCATIONS + "( "
            + KEY_ID + " " + ID_OPTIONS
            + ", " + LOCATION_LONGITUDE + " " + OPTIONS_STRING
            + ", " + LOCATION_LATITUDE + " " + OPTIONS_STRING
            + ", " + LOCATION_ACCURACY + " " + OPTIONS_STRING
            + ", " + LOCATION_ALTITUDE + " " + OPTIONS_STRING
            + ", " + LOCATION_VELOCITY + " " + OPTIONS_STRING
            + ", " + LOCATION_DATE + " " + OPTIONS_STRING
            + ", " + LOCATION_USER_ID + " " + OPTIONS_STRING
            + ");";
    private static final String DROP_TABLE_LOCATIONS = "DROP TABLE IF EXISTS " + DB_TABLE_LOCATIONS;
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
          db.execSQL(DB_CREATE_LOCATIONS);



        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

           db.execSQL(DROP_TABLE_LOCATIONS);

            onCreate(db);
        }

    }
}
