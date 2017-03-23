package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.R;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class Splash extends AppCompatActivity {
    private static int SLEEP_TIME = 3000;
    //private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //prefManager = new PrefManager(Splash.this);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(SLEEP_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (PrefManager.getPrefManager(Splash.this).isLogin()) {
                        startActivity(new Intent(Splash.this, GoOffline.class));
                        finish();
                    } else {
                        startActivity(new Intent(Splash.this, LoginActivity.class));
                        finish();
                    }

                }
            }
        };
        thread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
