package com.navigps.threads;

import android.content.Context;
import android.os.AsyncTask;

import com.navigps.models.MyLocation;
import com.navigps.providers.LocalLocationProvider;
import com.navigps.providers.OnlineLocationProvider;

import java.util.List;


public class FromLocalToOnlineSenderThread extends AsyncTask<Void,Void,Void>{
    private OnlineLocationProvider onlineLocationProvider;
    private LocalLocationProvider localLocationProvider;

    public FromLocalToOnlineSenderThread(Context context) {
       localLocationProvider = new LocalLocationProvider(context);
       onlineLocationProvider = new OnlineLocationProvider();
    }


    @Override
    protected Void doInBackground(Void... voids) {
        List<MyLocation> locationList = localLocationProvider.getAllLocations();
        for (MyLocation location:locationList)
        {
           if (onlineLocationProvider.saveLocation(location))
           {
               localLocationProvider.deleteLocation(location);
           }
        }


        return null;
    }
}
