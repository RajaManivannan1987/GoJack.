package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Common.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.ServiceClass.SearchLocation;
import com.example.gojack.gojack.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    boolean gps_enabled = false;
    boolean network_enabled = false;
    private final int MY_LOCATION = 1;
    private GoogleMap mMap;
    private SearchLocation searchLocation;
    private View.OnClickListener currentLocation;
    private FloatingActionButton locationFloatingActionButton;
    ImageView markerImage;
    Marker marker;
    GoogleApiClient mGoogleApiClient;
    MarkerOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        initMap();
    }

    private void initMap() {
        markerImage = (ImageView) findViewById(R.id.dashboardActivityMapMarkerImageView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(false);
            mMap.setIndoorEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);

           goToLocationZoom(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, 17);
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    LatLng ll = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
                    String address = CommonMethods.getMarkerMovedAddress(MapActivity.this, ll);
                    CommonMethods.toast(MapActivity.this, "" + address);
                }
            });
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder gc = new Geocoder(MapActivity.this);
                    LatLng ll = marker.getPosition();
                    List<Address> addresses = null;
                    try {
                        addresses = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address add = addresses.get(0);
                    marker.setTitle(add.getLocality());
                }
            });
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        markerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng ll = mMap.getCameraPosition().target;
                String address = CommonMethods.getMarkerMovedAddress(MapActivity.this, ll);
                CommonMethods.toast(MapActivity.this, "" + address);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        enableMyLocation();
    }


    private boolean enableMyLocation() {
        if (CommonMethods.checkLocationPermission(MapActivity.this)) {
        } else {
            AppControler.instanceLocation(MapActivity.this);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    CommonMethods.toast(MapActivity.this, "My Location permission denied");
                    finish();
                }
                break;
        }

    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            setMarker(ll, 17);
//          goToLocationZoom(location.getLatitude(), location.getLongitude(), 17);
        }
    }

    private void goToLocationZoom(double lat, double lang, float zoom) {
        LatLng ll = new LatLng(lat, lang);
       /* CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);*/
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(ll)
                .zoom(zoom)
                .bearing(30)
                .tilt(45)
                .build()));
//        setMarker(ll);
    }

    private void setMarker(LatLng ll, float zoom) {
        if (marker != null) {
            marker.remove();
        }
        int firsttime = 1;
        if (firsttime == 1) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
            mMap.animateCamera(update);
            firsttime = 0;
        }

        options = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_icon)).title("my location").anchor(0.5f, 1f);
        marker = mMap.addMarker(options);
        animateMarker(marker, ll, false);
    }

    private void animateMarker(final Marker marker, final LatLng ll, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * ll.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * ll.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

}
