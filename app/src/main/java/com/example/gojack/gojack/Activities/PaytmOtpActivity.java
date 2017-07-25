package com.example.gojack.gojack.Activities;

/**
 * Created by Im033 on 5/30/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.Validate.Validation;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PaytmOtpActivity extends CommonActionBar {
    private String TAG = "PaytmOtpActivity";
    private EditText paytmotpEditText;
    private Button paytmotpButton;
    private String state;
    private WebServiceClasses webServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_paytm_otp);

        webServices = new WebServiceClasses(PaytmOtpActivity.this, TAG);
        state = getIntent().getExtras().getString("paytmstate");
        paytmotpButton = (Button) findViewById(R.id.paytmotpButton);
        paytmotpEditText = (EditText) findViewById(R.id.paytmotpEditText);
        paytmotpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.isPaytmOtpValid(paytmotpEditText.getText().toString())) {
                    paytmLogin();
                } else {
                    paytmotpEditText.setError("Enter otp");
                    paytmotpEditText.requestFocus();
                }
            }
        });
    }


    private void paytmLogin() {
        webServices.verifyPaytmOTP(paytmotpEditText.getText().toString().trim(), state, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.has("status")) {
                    CommonMethods.toast(PaytmOtpActivity.this, response.getString("message"));
                } else {
                    updatePaytmToken(response.getString("access_token"));
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void updatePaytmToken(final String access_token) {
        webServices.updatePaytmToken(access_token, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    new PrefManager(PaytmOtpActivity.this).setPaytmtoken(access_token);
                    startActivity(new Intent(PaytmOtpActivity.this, GoOnline.class));
                    finish();
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }


}

