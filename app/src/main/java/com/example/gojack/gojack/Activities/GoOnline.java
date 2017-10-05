package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Common.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.ServiceClass.GPSTracker;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.SwipeButtonStyle.SwipeButton;
import com.example.gojack.gojack.HelperClasses.SwipeButtonStyle.SwipeButtonCustomItems;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.ModelClasses.VehicleDetails;
import com.example.gojack.gojack.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by IM0033 on 8/3/2016.
 */
public class GoOnline extends CommonNavigstionBar {
    private String TAG = "GoOnline";
    private TextView bikeNameTextView, bikeModelTextView, bikeNoTextView, balanceStatus, balanceTextView;
    private CircleImageView bikeImageView;
    private SwipeButton swipeButton;
    private static float paytmBalance;
    private Button pilotAccountRechargeButton;
    private VehicleDetails getData;
    SwipeButtonCustomItems swipeButtonCustomItems;
    private PrefManager prefManager;
    private View onLineView;
    private WebServiceClasses webServices;
    private String message = "Your wallet balance is low.\n" +
            "Add money to Wallet";
    private String message1 = "Your wallet balance is below the minimum limit.\n" +
            "You will not receive any rides until your account is recharged.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CommonMethods.checkLocationPermission(GoOnline.this)) {
                if (!CommonMethods.checkProvider(GoOnline.this)) {
                    CommonMethods.showLocationAlert(GoOnline.this);
                }
            }

        }
        webServices = new WebServiceClasses(GoOnline.this, TAG);
        prefManager = new PrefManager(GoOnline.this);
        getData = VehicleDetails.getVehicleDetails();
        loadVehicleDetails();
        if (!prefManager.getPilotPaytmtoken().equalsIgnoreCase("")) {
            checkBalance(prefManager.getPilotPaytmtoken());
        } else {
            startActivity(new Intent(GoOnline.this, PaytmLogin.class));
        }
        bikeNameTextView.setText(PrefManager.getPrefManager(GoOnline.this).getVehicleMake());
        bikeModelTextView.setText(PrefManager.getPrefManager(GoOnline.this).getVehicleModel());
        bikeNoTextView.setText(PrefManager.getPrefManager(GoOnline.this).getVehicleNumber());
//        balanceStatus.setText(PrefManager.getPrefManager(GoOnline.this).getBalanceMessage());
        if (prefManager.getVehiclePhoto().length() != 0 && !prefManager.getVehiclePhoto().equalsIgnoreCase("")) {
            Picasso.with(GoOnline.this).load(prefManager.getVehiclePhoto())
                    .placeholder(R.drawable.user_icon)
                    .error(R.drawable.user_photo_icon)
                    .resize(250, 200).into(bikeImageView);
        }
//        Picasso.with(GoOnline.this).load(prefManager.getVehiclePhoto()).into(bikeImageView);
        SwipeButtonCustomItems swipeButtonCustomItems = new SwipeButtonCustomItems() {

            @Override
            public void onSwipeConfirm() {
                if (!CommonMethods.checkProvider(GoOnline.this)) {
                    CommonMethods.showLocationAlert(GoOnline.this);
                }
                stopService(setIntent(getBaseContext()));
                startService(setIntent(getBaseContext()));
                webServices.getWebServiceClasses(GoOnline.this, TAG).updateStatus("1", new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        startActivity(new Intent(GoOnline.this, LocationCheckActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                    @Override
                    public void onError(String message, String title) {
                        CommonMethods.showSnakBar(message, bikeModelTextView);
                    }
                });
                /*startActivity(new Intent(GoOnline.this, GoOffline.class));
                finish();*/

            }
        };
        swipeButtonCustomItems.setButtonPressText("Swipe To Go Online")
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
        pilotAccountRechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoOnline.this, PaymentActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkBalance(String pilotPaytmtoken) {
        webServices.checkBalance(pilotPaytmtoken, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                // For Live 10/8/2017
               /* paytmBalance = Float.parseFloat(response.getJSONObject("response").getString("paytmWalletBalance"));
                balanceTextView.setText("Wallet Balance is: Rs." + response.getJSONObject("response").getString("paytmWalletBalance"));*/
                // For Testing 10/8/2017
                paytmBalance=500;
                if (paytmBalance < 300) {
                    swipeButton.setVisibility(View.GONE);
                    balanceTextView.setVisibility(View.VISIBLE);
                    balanceStatus.setText(message1);
                    pilotAccountRechargeButton.setVisibility(View.VISIBLE);
                    onLineView.setVisibility(View.VISIBLE);
//                    alertDialogBox(message, "Add Money");
                } else if (paytmBalance >= 300 && paytmBalance <= 500) {
                    balanceTextView.setVisibility(View.VISIBLE);
                    swipeButton.setVisibility(View.VISIBLE);
                    pilotAccountRechargeButton.setVisibility(View.VISIBLE);
                    onLineView.setVisibility(View.VISIBLE);
                    balanceStatus.setText(message);
//                    alertDialogBox(message1, "Ok");
                } else {
                    balanceTextView.setVisibility(View.GONE);
                    onLineView.setVisibility(View.GONE);
                    swipeButton.setVisibility(View.VISIBLE);
                    pilotAccountRechargeButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
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
        balanceTextView = (TextView) findViewById(R.id.onLinePageBalanceTextView);
        pilotAccountRechargeButton = (Button) findViewById(R.id.pilotAccountRechargeButton);
        bikeNameTextView = (TextView) findViewById(R.id.bikeNameTextView);
        bikeModelTextView = (TextView) findViewById(R.id.bikeModelTextView);
        bikeNoTextView = (TextView) findViewById(R.id.bikeNoTextView);
        balanceStatus = (TextView) findViewById(R.id.balanceStatus);
        bikeImageView = (CircleImageView) findViewById(R.id.bikeImageView);
        swipeButton = (SwipeButton) findViewById(R.id.my_swipe_button);
        onLineView = findViewById(R.id.onLineView);
    }

    private void alertDialogBox(String message, final String positiveValue) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GoOnline.this);
        if (positiveValue.startsWith("Add Money")) {
            alertDialog.setTitle("Paytm...");
            alertDialog.setCancelable(false);
        } else {
            alertDialog.setTitle("Warning...");
        }
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(positiveValue, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (positiveValue.startsWith("Add Money")) {
                    startActivity(new Intent(GoOnline.this, PaymentActivity.class));
                }
                dialog.dismiss();
            }
        });
        if (positiveValue.startsWith("Add Money")) {
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        try {
            alertDialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
