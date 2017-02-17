package com.example.gojack.gojack.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.GCMClasses.RegistrationIntentService;
import com.example.gojack.gojack.HelperClasses.AlertDialogManager;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.PrefManager;
import com.example.gojack.gojack.HelperClasses.Validation;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class ChangePassword extends CommonActionBar {
    private EditText newPasswordEditText, confirmNewPasswordEditText;
    String customerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setActionBar();
        customerId = getIntent().getExtras().getString("customerId");
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = (EditText) findViewById(R.id.confirmNewPasswordEditText);
        findViewById(R.id.changePasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPasswordValidate();
            }
        });
    }

    private void setPasswordValidate() {
        if (Validation.isPasswordValid(newPasswordEditText.getText().toString())) {
            newPasswordEditText.setError(null);
            if (Validation.isPasswordValid(confirmNewPasswordEditText.getText().toString())) {
                confirmNewPasswordEditText.setError(null);
                if (newPasswordEditText.getText().toString().startsWith(confirmNewPasswordEditText.getText().toString())) {
                    new WebServiceClasses(ChangePassword.this, "ChangePassword").changePassword(customerId, newPasswordEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                JSONObject jObject = response.getJSONObject("data");
                                CommonMethods.toast(ChangePassword.this, response.getString("message"));
                                PrefManager.getPrefManager(ChangePassword.this).setLoginDetails(jObject.getString("token"), jObject.getString("name"), jObject.getString("ping_location"), jObject.getString("driverid"), jObject.getString("gender"));
                                JSONObject vehicleDetail = jObject.getJSONObject("Vehicle");
                                PrefManager.getPrefManager(ChangePassword.this).setVehileDetails(vehicleDetail.getString("vehicle_make"), vehicleDetail.getString("vehicle_model"), vehicleDetail.getString("vehicle_registration_number"), vehicleDetail.getString("bike_photo"), vehicleDetail.getString("balance_status"), vehicleDetail.getString("balance_message"), vehicleDetail.getString("photo"));
                                startService(new Intent(ChangePassword.this, RegistrationIntentService.class));
                                startActivity(new Intent(ChangePassword.this, GoOnline.class));
                                finish();
                            } else {
                                CommonMethods.toast(ChangePassword.this, response.getString("message"));
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            AlertDialogManager.showAlertDialog(ChangePassword.this, title, message, false);
                        }
                    });
                    startActivity(new Intent(ChangePassword.this, GoOffline.class));
                } else {
                    confirmNewPasswordEditText.setError("Password mismatch");
                    confirmNewPasswordEditText.requestFocus();
                }
            } else {
                confirmNewPasswordEditText.requestFocus();
                confirmNewPasswordEditText.setError(Validation.passwordError);
            }
        } else {
            newPasswordEditText.requestFocus();
            newPasswordEditText.setError(Validation.passwordError);
        }
    }
}
