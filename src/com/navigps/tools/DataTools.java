package com.navigps.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class DataTools {
	public static String RoundTo(String value, String symbol, int places){
    	if(value.contains(symbol)){

	    	String[] splitted = value.split("\\"+symbol);
	    	if(splitted[1].length() > places){
	    		return splitted[0] + "." + splitted[1].substring(0,places);
	    	}
	    	else{
	    		return value;
	    	}
    	}
    	else{
    		throw new IllegalArgumentException("String " + value + " does not contain " + symbol);
    	}
    }
	
	public static String WhatLongitude(String longitude)
    {
    	double tempLongitude = Double.parseDouble(longitude);
    	if(tempLongitude > 0.0)
    	{
    		return longitude+ " E ";
    	}
    	else
    	{
    		return String.valueOf(-tempLongitude)+ " W ";
    	}
    }
	
    public static String WhatLatitude(String latitude)
    {
    	double tempLatitude = Double.parseDouble(latitude);
    	if(tempLatitude > 0.0)
    	{
    		return latitude+ " N ";
    	}
    	else
    	{
    		return String.valueOf(-tempLatitude)+ " S ";
    	}
    }
    
    public String AverageVelocity(String time, String distance) {
    	double hour = Double.parseDouble(time.substring(0, 2))+Double.parseDouble(time.substring(3, 5))/60.0+Double.parseDouble(time.substring(6, 8))/3600.0;
		Log.d("TIME", String.valueOf(hour));
    	double dist = Double.parseDouble(distance);
    	Log.d("DIST", String.valueOf(dist));
    	double value = 0.0;
    	Log.d("DIST", String.valueOf(value));
    	if (dist>0.0&&hour>0.0) {
			value = dist/hour;
		}
    	Log.d("DIST2", String.valueOf(value));
    	return String.valueOf(value);
    			
	}
    
    public static PolylineOptions getPolyline(LatLng point1, LatLng point2, int color){
    	return (new PolylineOptions())
        .add(point1, point2)
        .width(5)
        .color(color)
        .geodesic(true);
    }
    
    public static final String md5Converter(final String s) {
	    final String MD5 = "MD5";
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance(MD5);
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuilder hexString = new StringBuilder();
	        for (byte aMessageDigest : messageDigest) {
	            String h = Integer.toHexString(0xFF & aMessageDigest);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
    
    public static boolean isEmailValid(CharSequence email) {
		   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
}
