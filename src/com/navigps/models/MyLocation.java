package com.navigps.models;

import com.google.android.gms.maps.model.LatLng;

import android.os.Parcel;
import android.os.Parcelable;

public class MyLocation implements Parcelable{
    public static final String LOCATION_KEY = "location_key";

    public int id;
    public String longitude;
    public String latitude;
    public String altitude;
    public String accuracy;
    public String velocity;
    public String date;
    public int userId;

    public MyLocation()
    {

    }
    public MyLocation(Parcel parcel)
    {
        readFromParcel(parcel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(longitude);
             parcel.writeString(latitude);
             parcel.writeString(altitude);
             parcel.writeString(accuracy);
         parcel.writeString(velocity);
        parcel.writeString(date);

    }
    public LatLng getLatLng(){
    	return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }
    
    private void readFromParcel(Parcel parcel)
    {
        id = parcel.readInt();
        longitude = parcel.readString();
        latitude = parcel.readString();
        altitude = parcel.readString();
        accuracy = parcel.readString();
        velocity = parcel.readString();
        date = parcel.readString();
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    { 
    	public MyLocation createFromParcel(Parcel in){ 
    		return new MyLocation(in); 
    	}
        public MyLocation[] newArray(int size){ 
        	return new MyLocation[size]; 
        } 
    };
}
