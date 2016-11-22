package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.ActionCompletedSingleton;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.NotifyCustomerSingleton;
import com.example.gojack.gojack.HelperClasses.PrefManager;
import com.example.gojack.gojack.HelperClasses.SwipeButtonStyle.SwipeButton;
import com.example.gojack.gojack.HelperClasses.SwipeButtonStyle.SwipeButtonCustomItems;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.ActionCompleted;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.ModelClasses.CancelReason;
import com.example.gojack.gojack.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class GoOffline extends CommonNavigstionBar implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private String TAG = "GoOffline";
    private GoogleMap mMap;
    private LatLng currentLocation;
    private Marker marker;
    private SwipeButton startTripButton, endTripButton;
    private CircleImageView userImageView, directionButton, callButton;
    private LinearLayout showlayout, hideLayout;
    private PrefManager prefManager;
    private TextView fromAddressTextView, riderNameTextView, showStartTrip, twoAddressTextView, pilotCancelTripTextView;
    private Spinner cancelSpinner;
    private String rideId, start_lat, start_lang, end_lat, end_lang, p_num, riderGender, currentLat, currentLong, ridetype, responseObject;
    private boolean flag = false;
    private List<CancelReason> cancelReasonsList = new ArrayList<CancelReason>();
    private List<String> cancelReasonsListString = new ArrayList<String>();
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;

    int firstTime = 1, drawable;
    MarkerOptions markerOptionsmylocaiton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_offline);
        if (startService(GoOnline.setIntent(getBaseContext())) != null) {
        } else {
            stopService(setIntent(getBaseContext()));
            startService(setIntent(getBaseContext()));
        }
        checkBuildPermission();
        buildGoogleApiClient();
        prefManager = PrefManager.getPrefManager(GoOffline.this);
        findViewById();
        mRequestingLocationUpdates = false;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pilotLocationMap);
        mapFragment.getMapAsync(this);

        onClickEvents();
        if (getIntent().getExtras() != null) {
            if (showlayout.getVisibility() == View.GONE) {
                showRiderDetails();
            }
        } else {
            checkRideStatus();
        }
        NotifyCustomerSingleton.actionCompletedSingleton().setListener(new ActionCompleted() {
            @Override
            public void actionCompleted() {
                startTripButton.setBackgroundColor(getResources().getColor(R.color.ColorGreen));
                startTripButton.setClickable(true);
            }
        });

        ActionCompletedSingleton.actionCompletedSingleton().setListener(new ActionCompleted() {
            @Override
            public void actionCompleted() {
                showlayout.setVisibility(View.GONE);
                showStartTrip.setVisibility(View.GONE);
                setMarket(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), prefManager.getGender(), "");
            }
        });
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

    public void checkBuildPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonMethods.checkLocationPermission(GoOffline.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        if (!CommonMethods.checkProvider(GoOffline.this)) {
                            CommonMethods.showLocationAlert(GoOffline.this);
                        }
                } else {
                    CommonMethods.checkLocationPermission(GoOffline.this);
                }
        }
    }

    private void onClickEvents() {
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    CommonMethods.locationDirection(GoOffline.this, end_lat, end_lang);
                } else {
                    CommonMethods.locationDirection(GoOffline.this, start_lat, start_lang);
                }
            }
        });
        SwipeButtonCustomItems startSwipeButton = new SwipeButtonCustomItems() {
            @Override
            public void onSwipeConfirm() {
                WebServiceClasses.getWebServiceClasses(GoOffline.this, TAG).startTrip(rideId, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("token_status").equalsIgnoreCase("1")) {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                ridetype = response.getString("ridetype");
                                CommonMethods.toast(GoOffline.this, response.getString("message"));
                                flag = true;
                                setMarket(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), "tripStart", riderGender);
                                startTripButton.setVisibility(View.GONE);
                                showStartTrip.setVisibility(View.VISIBLE);
                                endTripButton.setVisibility(View.VISIBLE);
                                twoAddressTextView.setText(getCurrentAddress(new LatLng(Double.parseDouble(end_lat), Double.parseDouble(end_lang))));
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
                if (ridetype.equalsIgnoreCase("courier")) {
                    showCourireAlertBox();
                }
                WebServiceClasses.getWebServiceClasses(GoOffline.this, TAG).endTrip(rideId, currentLat, currentLong, ridetype, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("status").equalsIgnoreCase("1")) {
                            CommonMethods.toast(GoOffline.this, response.getString("message"));
                            JSONObject jsonObject = response.getJSONObject("data");
                            responseObject = jsonObject.toString();
                            if (!ridetype.equalsIgnoreCase("courier")) {
                                Intent i = new Intent(GoOffline.this, EndTripDetailActivity.class);
                                i.putExtra("rideId", rideId);
                                i.putExtra("EndTrip", responseObject);
                                startActivity(i);
                            }
                        }
                    }

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
        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hideLayout.getVisibility() == View.VISIBLE) {
                    hideLayout.setVisibility(View.GONE);
                } else if (hideLayout.getVisibility() == View.GONE) {
                    hideLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.callFunction(GoOffline.this, p_num);
            }
        });
        pilotCancelTripTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBox = new AlertDialog.Builder(GoOffline.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View alertView = inflater.inflate(R.layout.cancel_reason_dialog, null);
                alertBox.setView(alertView);
                getCancelReason();
                cancelSpinner = (Spinner) alertView.findViewById(R.id.cancelSpinner);
                Button yes = (Button) alertView.findViewById(R.id.tripYesButton);
                Button no = (Button) alertView.findViewById(R.id.tripNoButton);

                alertBox.setMessage("Select reason to cancel from below!");
                alertBox.setCancelable(false);
                final AlertDialog alertdialog = alertBox.create();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!cancelReasonsList.isEmpty()) {
                            WebServiceClasses.getWebServiceClasses(GoOffline.this, TAG).pilotCancel(rideId, cancelReasonsList.get(cancelSpinner.getSelectedItemPosition()).getReasonId(), new VolleyResponseListerner() {
                                @Override
                                public void onResponse(JSONObject response) throws JSONException {
                                    if (response.getString("status").equalsIgnoreCase("1")) {
                                        CommonMethods.toast(GoOffline.this, response.getString("message"));
                                        showlayout.setVisibility(View.GONE);
                                        showStartTrip.setVisibility(View.GONE);
                                        showStartTrip.setVisibility(View.GONE);
                                        alertdialog.dismiss();
                                        setMarket(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), prefManager.getGender(), "");
                                    }
                                }

                                @Override
                                public void onError(String message, String title) {

                                }
                            });
                        } else {
                            CommonMethods.toast(GoOffline.this, "Please wait while loading cancel reason...");
                        }
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertdialog.dismiss();
                    }
                });
                alertdialog.show();

            }
        });
    }

    private void getCancelReason() {
        WebServiceClasses.getWebServiceClasses(GoOffline.this, TAG).cancelReason(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                JSONArray array = response.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    CancelReason setreason = new CancelReason();
                    JSONObject jsonObject = array.getJSONObject(i);
                    setreason.setReasonId(jsonObject.getString("reason_id"));
                    setreason.setReason(jsonObject.getString("name"));
                    cancelReasonsList.add(setreason);
                    cancelReasonsListString.add(jsonObject.getString("name"));
                }
                cancelSpinner.setAdapter(new ArrayAdapter<String>(GoOffline.this, android.R.layout.simple_dropdown_item_1line, cancelReasonsListString));

            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }

    private void checkRideStatus() {
        WebServiceClasses.getWebServiceClasses(GoOffline.this, TAG).checkRideStatus(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                JSONObject jsonObject = response.getJSONObject("data");
                if (response.getString("status").equalsIgnoreCase("1")) {
                    getCheckStatusValues(jsonObject, "1");
                    NotifyCustomerSingleton.actionCompletedSingleton().ActionCompleted();
                    //ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();
                } else if (response.getString("status").equalsIgnoreCase("2")) {
                    getCheckStatusValues(jsonObject, "2");
                    //ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();
                    NotifyCustomerSingleton.actionCompletedSingleton().ActionCompleted();
                    twoAddressTextView.setText(getCurrentAddress(new LatLng(Double.parseDouble(end_lat), Double.parseDouble(end_lang))));
                    startTripButton.setVisibility(View.GONE);
                    endTripButton.setVisibility(View.VISIBLE);
                    showStartTrip.setVisibility(View.VISIBLE);

                } else if (response.getString("status").equalsIgnoreCase("3")) {
                    Intent i = new Intent(GoOffline.this, EndTripDetailActivity.class);
                    i.putExtra("EndTrip", jsonObject.toString());
                    startActivity(i);
                } else {
                    showlayout.setVisibility(View.GONE);
                    //startTripButton.setVisibility(View.GONE);
                    showStartTrip.setVisibility(View.GONE);
                    setMarket(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), prefManager.getGender(), "");
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void getCheckStatusValues(JSONObject jsonObject, String type) throws JSONException {
        showlayout.setVisibility(View.VISIBLE);
        if (type.startsWith("3")) {

        } else {
            rideId = jsonObject.getString("rideid");
            riderNameTextView.setText(jsonObject.getString("name"));
            p_num = jsonObject.getString("mobilenumber");
            start_lat = jsonObject.getString("startinglatitude");
            start_lang = jsonObject.getString("startinglongitude");
            riderGender = jsonObject.getString("gender");
            end_lat = jsonObject.getString("endinglatitude");
            end_lang = jsonObject.getString("endinglongitude");
            fromAddressTextView.setText(getCurrentAddress(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lang))));
            setMarket(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), "tripStart", riderGender);
        }
    }

    private void showRiderDetails() {
        showlayout.setVisibility(View.VISIBLE);
       /* startTripButton = (SwipeButton) findViewById(R.id.startTripButton);
        endTripButton = (SwipeButton) findViewById(R.id.endTripButton);
        pilotCancelTripTextView = (TextView) findViewById(R.id.pilotCancelTripTextView);*/
        riderNameTextView.setText(getIntent().getExtras().getString("riderName"));
        rideId = getIntent().getExtras().getString("rideId");
        start_lat = getIntent().getExtras().getString("start_lat");
        start_lang = getIntent().getExtras().getString("start_lang");
        end_lat = getIntent().getExtras().getString("end_lat");
        end_lang = getIntent().getExtras().getString("end_lang");
        p_num = getIntent().getExtras().getString("p_num");
        riderGender = getIntent().getExtras().getString("gender");
        fromAddressTextView.setText(getCurrentAddress(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lang))));
    }

    private void findViewById() {
        userImageView = (CircleImageView) findViewById(R.id.userImageView);
        showlayout = (LinearLayout) findViewById(R.id.showlayout);
        hideLayout = (LinearLayout) findViewById(R.id.hideLayout);
        fromAddressTextView = (TextView) findViewById(R.id.fromAddressTextView);
        riderNameTextView = (TextView) findViewById(R.id.riderNameTextView);
        //startTripButton = (Button) findViewById(R.id.startTripButton);
        showStartTrip = (TextView) findViewById(R.id.showStartTrip);
        directionButton = (CircleImageView) findViewById(R.id.directionButton);
        endTripButton = (SwipeButton) findViewById(R.id.endTripButton);
        startTripButton = (SwipeButton) findViewById(R.id.startTripButton);
        startTripButton.setClickable(false);
        callButton = (CircleImageView) findViewById(R.id.callButton);
        twoAddressTextView = (TextView) findViewById(R.id.twoAddressTextView);
        pilotCancelTripTextView = (TextView) findViewById(R.id.pilotCancelTripTextView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // today 22.11.2016
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        //mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMyLocationEnabled(true);
    }

    private String getCurrentAddress(LatLng currentLocation) {
        String addres = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
            if (addresses != null) {
                Address returnAddress = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                    sb.append(returnAddress.getAddressLine(i));
                }
                addres = sb.toString();
            }

        } catch (Exception e) {

        }
        return addres;
    }

    private void setMarket(LatLng currentLocation, String genderType, String riderGender) {
        if (marker != null) {
            marker.remove();
        }

        if (firstTime == 1) {
            LatLng ll = new LatLng(currentLocation.latitude, currentLocation.longitude);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 17);
            mMap.animateCamera(update);
            firstTime = 0;
        }

        if (genderType.startsWith("Male")) {
            drawable = R.drawable.male_pilot_icon;
        } else if (genderType.startsWith("Female")) {
            drawable = R.drawable.female_pilot_icon;
        } else {
            if (riderGender.startsWith("male")) {
                drawable = R.drawable.male_pilot_ride_icon;
            } else {
                drawable = R.drawable.female_pilot_ride_icon;
            }
        }


        LatLng position = new LatLng(currentLocation.latitude, currentLocation.longitude);
        markerOptionsmylocaiton = new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(drawable)).title("").anchor(0.5f, 1f);
        marker = mMap.addMarker(markerOptionsmylocaiton);
        LatLng latlang = new LatLng(currentLocation.latitude, currentLocation.longitude);
        animateMarker(marker, latlang, false);


        //Today 22.11.16

      /*  if (genderType.startsWith("Male")) {
            marker = mMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.male_pilot_icon)));
        } else if (genderType.startsWith("Female")) {
            marker = mMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.female_pilot_icon)));
        } else {
            if (riderGender.startsWith("male")) {
                marker = mMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.male_pilot_ride_icon)));
            } else {
                marker = mMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.female_pilot_ride_icon)));
            }
        }*/
    }

    private void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
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
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

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
      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } else {
            handleNewLocation(mLastLocation);
        }*/
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private void handleNewLocation(Location mLastLocation) {
        currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        currentLat = String.valueOf(currentLocation.latitude);
        currentLong = String.valueOf(currentLocation.longitude);
        setMarket(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), prefManager.getGender(), "");
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        handleNewLocation(mCurrentLocation);

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
        super.onStart();
        mGoogleApiClient.connect();
    }

    private void showCourireAlertBox() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.courier_alertbox, null);
        alertDialog.setView(dialogView);
        final EditText input = (EditText) dialogView.findViewById(R.id.addcategoryEdittext);
        Button confirmButton = (Button) dialogView.findViewById(R.id.categoryaddButton);
        alertDialog.setMessage("Delivered To:");
        alertDialog.setCancelable(false);
        final AlertDialog alertDialog1 = alertDialog.create();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input.getText().toString().trim().equals("")) {
                    String personName = input.getText().toString();
                    WebServiceClasses.getWebServiceClasses(GoOffline.this, "EndTripDetailActivity").deliveryPerson(rideId, personName, new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getString("token_status").equalsIgnoreCase("1")) {
                                CommonMethods.toast(GoOffline.this, response.getString("message"));
                                Intent i = new Intent(GoOffline.this, EndTripDetailActivity.class);
                                i.putExtra("rideId", rideId);
                                i.putExtra("EndTrip", responseObject);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onError(String message, String title) {

                        }
                    });
                    alertDialog1.dismiss();
                } else {
                    input.setFocusable(true);
                    input.setError("Enter Delivery person name");
                }
            }
        });

        alertDialog1.show();
        //alertDialog.create();
    }
}