package com.example.gojack.gojack.Activities;

/**
 * Created by Im033 on 5/30/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.Validate.Validation;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.R;
import com.paytm.pgsdk.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class PaytmLogin extends AppCompatActivity {
    private EditText paytmMobileNodEditText, paytmMailIdEditText;
    private Button paytmLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_login);
        paytmLoginButton = (Button) findViewById(R.id.paytmLoginButton);
        paytmMobileNodEditText = (EditText) findViewById(R.id.paytmMobileNodEditText);
        paytmMailIdEditText = (EditText) findViewById(R.id.paytmMailIdEditText);
        paytmLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paytmMobileNodEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    if (Validation.isUserNameValid(paytmMailIdEditText.getText().toString())) {
                        login();
                    } else {
                        paytmMailIdEditText.setError(Validation.userNameError);
                        paytmMailIdEditText.requestFocus();
                    }
                } else {
                    paytmMobileNodEditText.setError(Validation.paytmMobiledError);
                    paytmMobileNodEditText.requestFocus();
                }
            }
        });
    }

    private void login() {
        new WebServiceClasses(PaytmLogin.this, "Paytm").SendOTP(paytmMobileNodEditText.getText().toString().trim(), paytmMailIdEditText.getText().toString().trim(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("SUCCESS")) {
                    startActivity(new Intent(PaytmLogin.this, PaytmOtpActivity.class).putExtra("paytmstate", response.getString("state")).putExtra("mobileno", paytmMobileNodEditText.getText().toString().trim()).putExtra("mailid", paytmMailIdEditText.getText().toString().trim()));
                    finish();
                } else {
                    if (response.getString("responseCode").equalsIgnoreCase("430")) {
                        CommonMethods.toast(PaytmLogin.this, "Invalid Authorization");
                    } else if (response.getString("responseCode").equalsIgnoreCase("431")) {
                        CommonMethods.toast(PaytmLogin.this, "Invalid Mobile");
                    } else if (response.getString("responseCode").equalsIgnoreCase("432")) {
                        CommonMethods.toast(PaytmLogin.this, "Login Failed");
                    } else if (response.getString("responseCode").equalsIgnoreCase("433")) {
                        CommonMethods.toast(PaytmLogin.this, "Account Blocked");
                    } else if (response.getString("responseCode").equalsIgnoreCase("434")) {
                        CommonMethods.toast(PaytmLogin.this, "Bad Request");
                    } else if (response.getString("responseCode").equalsIgnoreCase("465")) {
                        CommonMethods.toast(PaytmLogin.this, "Invalid Email");
                    }
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
