package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.ServiceClass.GPSTracker;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class Splash extends AppCompatActivity {
    private static int SLEEP_TIME = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        updatePilotStatus();
       /* Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(SLEEP_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (PrefManager.getPrefManager(Splash.this).isLogin()) {
                        if (!PrefManager.getPrefManager(Splash.this).getPilotPaytmtoken().equalsIgnoreCase("")) {
                            startActivity(new Intent(Splash.this, CheckBalanceActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(Splash.this, PaytmLogin.class));
                            finish();
                        }

                    } else {
                        startActivity(new Intent(Splash.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        };
        thread.start();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void updatePilotStatus() {
        WebServiceClasses.getWebServiceClasses(this, "AppControler").getPilotStatus(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1") && response.getString("status").equalsIgnoreCase("1")) {
                    startService(new Intent(Splash.this, GPSTracker.class));
                }
                if (PrefManager.getPrefManager(Splash.this).isLogin()) {
                    if (!PrefManager.getPrefManager(Splash.this).getPilotPaytmtoken().equalsIgnoreCase("")) {
                        startActivity(new Intent(Splash.this, CheckBalanceActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(Splash.this, PaytmLogin.class));
                        finish();
                    }

                } else {
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                    finish();
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }
}
