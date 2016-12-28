package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.example.gojack.gojack.HelperClasses.PrefManager;
import com.example.gojack.gojack.HelperClasses.SwipeButtonStyle.SwipeButton;
import com.example.gojack.gojack.HelperClasses.SwipeButtonStyle.SwipeButtonCustomItems;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.ServiceClass.GPSTracker;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.GoJackServerUrls;
import com.example.gojack.gojack.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by Im033 on 12/8/2016.
 */
public class HailActivity extends CommonNavigstionBar implements PlaceSelectionListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "HailActivity";
    private GoogleMap hailPageMap;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    private LatLng currentLocation, toAddressLatLng;
    private PrefManager prefManager;
    private String currentLat, currentLong, toLat, toLang, responseObject, ridetype, rideid, toAddress;
    private SwipeButton startTripButton, endTripButton;
    private TextView hailOnTripTextView;
    private ImageView hailDirectionButton;
    private Marker marker;
    int drawable;
    private PlaceAutocompleteFragment autocompleteFragment;
    MarkerOptions markerOptionsmylocaiton;
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_hail);


        buildGoogleApiClient();

        gpsTracker = new GPSTracker();
        prefManager = PrefManager.getPrefManager(HailActivity.this);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint("Choose your destination...");
        autocompleteFragment.getView().setBackground(getResources().getDrawable(R.drawable.background_edit_text));
        autocompleteFragment.setOnPlaceSelectedListener(this);

        hailOnTripTextView = (TextView) findViewById(R.id.hailOnTripTextView);
        startTripButton = (SwipeButton) findViewById(R.id.hailStartTripButton);
        endTripButton = (SwipeButton) findViewById(R.id.hailEndTripButton);
        hailDirectionButton = (ImageView) findViewById(R.id.hailDirectionButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.hailPageMap);
        mapFragment.getMapAsync(this);
        hailDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toAddressLatLng != null) {
                    String lat = String.valueOf(toAddressLatLng.latitude);
                    String lang = String.valueOf(toAddressLatLng.longitude);
                    CommonMethods.locationDirection(HailActivity.this, lat, lang);
                } else {
                    CommonMethods.toast(HailActivity.this, "Choose your destination");
                }
            }
        });
        SwipeButtonCustomItems startSwipeButton = new SwipeButtonCustomItems() {
            @Override
            public void onSwipeConfirm() {
                String address = CommonMethods.getMarkerMovedAddress(HailActivity.this, new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
                WebServiceClasses.getWebServiceClasses(HailActivity.this, TAG).hailStartTrip(address, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("token_status").equalsIgnoreCase("1")) {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                rideid = response.getString("rideid");
                                ridetype = response.getString("ridetype");
                                //  CommonMethods.toast(HailActivity.this, response.getString("message"));
                                // flag = true;
                                setMarket(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), prefManager.getGender());
                                startTripButton.setVisibility(View.GONE);
                                autocompleteFragment.getView().setVisibility(View.GONE);
                                endTripButton.setVisibility(View.VISIBLE);
                                hailOnTripTextView.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onError(String message, String title) {

                    }
                });
            }
        };
        startSwipeButton.setButtonPressText(">> Slide to Start Trip >>")
                .setGradientColor1(0xFF549fd0)
                .setGradientColor2(0xFF666666)
                .setGradientColor2Width(45)
                .setGradientColor3(0xFF333333)
                .setPostConfirmationColor(0xFF05e177)
                .setActionConfirmDistanceFraction(0.8)
                .setActionConfirmText("Trip Started");
        if (startTripButton != null) {
            startTripButton.setSwipeButtonCustomItems(startSwipeButton);
        }
        SwipeButtonCustomItems endSwip = new SwipeButtonCustomItems() {
            @Override
            public void onSwipeConfirm() {
//                if (ridetype.equalsIgnoreCase("courier")) {
//                    showCourireAlertBox();
//                }
                String address = CommonMethods.getMarkerMovedAddress(HailActivity.this, new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));

                WebServiceClasses.getWebServiceClasses(HailActivity.this, TAG).hideEndTrip(rideid, currentLat, currentLong, address, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
//                        if (response.getString("status").equalsIgnoreCase("1")) {
//                            CommonMethods.toast(HailActivity.this, response.getString("message"));
//                            JSONObject jsonObject = response.getJSONObject("data");
//                        responseObject = response.toString();
                        if (!ridetype.equalsIgnoreCase("courier")) {
                            Intent i = new Intent(HailActivity.this, EndTripDetailActivity.class);
                            i.putExtra("rideId", rideid);
                            i.putExtra("EndTrip", response.toString());
                            startActivity(i);
                        }
                    }
//                    }

                    @Override
                    public void onError(String message, String title) {

                    }
                });
            }

        };
        endSwip.setButtonPressText(">> Trip End >>")
                .setGradientColor1(0xFFf03131)
                .setGradientColor2(0xFF666666)
                .setGradientColor2Width(45)
                .setGradientColor3(0xFF333333)
                .setPostConfirmationColor(0xFFf03131)
                .setActionConfirmDistanceFraction(0.8)
                .setActionConfirmText("Trip End");
        if (endTripButton != null) {
            endTripButton.setSwipeButtonCustomItems(endSwip);
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void checkBuildPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CommonMethods.checkLocationPermission(HailActivity.this)) {
                if (!CommonMethods.checkProvider(HailActivity.this)) {
                    CommonMethods.showLocationAlert(HailActivity.this);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        handleNewLocation(mCurrentLocation);
    }

    private void handleNewLocation(Location mCurrentLocation) {
        currentLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        currentLat = String.valueOf(currentLocation.latitude);
        currentLong = String.valueOf(currentLocation.longitude);
        setMarket(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), prefManager.getGender());
    }

    private void setMarket(LatLng currentLocation, String genderType) {
        if (marker != null) {
            marker.remove();
        }
        LatLng ll = new LatLng(currentLocation.latitude, currentLocation.longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 17);
        hailPageMap.animateCamera(update);
        if (genderType.startsWith("Male")) {
            drawable = R.drawable.male_pilot_icon;
        } else {
            drawable = R.drawable.female_pilot_icon;
        }
        LatLng position = new LatLng(currentLocation.latitude, currentLocation.longitude);
        markerOptionsmylocaiton = new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(drawable)).title("").anchor(0.5f, 1f);
        marker = hailPageMap.addMarker(markerOptionsmylocaiton);
        LatLng latlang = new LatLng(currentLocation.latitude, currentLocation.longitude);
        animateMarker(marker, latlang, false);
    }

    private void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = hailPageMap.getProjection();
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
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 17);
                }
                {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mRequestingLocationUpdates = true;
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        checkBuildPermission();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        hailPageMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        hailPageMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        if (!CommonMethods.checkProvider(HailActivity.this)) {
                            CommonMethods.showLocationAlert(HailActivity.this);
                        }
                } else {
                    CommonMethods.checkLocationPermission(HailActivity.this);
                }
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        toAddress = CommonMethods.getMarkerMovedAddress(HailActivity.this, place.getLatLng());
        toAddressLatLng = place.getLatLng();
//        String str = getMarkerMovedAddress(place.getLatLng(), "toaddress");
        if (toAddress != null && !toAddress.equals("")) {
            SpannableStringBuilder sb = new SpannableStringBuilder(toAddress);
            sb.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.contentSizeSmall)), 0, toAddress.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//resize size
            if (autocompleteFragment != null) {
                autocompleteFragment.setText(sb);
            }
        }
    }
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());
    }
}