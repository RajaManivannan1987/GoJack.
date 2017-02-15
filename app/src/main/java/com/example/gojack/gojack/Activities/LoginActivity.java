package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LoginActivity";
    private LoginActivity activity = LoginActivity.this;
    private EditText userName, password;
    private Button submitButton;
    private String MobilePattern = "[0-9]{10}";
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
                loginValidate();
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

    private void loginValidate() {
        if (Validation.emailPhoneValidation(userName.getText().toString()).equalsIgnoreCase("email") || Validation.emailPhoneValidation(userName.getText().toString()).equalsIgnoreCase("phone")) {
            userName.setError(null);
            if (Validation.isPasswordValid(password.getText().toString())) {
                password.setError(null);
                login();
            } else {
                password.setError(Validation.passwordError);
                password.requestFocus();
            }
        } else {
            userName.setError(Validation.emailPhoneValidation(userName.getText().toString()));
            userName.requestFocus();
        }
   /*     if (userName.getInputType() == InputType.TYPE_CLASS_PHONE) {
            if (userName.getText().toString().matches(MobilePattern)) {
                userName.setError(null);
                if (Validation.isPasswordValid(password.getText().toString())) {
                    password.setError(null);
                    login();
                } else {
                    password.setError(Validation.passwordError);
                    password.requestFocus();
                }
            } else {
                userName.setError(Validation.userNameMobileNoError);
                userName.requestFocus();
            }
        } else {
            if (Validation.isUserNameValid(userName.getText().toString())) {
                userName.setError(null);
                if (Validation.isPasswordValid(password.getText().toString())) {
                    password.setError(null);
                    login();
                } else {
                    password.setError(Validation.passwordError);
                    password.requestFocus();
                }
            } else {
                userName.setError(Validation.userNameError);
                userName.requestFocus();
            }
        }*/
    }

    private void login() {
        WebServiceClasses.getWebServiceClasses(LoginActivity.this, TAG).login(LoginActivity.this, userName.getText().toString().trim(), password.getText().toString().trim(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONObject jObject = response.getJSONObject("data");
                    CommonMethods.toast(LoginActivity.this, response.getString("message"));
                    PrefManager.getPrefManager(LoginActivity.this).setLoginDetails(jObject.getString("token"), jObject.getString("name"), jObject.getString("ping_location"), jObject.getString("driverid"), jObject.getString("gender"));
                    JSONObject vehicleDetail = jObject.getJSONObject("Vehicle");
                    PrefManager.getPrefManager(LoginActivity.this).setVehileDetails(vehicleDetail.getString("vehicle_make"), vehicleDetail.getString("vehicle_model"), vehicleDetail.getString("vehicle_registration_number"), vehicleDetail.getString("bike_photo"), vehicleDetail.getString("balance_status"), vehicleDetail.getString("balance_message"));
                    startService(new Intent(LoginActivity.this, RegistrationIntentService.class));
                    startActivity(new Intent(activity, GoOnline.class).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    CommonMethods.toast(LoginActivity.this, response.getString("message"));
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(LoginActivity.this,title,message,false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
