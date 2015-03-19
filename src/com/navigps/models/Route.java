package com.navigps.models;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Route {
	private List<LatLng> points;
	private String distance;
	private String duration;
	
	public Route() {
	}
	
	public void setPoints(List<LatLng> points) {
		this.points = points;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public List<LatLng> getPoints() {
		return points;
	}
	public String getDistance() {
		return distance;
	}
	public String getDuration() {
		return duration;
	}
}
