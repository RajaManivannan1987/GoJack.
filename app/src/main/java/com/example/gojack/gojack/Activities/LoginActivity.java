package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.GCMClasses.RegistrationIntentService;
import com.example.gojack.gojack.HelperClasses.DialogBox.AlertDialogManager;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.InterNet.ConnectivityReceiver;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.Validate.Validation;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private String TAG = "LoginActivity";
    private LoginActivity activity = LoginActivity.this;
    private EditText userName, password;
    private Button submitButton;
    private TextInputLayout passwordTextInputLayout;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                userName.setText("");
                password.setText("");
                CommonMethods.hideKeyboard(LoginActivity.this, view);
                startActivity(new Intent(activity, ForgotPassword.class));
            }
        });
    }

    private void findViewById() {
        scrollView = (ScrollView) findViewById(R.id.loginScrollView);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);
        userName = (EditText) findViewById(R.id.userNameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        submitButton = (Button) findViewById(R.id.loginButton);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);
    }

    private void loginValidate() {
        if (Validation.emailPhoneValidation(userName.getText().toString().trim()).equalsIgnoreCase("email") || Validation.emailPhoneValidation(userName.getText().toString().trim()).equalsIgnoreCase("phone")) {
            userName.setError(null);
            if (Validation.isPasswordEmpty(password.getText().toString().trim())) {
                password.setError(null);
                if (Validation.isPasswordValid(password.getText().toString().trim())) {
                    password.setError(null);
                    login();
                } else {
                    passwordTextInputLayout.setError(Validation.passwordError);
                    password.requestFocus();
                }
            } else {
                passwordTextInputLayout.setError(Validation.passwordEmptyMessage);
                password.requestFocus();
            }
        } else {
            userName.setError(Validation.emailPhoneValidation(userName.getText().toString()));
            userName.requestFocus();
        }
    }

    private void login() {
        WebServiceClasses.getWebServiceClasses(LoginActivity.this, TAG).login(LoginActivity.this, userName.getText().toString().trim(), password.getText().toString().trim(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONObject jObject = response.getJSONObject("data");
                    CommonMethods.showSnakBar(response.getString("message"), userName);
                    PrefManager.getPrefManager(LoginActivity.this).setLoginDetails(jObject.getString("token"), jObject.getString("name"), jObject.getString("ping_location"), jObject.getString("driverid"), jObject.getString("gender"), jObject.getString("paytm_token"));
                    JSONObject vehicleDetail = jObject.getJSONObject("Vehicle");
                    PrefManager.getPrefManager(LoginActivity.this).setVehileDetails(vehicleDetail.getString("vehicle_make"), vehicleDetail.getString("vehicle_model"), vehicleDetail.getString("vehicle_registration_number"), vehicleDetail.getString("bike_photo"), vehicleDetail.getString("balance_status"), vehicleDetail.getString("balance_message"), vehicleDetail.getString("photo"));
                    userName.setText("");
                    password.setText("");
                    startService(new Intent(LoginActivity.this, RegistrationIntentService.class));
                    startActivity(new Intent(activity, GoOnline.class).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    CommonMethods.hideKeyboard(LoginActivity.this, userName);
                    finish();
                } else {
                    CommonMethods.showSnakBar(response.getString("message"), userName);
                }
            }

            @Override
            public void onError(String message, String title) {
                CommonMethods.showSnakBar(message, userName);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonMethods.closeIntent(LoginActivity.this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnake(isConnected);
    }

    private void showSnake(boolean isConnected) {
        String message = null;

        if (isConnected) {
            message = "Good! Connected to Internet";
        } else {
            message = "Sorry! Not connected to internet";
        }
        CommonMethods.showSnakBar(message, userName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppControler.getsInstance().setConnectivitylistener(this);
    }
}
