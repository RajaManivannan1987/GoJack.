package com.example.gojack.gojack.CommonActivityClasses;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.InterNet.ConnectivityReceiver;
import com.example.gojack.gojack.R;

/**
 * Created by IM0033 on 8/3/2016.
 */
public class CommonActionBar extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private ImageView menuImageView;
    private FrameLayout backActivityFrameLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_actionbar);

        toolbar = (Toolbar) findViewById(R.id.commonMenuActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        backActivityFrameLayout = (FrameLayout) findViewById(R.id.backActivityFrameLayout);
        menuImageView = (ImageView) findViewById(R.id.menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.hideKeyboard(CommonActionBar.this, v);
                onBackPressed();
            }
        });
        // Toolbar toolbar = (Toolbar) findViewById(R.id.commonToolBar);
        // setSupportActionBar(toolbar);
    }

    public void setView(int viewLayout) {
        frameLayout = (FrameLayout) findViewById(R.id.commonMenuActivityFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(viewLayout, null, false);
        frameLayout.addView(activityView);
    }
   /* public void setActionBar() {
        LayoutInflater inflater = LayoutInflater.from(CommonActionBar.this);
        v = inflater.inflate(R.layout.common_actionbar, null, false);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        getSupportActionBar().setCustomView(v);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }*/

    /* @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case android.R.id.home:
                 this.finish();
         }
         return true;
     }
 */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

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
        try {
            CommonMethods.showSnakBar(message, frameLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppControler.getsInstance().setConnectivitylistener(this);
    }
}
