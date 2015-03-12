package com.navigps.services;

import com.navigps.models.MyLocation;

public interface LocationService {
    public boolean saveLocation(MyLocation location);
    public boolean updateLocation(MyLocation location);
    public boolean deleteLocation(MyLocation location);

}
