package com.nexters.vobble.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHelper {

    LocationManager mLocationManager;

    public LocationHelper(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mDefaultLocationListener);
    }

    LocationListener mDefaultLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public Location getCurrentLocation() {
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        return mLocationManager.getLastKnownLocation(locationProvider);
    }

    public Location getDefaultLocation() {
        // 강남역
        Location location = new Location("");
        location.setLatitude(37.4979);
        location.setLongitude(127.0276);
        return location;
    }

    public void destroy() {
        mLocationManager.removeUpdates(mDefaultLocationListener);
    }
}
