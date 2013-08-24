package com.example.logowanie.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.logowanie.models.MyLocation;
import com.example.logowanie.services.DatabaseService;
import com.example.logowanie.services.LocationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barti on 24.08.13.
 */
public class LocalLocationProvider implements LocationService {
    private DatabaseService databaseService;
    public LocalLocationProvider(Context context)
    {
        databaseService = new DatabaseService(context);
    }
    @Override
    public void saveLocation(MyLocation location) {
        databaseService.open();
        databaseService.db.insert(DatabaseService.DB_TABLE_LOCATIONS,"NULL",createContentValues(location));
        databaseService.close();
    }
    public List<MyLocation> getAllLocations()
    {
        List<MyLocation> locations = new ArrayList<MyLocation>();
        databaseService.open();
        Cursor c = databaseService.db.rawQuery("SELECT * FROM " + DatabaseService.DB_TABLE_LOCATIONS + ";",null);
        if(c!=null && c.moveToFirst())
        {
            do
            {
                MyLocation location = new MyLocation();
                location.id  = c.getInt(0);
                location.longitude = c.getString(1);
                location.latitude = c.getString(2);
                location.accuracy = c.getString(3);
                location.altitude = c.getString(4);
                location.velocity = c.getString(5);
                location.date = c.getString(6);
                location.userId = c.getInt(7);

                locations.add(location);
            }
            while (c.moveToNext());
        }
        c.close();
        databaseService.close();
        return locations;

    }
    private ContentValues createContentValues(MyLocation location)
    {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseService.KEY_ID,location.id);
        cv.put(DatabaseService.LOCATION_LONGITUDE,location.longitude);
        cv.put(DatabaseService.LOCATION_LATITUDE,location.latitude);
        cv.put(DatabaseService.LOCATION_ACCURACY,location.accuracy);
        cv.put(DatabaseService.LOCATION_ALTITUDE,location.altitude);
        cv.put(DatabaseService.LOCATION_DATE,location.date);
        cv.put(DatabaseService.LOCATION_VELOCITY,location.velocity);
        cv.put(DatabaseService.LOCATION_USER_ID,location.userId);
        return cv;
    }

}
