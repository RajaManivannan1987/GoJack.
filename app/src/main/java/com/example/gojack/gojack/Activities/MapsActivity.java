package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.HelperClasses.SearchLocation;
import com.example.gojack.gojack.Interface.PlaceInterface;
import com.example.gojack.gojack.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SearchLocation searchLocation;
    private LatLng latLng;
    private View.OnClickListener currentLocation;
    private FloatingActionButton locationFloatingActionButton, location1FloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        searchLocation = new SearchLocation(this, new PlaceInterface() {
            @Override
            public void getPlace(Place place) {
                if (place != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                }
            }
        });
        locationFloatingActionButton = (FloatingActionButton) findViewById(R.id.dashboardActivityCurrentLocationFloatingActionButton);
        currentLocation = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(AppControler.locationInstance().getLocation()));
            }
        };
        locationFloatingActionButton.setOnClickListener(currentLocation);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //changeCameraPosition(AppControler.locationInstance().getLocation());
        enableCurrentLocation();

        // Add a marker in Sydney and move the camera
    }

    private void changeCameraPosition(LatLng latLng) {
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        mMap.animateCamera(yourLocation, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                updateAddress();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void updateAddress() {

    }

    private void enableCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }
}
