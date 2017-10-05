package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.ScheduleThread.ScheduleThread;
import com.example.gojack.gojack.HelperClasses.ScheduleThread.TimerInterface;
import com.example.gojack.gojack.R;

/**
 * Created by Im033 on 4/19/2017.
 */

public class LocationCheckActivity extends AppCompatActivity {
    private static String TAG = "LocationCheckActivity";
    private LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private final int MY_LOCATION = 1;
    private ScheduleThread thread;
    private ImageView animationIcon;
    private RelativeLayout locationCheckRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationcheck);
        animationIcon = (ImageView) findViewById(R.id.animationIcon);

        locationCheckRelativeLayout = (RelativeLayout) findViewById(R.id.locationCheckRelativeLayout);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        thread = new ScheduleThread(new TimerInterface() {
            @Override
            public void onRun() {
                Log.d(TAG, "Inside Thread");
                if (AppControler.locationInstance() != null && AppControler.locationInstance().getLocation() != null) {
                    Log.d(TAG, "Inside Thread Success");
                    startActivity(new Intent(LocationCheckActivity.this, GoOffline.class));
                    thread.stop();
                }
            }
        }, 2000, 0, this);
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        animationIcon.startAnimation(myAnim);
    }

    private void enableLocation() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Enable Location Services");
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();
        } else {
            enableMyLocation();
        }
    }

    private boolean enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationCheckActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION);
        } else {
            AppControler.instanceLocation(LocationCheckActivity.this);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                } else {
                    CommonMethods.showSnakBar("My Location permission denied", animationIcon);
                    finish();
                }
                break;
        }

    }
}
