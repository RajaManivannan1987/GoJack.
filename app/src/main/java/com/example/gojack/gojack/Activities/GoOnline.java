package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.PrefManager;
import com.example.gojack.gojack.HelperClasses.SwipeButtonStyle.SwipeButton;
import com.example.gojack.gojack.HelperClasses.SwipeButtonStyle.SwipeButtonCustomItems;
import com.example.gojack.gojack.ModelClasses.VehicleDetails;
import com.example.gojack.gojack.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by IM0033 on 8/3/2016.
 */
public class GoOnline extends CommonNavigstionBar {
    private String TAG = "GoOnline";
    private TextView bikeNameTextView, bikeModelTextView, bikeNoTextView, balanceStatus;
    private CircleImageView bikeImageView;
    private SwipeButton swipeButton;
    private VehicleDetails getData;
    SwipeButtonCustomItems swipeButtonCustomItems;
    // private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonMethods.checkLocationPermission(GoOnline.this);
        }
        // prefManager = new PrefManager(GoOnline.this);
        getData = VehicleDetails.getVehicleDetails();
        loadVehicleDetails();
        bikeNameTextView.setText(PrefManager.getPrefManager(GoOnline.this).getVehicleMake());
        bikeModelTextView.setText(PrefManager.getPrefManager(GoOnline.this).getVehicleModel());
        bikeNoTextView.setText(PrefManager.getPrefManager(GoOnline.this).getVehicleNumber());
        balanceStatus.setText(PrefManager.getPrefManager(GoOnline.this).getBalanceMessage());

        //Picasso.with(GoOnline.this).load(prefManager.getVehiclePhoto()).into(bikeImageView);
        SwipeButtonCustomItems swipeButtonCustomItems = new SwipeButtonCustomItems() {

            @Override
            public void onSwipeConfirm() {
                if (!CommonMethods.checkProvider(GoOnline.this)) {
                    CommonMethods.showLocationAlert(GoOnline.this);
                }
                stopService(setIntent(getBaseContext()));
                startService(setIntent(getBaseContext()));
                startActivity(new Intent(GoOnline.this, GoOffline.class));
                finish();

            }
        };
        swipeButtonCustomItems.setButtonPressText(">> GO TO ONLINE >>")
                .setGradientColor1(0xFF549fd0)
                .setGradientColor2(0xFF666666)
                .setGradientColor2Width(50)
                .setGradientColor3(0xFF333333)
                .setPostConfirmationColor(0xFF549fd0)
                .setActionConfirmDistanceFraction(0.8)
                .setActionConfirmText("Action Confirmed");
        if (swipeButton != null) {
            swipeButton.setSwipeButtonCustomItems(swipeButtonCustomItems);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        if (!CommonMethods.checkProvider(GoOnline.this)) {
                            CommonMethods.showLocationAlert(GoOnline.this);
                        }
                } else {
                    CommonMethods.checkLocationPermission(GoOnline.this);
                }
        }
    }

    private void loadVehicleDetails() {
        bikeNameTextView = (TextView) findViewById(R.id.bikeNameTextView);
        bikeModelTextView = (TextView) findViewById(R.id.bikeModelTextView);
        bikeNoTextView = (TextView) findViewById(R.id.bikeNoTextView);
        balanceStatus = (TextView) findViewById(R.id.balanceStatus);
        bikeImageView = (CircleImageView) findViewById(R.id.bikeImageView);
        swipeButton = (SwipeButton) findViewById(R.id.my_swipe_button);
    }
}
