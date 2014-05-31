package com.example.carpool.util;

public class URLValue {

	public static final String rootURL = "http://cray.hbg.psu.edu:6540/carpool";
	
	/** crud for user **/
	public static final String SIGN_IN = rootURL + "/authenticateuser";

	public static final String ADD_USER = rootURL + "/addUser";

	public static final String USER_INFO = rootURL + "/user";

	public static final String UPDATE_USER = rootURL + "/updateUser"; 

	/** crud for user ends here**/
	public static final String USER_RIDES = rootURL + "/user/rides";

	public static final String ADD_RIDE = rootURL + "/addRide";

	public static final String SEARCH_RIDES = rootURL + "/search";

	public static final String FETCH_RIDE = rootURL + "/ride/id";
	
	public static final String UPDATE_RIDE = rootURL + "/updateRide";
	
	/** URL to fetch city list **/
	public static final String ALL_CITIES = rootURL + "/cities";
}