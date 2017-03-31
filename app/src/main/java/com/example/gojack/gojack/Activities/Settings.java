package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.DialogBox.AlertDialogManager;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class Settings extends CommonNavigstionBar {
    private String TAG = "Settings";
    WebServiceClasses webServiceClasses;
    Button logoutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_settings);
        logoutButton = (Button) findViewById(R.id.logoutButton1);
        webServiceClasses = new WebServiceClasses(Settings.this, TAG);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebServiceClasses.getWebServiceClasses(Settings.this, TAG).checkRideStatus(new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("status").equalsIgnoreCase("0")) {
                            logout();
                        } else {
                            CommonMethods.toast(Settings.this, "Ride sitll not completed");
                        }
                    }

                    @Override
                    public void onError(String message, String title) {
                        CommonMethods.showSnakBar(message, logoutButton);
                    }
                });
            }
        });
    }

    private void logout() {
        webServiceClasses.logout(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1")) {
                    CommonMethods.toast(Settings.this, response.getString("message"));
                    stopService(setIntent(getBaseContext()));
                    PrefManager.getPrefManager(Settings.this).logout();
                    startActivity(new Intent(Settings.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }

            @Override
            public void onError(String message, String title) {
                CommonMethods.showSnakBar(message, logoutButton);
            }
        });
    }
}
