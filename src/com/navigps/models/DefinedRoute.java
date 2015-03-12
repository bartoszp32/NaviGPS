package com.navigps.models;

import com.google.android.gms.maps.model.LatLng;

public class DefinedRoute {
	private String recId;
	private String longitude;
	private String latitude;

    public DefinedRoute(){
    }
    
    public DefinedRoute(String recId, String latitude, String longitude){
    	this.recId = recId;
    	this.latitude = latitude;
    	this.longitude = longitude;
    }
    
    public void setRecId(String recId){
    	this.recId = recId;
    }
    
    public void setLongitude(String longitude){
    	this.longitude = longitude;
    }
    
    public void setLatitude(String latitude){
    	this.latitude = latitude;
    }
    
    public String getRecId(){
    	return recId;
    }
    
    public String getLongitude(){
    	return longitude;
    }
    
    public String getLatitude(){
    	return latitude;
    }
    
    public double getLongitudeDouble(){
    	return Double.parseDouble(longitude);
    }
    
    public double getLatitudeDouble(){
    	return Double.parseDouble(latitude);
    }
    
    public LatLng convertToLatLng(){
    	return new LatLng(getLatitudeDouble(), getLongitudeDouble());
    }
}
