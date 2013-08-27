package com.navigps.providers;

import com.navigps.models.MyLocation;
import com.navigps.services.LocationService;

/**
 * Created by Barti on 24.08.13.
 */
public class OnlineLocationProvider implements LocationService {
    public OnlineLocationProvider() {
    }

    @Override
    public boolean saveLocation(MyLocation location) {
       ///////
       // PUT YOUR CODE HERE
       /////////
        return false;
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
