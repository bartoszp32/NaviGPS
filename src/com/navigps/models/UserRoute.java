package com.navigps.models;

public class UserRoute {
	public String routeName;
	public String userId;
	public DefinedRoute route;
	public String distance;
	
	public UserRoute(){	
		route = new DefinedRoute();
	}
	
	public UserRoute(String routeName, String userId, DefinedRoute route, String distance){	
		this.routeName = routeName;
		this.userId = userId;
		this.route = route;
		this.distance = distance;
	}
}
