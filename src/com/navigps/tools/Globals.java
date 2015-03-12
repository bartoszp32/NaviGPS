package com.navigps.tools;

public class Globals {
	public class DEFINED_ROUTE {
		public static final String TAG_SUCCESS = "success";
		public static final String TAG_ROUTES = "routes";
		public static final String TAG_ROUTE_ID = "route_id";
		public static final String TAG_NAME = "name";
		public static final String TAG_START_NAME = "start_name";
		public static final String TAG_END_NAME = "end_name";
		public static final String TAG_DISTANCE = "distance";
		public static final String TAG_DESIGNATION = "designation";
		public static final String TAG_DETAILS_ROUTE = "details_route";
		public static final String TAG_COLOR_ROUTE = "color_route";
	}
	
	public class DEFINED_ROUTE_MAP {
		public static final String TAG_SUCCESS = "success";
		public static final String TAG_MAP_COORDINATE = "map_coordinate";
		public static final String TAG_RECORD_ID = "rec_id";
		public static final String TAG_LATITUDE = "latitude";
		public static final String TAG_LONGITUDE = "longitude";
		public static final String TAG_ROUTE_ID = "route_id";
	}

	public class LOGIN_RESULT {
		public static final int LOGGED = 1;
		public static final int USER_NOT_EXIST = 2;
		public static final int INCORRECT_PASWORD = 3;
		public static final int LOGIN_OR_PASSWORD_IS_EMPTY = 4;
	}
	
	public class CREATE_USER_RESULT {
		public static final int CREATED = 1;
		public static final int USER_ALREADY_EXISTS = 2;
		public static final int INCORRECT_PASSWORD = 3;
		public static final int EMAIL_INCORRECT = 4;
		public static final int LOGIN_IS_EMPTY = 5;
		public static final int PASSWORD_IS_EMPTY = 6;
		public static final int EMAIL_IS_EMPTY = 7;
	}
}
