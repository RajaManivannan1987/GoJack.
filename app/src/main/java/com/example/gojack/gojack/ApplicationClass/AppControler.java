package com.example.gojack.gojack.ApplicationClass;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.gojack.gojack.GCMClasses.RegistrationIntentService;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.ServiceClass.GPSTracker;
import com.example.gojack.gojack.ServiceClass.MyLocation;

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

    @Override
    public void onCreate() {
        super.onCreate();
        new GPSTracker();
        new CommonMethods();
        sInstance = this;
        instanceLocation(this);
        startService(new Intent(AppControler.this, GPSTracker.class));
        startService(new Intent(AppControler.this, RegistrationIntentService.class));
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
