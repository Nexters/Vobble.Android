package com.nexters.vobble.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationHelper {

    LocationManager mLocationManager;

    public LocationHelper(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public Location getCurrentLocation() {
        String locationProvider = mLocationManager.getBestProvider(new Criteria(), true);
        return mLocationManager.getLastKnownLocation(locationProvider);
    }

    public boolean isGPSEnabled() {
        return mLocationManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
    }
}
