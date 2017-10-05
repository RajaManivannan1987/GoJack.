package com.example.gojack.gojack.Activities;

/**
 * Created by Im033 on 5/30/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.Validate.Validation;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PaytmOtpActivity extends AppCompatActivity {
    private String TAG = "PaytmOtpActivity";
    private EditText paytmotpEditText;
    private Button paytmotpButton;
    private String state, mailId = "", mobileno = "";
    private TextView resendOtp;
    private WebServiceClasses webServices;
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_otp);

        webServices = new WebServiceClasses(PaytmOtpActivity.this, TAG);
        prefManager = new PrefManager(this);
        state = getIntent().getExtras().getString("paytmstate");
        mailId = getIntent().getExtras().getString("mailid");
        mobileno = getIntent().getExtras().getString("mobileno");
        paytmotpButton = (Button) findViewById(R.id.paytmotpButton);
        resendOtp= (TextView) findViewById(R.id.resendOtp);
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
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp();
            }
        });
    }

    private void sendOtp() {
        webServices.SendOTP(mobileno, mailId, new VolleyResponseListerner() {
                @Override
                public void onResponse(JSONObject response) throws JSONException {
                    if (response.getString("status").equalsIgnoreCase("SUCCESS")) {
                        CommonMethods.toast(PaytmOtpActivity.this, "OTP send successful");
                    } else {
                        if (response.getString("responseCode").equalsIgnoreCase("430")) {
                            CommonMethods.showSnakBar("Invalid Authorization", paytmotpEditText);
                        } else if (response.getString("responseCode").equalsIgnoreCase("431")) {
                            CommonMethods.showSnakBar("Invalid Mobile", paytmotpEditText);
                        } else if (response.getString("responseCode").equalsIgnoreCase("432")) {
                            CommonMethods.showSnakBar("Login Failed", paytmotpEditText);
                        } else if (response.getString("responseCode").equalsIgnoreCase("433")) {
                            CommonMethods.showSnakBar("Account Blocked", paytmotpEditText);
                        } else if (response.getString("responseCode").equalsIgnoreCase("434")) {
                            CommonMethods.showSnakBar("Bad Request", paytmotpEditText);
                        } else if (response.getString("responseCode").equalsIgnoreCase("465")) {
                            CommonMethods.showSnakBar("Invalid Email", paytmotpEditText);
                        }
                    }

                }

                @Override
                public void onError(String message, String title) {

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
                    checkPaytmUserValidate(access_token);
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void checkPaytmUserValidate(final String access_token) {
        webServices.checkPaytmUserValidate(access_token, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.has("status") && (response.has("responseCode")) && (response.has("message"))) {
                    if (response.getString("status").equalsIgnoreCase("FAILURE") && response.getString("responseCode").equalsIgnoreCase("530")) {
                        prefManager.setPaytmtoken("", "", "");
                    }
                } else {
                    prefManager.setPaytmtoken(access_token, response.getString("email"), response.getString("mobile"));
                    startActivity(new Intent(PaytmOtpActivity.this, CheckBalanceActivity.class));
                    finish();
                    Log.d(TAG, "Valid Paytm Token");
                }


            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }


}

