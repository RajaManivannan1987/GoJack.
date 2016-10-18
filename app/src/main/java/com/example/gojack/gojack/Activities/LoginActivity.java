package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gojack.gojack.GCMClasses.RegistrationIntentService;
import com.example.gojack.gojack.HelperClasses.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LoginActivity";
    private LoginActivity activity = LoginActivity.this;
    private EditText userName, password;
    private Button submitButton;
   // private WebServiceClasses webServiceClasses;
    //private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //webServiceClasses = new WebServiceClasses(this, TAG);
        //prefManager = new PrefManager(this);
        findViewById();
        onClickMethod();
    }


    private void onClickMethod() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userName.getText().toString().trim().equalsIgnoreCase("")) {
                    userName.setError(null);
                    if (!password.getText().toString().trim().equalsIgnoreCase("")) {
                        password.setError(null);
                        WebServiceClasses.getWebServiceClasses(LoginActivity.this, TAG).login(userName.getText().toString().trim(), password.getText().toString().trim(), new VolleyResponseListerner() {
                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                if (response.getString("status").equalsIgnoreCase("1")) {
                                    JSONObject jObject = response.getJSONObject("data");
                                    PrefManager.getPrefManager(LoginActivity.this).setLoginDetails(jObject.getString("token"), jObject.getString("name"), jObject.getString("ping_location"), jObject.getString("driverid"), jObject.getString("gender"));
                                    JSONObject vehicleDetail = jObject.getJSONObject("Vehicle");
                                    PrefManager.getPrefManager(LoginActivity.this).setVehileDetails(vehicleDetail.getString("vehicle_make"), vehicleDetail.getString("vehicle_model"), vehicleDetail.getString("vehicle_registration_number"), vehicleDetail.getString("bike_photo"), vehicleDetail.getString("balance_status"), vehicleDetail.getString("balance_message"));
/*
                                    VehicleDetails setData = VehicleDetails.getVehicleDetails();
                                    JSONObject jsonObject = jObject.getJSONObject("Vehicle");
                                    setData.setVehicle_make(jsonObject.getString("vehicle_make"));
                                    setData.setVehicle_model(jsonObject.getString("vehicle_model"));
                                    setData.setVehicle_number(jsonObject.getString("vehicle_registration_number"));
                                    setData.setBike_photo(jsonObject.getString("bike_photo"));
                                    setData.setBalance_status(jsonObject.getString("balance_status"));
                                    setData.setBalance_message(jsonObject.getString("balance_message"));*/
                                    startService(new Intent(LoginActivity.this, RegistrationIntentService.class));
                                    startActivity(new Intent(activity, GoOnline.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onError(String message, String title) {

                            }
                        });

                    } else {
                        password.setError("Enter Password");
                        password.requestFocus();
                    }
                } else {
                    userName.setError("Enter Pilotname");
                    userName.requestFocus();
                }
            }
        });
        findViewById(R.id.forgotTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, ForgotPassword.class));
//                startActivity(new Intent(activity, NotificationAlertActivity.class));
            }
        });
    }

    private void findViewById() {
        userName = (EditText) findViewById(R.id.userNameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        submitButton = (Button) findViewById(R.id.loginButton);

    }
}
