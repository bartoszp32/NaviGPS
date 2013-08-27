package com.navigps.managers;

import com.navigps.services.LocationService;


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
