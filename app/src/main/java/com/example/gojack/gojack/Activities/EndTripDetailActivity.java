package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.AlertDialogManager;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/23/2016.
 */
public class EndTripDetailActivity extends CommonActionBar {
    private TextView dateAndTimeTextView, amountTextView, distanceTextView, riderNameTextView, deliverySaveTextView, cashCollectTextView;
    private String payMode, rideId, type;
    private EditText deliveredByEditText;
    private LinearLayout payModeLayout, userLayout, deliverLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_trip);
        setActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        loadData();
        rideId = getIntent().getExtras().getString("RideType");
        deliverySaveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personName = deliveredByEditText.getText().toString();
                WebServiceClasses.getWebServiceClasses(EndTripDetailActivity.this, "EndTripDetailActivity").deliveryPerson(rideId, personName, new VolleyResponseListerner() {
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
                        AlertDialogManager.showAlertDialog(EndTripDetailActivity.this,title,message,false);
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
            distanceTextView.setText("Duration " + jsonObject.getString("distance") + " kms, Time " + jsonObject.getString("minutes"));
            amountTextView.setText(jsonObject.getString("rate"));
            if (jsonObject.has("name")) {
                riderNameTextView.setText(jsonObject.getString("name"));
            }
            dateAndTimeTextView.setText(jsonObject.getString("datetime"));
            payMode = jsonObject.getString("mode");
            type = jsonObject.getString("type");
            if (type.startsWith("courier")) {
                //userLayout.setVisibility(View.vi);
                cashCollectTextView.setVisibility(View.GONE);
                // deliverLayout.setVisibility(View.GONE);
            } else {
                userLayout.setVisibility(View.VISIBLE);
            }
            if (!payMode.startsWith("cash")) {
                payModeLayout.setBackground(getResources().getDrawable(R.drawable.paytm_bg));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
