package com.example.gojack.gojack.HelperClasses.ServiceClass;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gojack.gojack.Activities.LoginActivity;
import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Common.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IM0033 on 8/4/2016.
 */
public class GPSTracker extends Service implements LocationListener {
    public static final String MY_SERVICE = "com.example.gojack.gojack.HelperClasses.ServiceClass.GPSTracker";
    private static final String TAG = "GPSTracker";
    private static String Network = "NetWork";
    private static String gpsEnable = "gps Enable";
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private static Location mLocation;
    private double latitude, longitude, speed, direction;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager mLocationManager;
    RequestQueue queue;
    //    private WebServiceClasses webServiceClasses;
    private Timer timer;
    private TimerTask timerTask;

    String networkErrorMessage = "Network error – please try again.";
    String poorNetwork = "Your data connection is too slow – please try again when you have a better network connection";
    String timeout = "Response timed out – please try again.";
    String authorizationFailed = "Authorization failed – please try again.";
    String serverNotResponding = "Server not responding – please try again.";
    String parseError = "Data not received from server – please check your network connection.";

    String networkErrorTitle = "Network error";
    String poorNetworkTitle = "Poor Network Connection";
    String timeoutTitle = "Network Error";
    String authorizationFailedTitle = "Network Error";
    String serverNotRespondingTitle = "Server Error";
    String parseErrorTitle = "Network Error";


    @Override
    public void onCreate() {
        super.onCreate();
//        webServiceClasses = new WebServiceClasses(this, TAG);
        timer = new Timer();
        queue = Volley.newRequestQueue(GPSTracker.this);
        Log.d(TAG, "ONCREATE");
        getLocation();
    }

    public GPSTracker() {
    }


    public Location getLocation() {
        try {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //getting gps status
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting network status
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d(TAG, "no network provider is enabled");
                showSettingsAlert();
            } else {
                this.canGetLocation = true;
                //first get location from network provider
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d(Network, Network);
                    if (mLocationManager != null) {
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (mLocation != null) {
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();
                            scheduleTask();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (mLocation == null) {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d(gpsEnable, gpsEnable);
                        if (mLocationManager != null) {
                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mLocation != null) {
                                latitude = mLocation.getLatitude();
                                longitude = mLocation.getLongitude();
                                scheduleTask();
                            }
                        }
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mLocation;
    }

    private void scheduleTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!PrefManager.getPrefManager(GPSTracker.this).getPilotToken().equalsIgnoreCase("")) {
                    sendLocationToServer(latitude, longitude);
//                    Toast.makeText(, latitude + "" + "Lang: " + longitude + "", Toast.LENGTH_SHORT).show();
                }
            }
        };
        try {
            timer.schedule(timerTask, 01, 1000 * 30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLocationToServer(double latitude, double longitude) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(GPSTracker.this).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(GPSTracker.this).getPilotId());
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                GoJackServerUrls.REGISTER_LOCATION, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "volleyPostData  url - " + GoJackServerUrls.REGISTER_LOCATION);
                        Log.d(TAG, "volleyPostData  data - " + jsonObject.toString());
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Log.e(timeout, timeoutTitle);
                } else if (error instanceof NoConnectionError) {
                    Log.e(poorNetwork, poorNetworkTitle);
                } else if (error instanceof AuthFailureError) {
                    Log.e(authorizationFailed, authorizationFailedTitle);
                } else if (error instanceof ServerError) {
                    Log.e(serverNotResponding, serverNotRespondingTitle);
                } else if (error instanceof NetworkError) {
                    Log.e(networkErrorMessage, networkErrorTitle);
                } else if (error instanceof ParseError) {
                    Log.e(parseError, parseErrorTitle);
                }

            }
        });
        int MY_SOCKET_TIMEOUT_MS = 30000;
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
// AppControler.getsInstance().addToRequestQueue(jsonRequest);
    }

    public void stopUsingGps() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude() {
        if (mLocation != null) {
            latitude = mLocation.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (mLocation != null) {
            longitude = mLocation.getLongitude();
        }
        return longitude;
    }

    public void stoptimer() {
        if (timer != null && timerTask != null) {
            timerTask.cancel();
            timer.cancel();
            timer.purge();
        }

    }

    public String getAddress() {
        String address = CommonMethods.getMarkerMovedAddress(this, new LatLng(getLatitude(), getLongitude()));
        return address;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDirection() {
        return direction;
    }

    public boolean isCanGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(GPSTracker.this);
        alertBox.setTitle("GPS is settings");
        alertBox.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertBox.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertBox.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            speed = location.getSpeed();
            direction = location.getBearing();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
//            scheduleTask();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(Intent name) {
        Log.d(TAG, "stopService");
        if (timer != null && timerTask != null) {
            timer.cancel();
            timerTask.cancel();
            stopSelf();
            if (mLocationManager != null)
                mLocationManager.removeUpdates(this);
        }
        return super.stopService(name);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
        if (timerTask != null)
            timerTask.cancel();
        stopSelf();
        Log.v(TAG, "stopService");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mLocationManager != null)
            mLocationManager.removeUpdates(this);
    }


}
