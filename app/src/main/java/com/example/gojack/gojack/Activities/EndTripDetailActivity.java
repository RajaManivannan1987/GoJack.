package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Common.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by IM0033 on 8/23/2016.
 */
public class EndTripDetailActivity extends CommonActionBar {
    private TextView dateAndTimeTextView, amountTextView, distanceTextView, riderNameTextView, deliverySaveTextView, cashCollectTextView;
    private String payMode, rideType, orderId = "";
    private String rideId = "", activityName = "";
    private EditText deliveredByEditText;
    private LinearLayout payModeLayout, userLayout, deliverLayout;
    private WebServiceClasses webService;
    private PrefManager prefManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_end_trip);
        webService = new WebServiceClasses(EndTripDetailActivity.this, "EndTripDetailActivity");
        prefManager = new PrefManager(this);
//        setActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        rideId = getIntent().getExtras().getString("rideId");
        activityName = getIntent().getExtras().getString("activityName");
        loadData();
        deliverySaveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personName = deliveredByEditText.getText().toString();
                webService.getWebServiceClasses(EndTripDetailActivity.this, "EndTripDetailActivity").deliveryPerson(rideId, personName, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("token_status").equalsIgnoreCase("1")) {
                            CommonMethods.toast(EndTripDetailActivity.this, response.getString("message"));
                            deliverySaveTextView.setVisibility(View.GONE);
                            deliveredByEditText.setEnabled(false);
                            deliveredByEditText.setFocusable(false);
                            deliveredByEditText.setCursorVisible(false);
                            //deliveredByEditText.setText("0");
                        }
                    }

                    @Override
                    public void onError(String message, String title) {
                        CommonMethods.showSnakBar(message, deliveredByEditText);
                    }
                });
            }
        });
    }

    private void loadData() {

        findViewById(R.id.dashboardButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EndTripDetailActivity.this, GoOffline.class));
                finish();
            }
        });
        dateAndTimeTextView = (TextView) findViewById(R.id.dateAndTimeTextView);
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        riderNameTextView = (TextView) findViewById(R.id.riderNameTextView);
        payModeLayout = (LinearLayout) findViewById(R.id.payModeLayout);
        deliverySaveTextView = (TextView) findViewById(R.id.deliverySaveTextView);
        deliveredByEditText = (EditText) findViewById(R.id.deliveredByEditText);
        userLayout = (LinearLayout) findViewById(R.id.userLayout);
        deliverLayout = (LinearLayout) findViewById(R.id.deliverLayout);
        cashCollectTextView = (TextView) findViewById(R.id.cashCollectTextView);

        try {
            JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("EndTrip"));
            distanceTextView.setText("Distance: " + jsonObject.getString("distance") + " kms, Time: " + jsonObject.getString("minutes"));
            amountTextView.setText(jsonObject.getString("rate"));
            if (jsonObject.has("name")) {
                riderNameTextView.setText(jsonObject.getString("name"));
            }else {
                riderNameTextView.setText("Hail User");
            }
            dateAndTimeTextView.setText(jsonObject.getString("datetime"));
            payMode = jsonObject.getString("mode");
            rideType = jsonObject.getString("type");
            if (rideType.startsWith("courier")) {
                cashCollectTextView.setVisibility(View.GONE);
            } else {
                userLayout.setVisibility(View.VISIBLE);
            }
            if (activityName.equalsIgnoreCase("Hail") && payMode.startsWith("cash")) {
                withDrawAmount(jsonObject.getString("commission"));
            }
            if (!payMode.startsWith("cash")) {
                payModeLayout.setBackground(getResources().getDrawable(R.drawable.paytm_bg));
            } else {
                if (!activityName.equalsIgnoreCase("Hail"))
                    withDrawAmount(jsonObject.getString("commission"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void withDrawAmount(String cash) {
        Random r = new Random(System.currentTimeMillis());
        orderId = "dialjack" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(100);
        String order = rideId + "p";
        webService.generateWithDrawChecksum(order, prefManager.getPilotId(), cash, GoJackServerUrls.WITHDRAW_RQUESTTYPE, prefManager.getPilotPaytmtoken(), "9865132365", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                CommonMethods.toast(EndTripDetailActivity.this, response.getString("ResponseMessage"));
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

}
