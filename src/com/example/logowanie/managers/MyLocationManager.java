package com.example.logowanie.managers;

import com.example.logowanie.services.LocationService;

/**
 * Created by Barti on 24.08.13.
 */
public class MyLocationManager {
    private static MyLocationManager instance = new MyLocationManager();;
    private LocationService service;
    public static MyLocationManager getInstance()
    {

        return instance;
    }
    public LocationService getService()
    {
        return service;
    }
    public void setService(LocationService service)
    {
        this.service = service;
    }

}
