package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.ImcomeSmsReceiver;
import com.example.gojack.gojack.HelperClasses.Validation;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.ImComeSms;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/3/2016.
 */
public class CodeConfirmation extends CommonActionBar {
    private EditText userNameEditText;
    String customerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_confirmation);
        customerId = getIntent().getExtras().getString("customerId");
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        setActionBar();
        ImcomeSmsReceiver.bindMessageListener(new ImComeSms() {
            @Override
            public void messageReceived(String messageText) {
                userNameEditText.setText(messageText);
            }
        });
        findViewById(R.id.codeSendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.isOtpValid(userNameEditText.getText().toString())) {
                    userNameEditText.setError(null);
                    new WebServiceClasses(CodeConfirmation.this, "CodeConfirm").validateOtp(customerId, userNameEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                startActivity(new Intent(getApplicationContext(), ChangePassword.class).putExtra("customerId", response.getString("userid")));
                            } else {
                                CommonMethods.toast(CodeConfirmation.this, response.getString("message"));
                            }
                        }

                        @Override
                        public void onError(String message, String title) {

                        }
                    });
                } else {
                    userNameEditText.setError(Validation.otpError);
                    userNameEditText.requestFocus();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!CommonMethods.checkmarshmallowPermission(CodeConfirmation.this, Manifest.permission.RECEIVE_SMS, GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION)) {
            //CommonMethods.toast(CodeConfirmation.this,"Enable");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                onStart();
            }
        }
    }
}
