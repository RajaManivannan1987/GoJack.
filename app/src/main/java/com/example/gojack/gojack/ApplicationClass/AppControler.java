package com.example.gojack.gojack.ApplicationClass;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.GCMClasses.RegistrationIntentService;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Font.FontsOverride;
import com.example.gojack.gojack.HelperClasses.InterNet.ConnectivityReceiver;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.ServiceClass.GPSTracker;
import com.example.gojack.gojack.HelperClasses.ServiceClass.MyLocation;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class AppControler extends MultiDexApplication {
    public static String deviceId = "";
    private static MyLocation location;
    private RequestQueue mRequestQueue;
    private static AppControler sInstance;

    public static MyLocation locationInstance() {
        return location;
    }

    public static void instanceLocation(Context context) {
        location = new MyLocation(context);
    }

    public AppControler() {
    }

    public static synchronized AppControler getsInstance() {
        return sInstance;
    }

    public void setConnectivitylistener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/roboto_regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/rupee_foradian.ttf");

//          31/3/2017
//        new GPSTracker();
        new CommonMethods();
        sInstance = this;
        instanceLocation(this);
//        updatePilotStatus();
        startService(new Intent(AppControler.this, RegistrationIntentService.class));
    }

    private void updatePilotStatus() {
        WebServiceClasses.getWebServiceClasses(this, "AppControler").getPilotStatus( new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1")&&response.getString("status").equalsIgnoreCase("1")){
                    startService(new Intent(AppControler.this, GPSTracker.class));
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }

    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public void addToRequestQueue(Request queue) {
        getmRequestQueue().add(queue);
    }
}
