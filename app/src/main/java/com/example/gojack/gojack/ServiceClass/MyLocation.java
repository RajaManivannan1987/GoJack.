package com.example.gojack.gojack.ServiceClass;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by IM028 on 5/12/16.
 */
public class MyLocation {
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private LocationManager locationManager;
    private Location location;
    private Context context;


    public MyLocation(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        getLocation();
    }


    public LatLng getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            else if (location == null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

        }
        if (location != null)
            return new LatLng(location.getLatitude(), location.getLongitude());
        else
            return null;
    }


}
