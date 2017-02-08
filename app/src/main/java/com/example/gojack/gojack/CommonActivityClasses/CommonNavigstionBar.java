package com.example.gojack.gojack.CommonActivityClasses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.gojack.gojack.Activities.About;
import com.example.gojack.gojack.Activities.Account;
import com.example.gojack.gojack.Activities.EndTripDetailActivity;
import com.example.gojack.gojack.Activities.GoOffline;
import com.example.gojack.gojack.Activities.GoOnline;
import com.example.gojack.gojack.Activities.Help;
import com.example.gojack.gojack.Activities.History;
import com.example.gojack.gojack.Activities.LoginActivity;
import com.example.gojack.gojack.Activities.Settings;
import com.example.gojack.gojack.AdapterClasses.NavigationBarAdapter;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.NotifyCustomerSingleton;
import com.example.gojack.gojack.HelperClasses.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;
import com.example.gojack.gojack.ServiceClass.LocationService;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class CommonNavigstionBar extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private String TAG = "CommonNavigstionBar";
    private static Intent locationIntent;
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private TextView statusTextView;
    public ImageView navigationIcon, sosIcon;
    private NavigationBarAdapter adapter;
    private View headerView, footerView;
    //private PrefManager prefManager;
    private Switch goOfflineSwitch;
    private WebServiceClasses webServiceClasses;

    public static Intent setIntent(Context context) {
        if (locationIntent == null) {
            locationIntent = new Intent(context, LocationService.class);
        }
        return locationIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationbar_activity);
        webServiceClasses = new WebServiceClasses(CommonNavigstionBar.this, TAG);
        //prefManager = new PrefManager(CommonNavigstionBar.this);
        adapter = new NavigationBarAdapter(this);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        navigationIcon = (ImageView) toolbar.findViewById(R.id.navigationIcon);
        sosIcon = (ImageView) findViewById(R.id.sosIcon);
        navigationIcon.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        setNavigationBar();
    }

    private void setNavigationBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.actionBarViewListView);
        headerView = getLayoutInflater().inflate(R.layout.navigation_header, null, false);
        statusTextView = (TextView) headerView.findViewById(R.id.statusTextView);
        goOfflineSwitch = (Switch) headerView.findViewById(R.id.goOfflineSwitch);
        goOfflineSwitch.setOnCheckedChangeListener(this);
        //checkStatus();
        footerView = getLayoutInflater().inflate(R.layout.navigation_footer, null, false);
        footerView.findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webServiceClasses.logout(new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("token_status").equalsIgnoreCase("1")) {
                            CommonMethods.toast(CommonNavigstionBar.this, response.getString("message"));
                            PrefManager.getPrefManager(CommonNavigstionBar.this).logout();
                            startActivity(new Intent(CommonNavigstionBar.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }

                    @Override
                    public void onError(String message, String title) {

                    }
                });
            }
        });
        listView.addFooterView(footerView);
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
        listView.setAnimation(new AnimationSet(true));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0 && position != listView.getCount() - 1) {
                    TextView textView = (TextView) view.findViewById(R.id.navigationBarListTextView);
                    switch (textView.getText().toString().trim()) {
                        case "GO OFFLINE":
                            checkRideStatus();
                            break;
                        case "DASHBOARD":
                            startActivity(new Intent(CommonNavigstionBar.this, GoOffline.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "HISTORY":
                            startActivity(new Intent(CommonNavigstionBar.this, History.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "ACCOUNTS":
                            startActivity(new Intent(CommonNavigstionBar.this, Account.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "HELP":
                            startActivity(new Intent(CommonNavigstionBar.this, Help.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "SETTINGS":
                            startActivity(new Intent(CommonNavigstionBar.this, Settings.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "ABOUT":
                            startActivity(new Intent(CommonNavigstionBar.this, About.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                    }
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }
                }
            }
        });
        sosIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.callFunction(CommonNavigstionBar.this, "+91- 111-111-1111");
            }
        });
    }

    private void checkRideStatus() {
        WebServiceClasses.getWebServiceClasses(CommonNavigstionBar.this, TAG).checkRideStatus(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
//                JSONObject jsonObject = response.getJSONObject("data");
                if (response.getString("status").equalsIgnoreCase("0")) {
                    updatePilotStatus();
                    stopService(setIntent(getBaseContext()));
                    startActivity(new Intent(CommonNavigstionBar.this, GoOnline.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                }  else {
                    CommonMethods.toast(CommonNavigstionBar.this, "Ride sitll not completed so won't go to offline");
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    public void setView(int layoutid) {
        frameLayout = (FrameLayout) findViewById(R.id.contentView);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutid, null, false);
        frameLayout.addView(view);
    }

    private void updatePilotStatus() {
        WebServiceClasses.getWebServiceClasses(CommonNavigstionBar.this, TAG).updateStatus("0", new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigationIcon:
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.openDrawer(Gravity.LEFT);
                else
                    drawerLayout.closeDrawer(Gravity.LEFT);
                adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
           /* stopService(setIntent(getBaseContext()));
            startService(setIntent(getBaseContext()));
            updateStatus("1");*/
        } else {
            /*stopService(setIntent(getBaseContext()));
            updateStatus("0");*/
        }

    }

    private void updateStatus(String status) {
        webServiceClasses.updateStatus(status, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
