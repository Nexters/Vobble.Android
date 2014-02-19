package com.nexters.vobble.listener;

import android.location.Location;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;

public class CustomOnMapStateChangeListener implements NMapView.OnMapStateChangeListener{

    private NMapController mMapController;
    private double initLatitude;
    private double initLongitude;

    public CustomOnMapStateChangeListener(NMapController mapController, Location initLocation) {
        this(mapController, initLocation.getLatitude(), initLocation.getLongitude());
    }

    public CustomOnMapStateChangeListener(NMapController mapController, double latitude, double longitude) {
        this.mMapController = mapController;
        this.initLatitude = latitude;
        this.initLongitude = longitude;
    }

    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        if (nMapError == null) {
            mMapController.setMapCenter(new NGeoPoint(initLongitude, initLatitude), 10);
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i2) {

    }
}
