package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.ServiceClass.GPSTracker;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Im033 on 7/13/2017.
 */


public class CheckBalanceActivity extends CommonActionBar {
    ProgressBar progressBar;
    private static float paytmBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);
        progressBar = (ProgressBar) findViewById(R.id.checkProgressBar);
        if (!PrefManager.getPrefManager(CheckBalanceActivity.this).getPilotPaytmtoken().equalsIgnoreCase("")) {
            updatePilotStatus();
        } else {
            startActivity(new Intent(CheckBalanceActivity.this, PaytmLogin.class));
            finish();
        }
    }

    private void updatePilotStatus() {
        WebServiceClasses.getWebServiceClasses(this, "CheckBalanceActivity").getPilotStatus(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1") && response.getString("status").equalsIgnoreCase("1")) {
                    checkPaytmBalance(PrefManager.getPrefManager(CheckBalanceActivity.this).getPilotPaytmtoken());
                } else {
                    startActivity(new Intent(CheckBalanceActivity.this, GoOnline.class));
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void checkPaytmBalance(String pilotPaytmtoken) {
        WebServiceClasses.getWebServiceClasses(CheckBalanceActivity.this, "").checkBalance(pilotPaytmtoken, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                paytmBalance = Float.parseFloat(response.getJSONObject("response").getString("amount"));
                if (paytmBalance < 300) {
                    startActivity(new Intent(CheckBalanceActivity.this, GoOnline.class));
                    finish();
                } else {
                    startActivity(new Intent(CheckBalanceActivity.this, LocationCheckActivity.class));
                    finish();
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
