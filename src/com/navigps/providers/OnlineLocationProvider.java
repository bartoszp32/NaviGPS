package com.navigps.providers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.navigps.models.MyLocation;
import com.navigps.services.LocationService;


public class OnlineLocationProvider implements LocationService {
	JSONParser jsonParser = new JSONParser();
 	private static String url_add_data = "http://www.navigps.cba.pl/menu/add_data.php";
 	private static final String TAG_SUCCESS = "success";
 	
	public OnlineLocationProvider() {
    }

    @Override
    public boolean saveLocation(MyLocation location) {
    	
    	// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User_Id", String.valueOf(location.userId)));
		params.add(new BasicNameValuePair("Route_Id", String.valueOf(location.userRouteId)));
		params.add(new BasicNameValuePair("Longitude", location.longitude));
		params.add(new BasicNameValuePair("Latitude", location.latitude));
		params.add(new BasicNameValuePair("Altitude", location.altitude));
		params.add(new BasicNameValuePair("Velocity", location.velocity));
		params.add(new BasicNameValuePair("Accuracy", location.accuracy));
		params.add(new BasicNameValuePair("Time", location.date));
		params.add(new BasicNameValuePair("Request_Defined", location.reuestDefined));
		
		Log.d("DANE", params.toString());
		
		JSONObject json = jsonParser.makeHttpRequest(url_add_data, "POST", params);
		
		Log.d("Create Response", json.toString());

		try {
			int success = json.getInt(TAG_SUCCESS);

			if (success != 1) {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return true;
    }

    @Override
    public boolean updateLocation(MyLocation location) {
        return false;
    }

    @Override
    public boolean deleteLocation(MyLocation location) {
        return false;
    }
}
