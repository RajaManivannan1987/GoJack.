package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class Settings extends CommonNavigstionBar {
    private String TAG = "Settings";
    WebServiceClasses webServiceClasses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_settings);
        webServiceClasses = new WebServiceClasses(Settings.this, TAG);
        findViewById(R.id.logoutButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webServiceClasses.logout(new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("token_status").equalsIgnoreCase("1")) {
                            CommonMethods.toast(Settings.this, response.getString("message"));
                            PrefManager.getPrefManager(Settings.this).logout();
                            startActivity(new Intent(Settings.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }

                    @Override
                    public void onError(String message, String title) {

                    }
                });
            }
        });
    }
}
