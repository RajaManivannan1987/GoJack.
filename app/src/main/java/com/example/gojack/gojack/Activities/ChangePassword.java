package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.GCMClasses.RegistrationIntentService;
import com.example.gojack.gojack.HelperClasses.Common.CommonIntent;
import com.example.gojack.gojack.HelperClasses.DialogBox.AlertDialogManager;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.Validate.Validation;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class ChangePassword extends CommonActionBar {
    private EditText newPasswordEditText, confirmNewPasswordEditText;
    private TextInputLayout newPasswordTextInputLayout,confirmNewPasswordTextInputLayout;
    String customerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setActionBar();
        customerId = getIntent().getExtras().getString(CommonIntent.customerId);
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = (EditText) findViewById(R.id.confirmNewPasswordEditText);
        newPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.newPasswordTextInputLayout);
        confirmNewPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.confirmNewPasswordTextInputLayout);
        findViewById(R.id.changePasswordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPasswordValidate();
            }
        });
    }

    private void setPasswordValidate() {
        if (Validation.isPasswordValid(newPasswordEditText.getText().toString().trim())) {
            newPasswordTextInputLayout.setError(null);
            if (Validation.isPasswordValid(confirmNewPasswordEditText.getText().toString().trim())) {
                confirmNewPasswordTextInputLayout.setError(null);
                if (newPasswordEditText.getText().toString().equalsIgnoreCase(confirmNewPasswordEditText.getText().toString())) {
                    confirmNewPasswordTextInputLayout.setError(null);
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
                                startActivity(new Intent(ChangePassword.this, GoOnline.class).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            } else {
                                CommonMethods.toast(ChangePassword.this, response.getString("message"));
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            CommonMethods.showSnakBar(message,confirmNewPasswordTextInputLayout );
                        }
                    });
                    startActivity(new Intent(ChangePassword.this, GoOffline.class));
                } else {
                    confirmNewPasswordTextInputLayout.setError("Password mismatch");
                    confirmNewPasswordEditText.requestFocus();
                }
            } else {
                confirmNewPasswordEditText.requestFocus();
                confirmNewPasswordTextInputLayout.setError(Validation.passwordError);
            }
        } else {
            newPasswordEditText.requestFocus();
            newPasswordTextInputLayout.setError(Validation.passwordError);
        }
    }
}
