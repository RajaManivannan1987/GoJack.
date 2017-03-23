package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.Common.CommonIntent;
import com.example.gojack.gojack.HelperClasses.DialogBox.AlertDialogManager;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Common.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.Sms.ImcomeSmsReceiver;
import com.example.gojack.gojack.HelperClasses.Validate.Validation;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.HelperClasses.Interface.ImComeSms;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/3/2016.
 */
public class CodeConfirmation extends CommonActionBar {
    private EditText userNameEditText;
    String customerId;
    private TextView resendTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_confirmation);
        customerId = getIntent().getExtras().getString(CommonIntent.customerId);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        resendTextView = (TextView) findViewById(R.id.resendTextView);
        setActionBar();
        ImcomeSmsReceiver.bindMessageListener(new ImComeSms() {
            @Override
            public void messageReceived(String messageText) {
                userNameEditText.setText(messageText);
            }
        });
        resendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameEditText.setText("");
                final ProgressDialog progressDialog = new ProgressDialog(CodeConfirmation.this);
                progressDialog.setMessage("Waiting...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new WebServiceClasses(CodeConfirmation.this, "CodeConfirm").reSendOtp(customerId, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        progressDialog.dismiss();
                        if (response.getString("status").equalsIgnoreCase("1")) {
                            CommonMethods.toast(CodeConfirmation.this, response.getString("message"));
                        }
                    }

                    @Override
                    public void onError(String message, String title) {
                        progressDialog.dismiss();
                    }
                });
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
                                startActivity(new Intent(getApplicationContext(), ChangePassword.class).putExtra(CommonIntent.customerId, response.getString("userid")));
                            } else {
                                CommonMethods.toast(CodeConfirmation.this, response.getString("message"));
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            AlertDialogManager.showAlertDialog(CodeConfirmation.this, title, message, false);
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
