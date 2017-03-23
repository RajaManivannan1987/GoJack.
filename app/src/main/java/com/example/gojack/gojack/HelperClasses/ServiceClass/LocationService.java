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
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IM0033 on 8/9/2016.
 */
public class LocationService extends Service implements LocationListener {
    private static final String TAG = "LocationService";
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
    private WebServiceClasses webServiceClasses;
    private Timer timer;
    private TimerTask timerTask;
    PrefManager prefManager;

    @Override
    public void onCreate() {
        super.onCreate();
        webServiceClasses = new WebServiceClasses(this, TAG);
        prefManager = new PrefManager(LocationService.this);
        timer = new Timer();
        Log.d(TAG, "ServiceStarted");

        getCurrentLocation();
    }

    private void getCurrentLocation() {
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
                        return;
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
                                //sendLocationToServer(latitude, longitude);
                            }
                        }
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
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

    private void scheduleTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                sendLocationToServer(latitude, longitude);
            }
        };
        try {
            timer.schedule(timerTask, 01, 1000 * 30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(Intent name) {
        Log.d(TAG, "stopService");
        timer.cancel();
        timerTask.cancel();
        stopSelf();
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

    public void sendLocationToServer(double lat, double lang) {
        if (!prefManager.getPilotToken().equalsIgnoreCase(""))
            webServiceClasses.sendLocation(String.valueOf(lat), String.valueOf(lang), new VolleyResponseListerner() {
                @Override
                public void onResponse(JSONObject response) throws JSONException {

                }

                @Override
                public void onError(String message, String title) {

                }
            });
    }

    @Override
    public void onLocationChanged(Location location) {

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
}
