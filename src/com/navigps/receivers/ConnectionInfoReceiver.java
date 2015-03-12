package com.navigps.receivers;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionInfoReceiver {
	
	private ConnectivityManager connectivityManager = null;
	private Context _context;
	public ConnectionInfoReceiver(Context context){
		_context = context;
		connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public boolean WifiAvailable()
	{
		return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
	}
	
	public boolean WifiConnected()
	{
		return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}
	
	public boolean MobileAvailable()
	{
		return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
	}
	
	public boolean MobileConnected()
	{
		return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
	}
}
