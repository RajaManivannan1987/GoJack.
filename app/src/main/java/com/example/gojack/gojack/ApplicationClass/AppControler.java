package com.example.gojack.gojack.ApplicationClass;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

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

    public static MyLocation locationInstance() {
        return location;
    }

    public static void instanceLocation(Context context) {
        location = new MyLocation(context);
    }

    public AppControler() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        new GPSTracker();
        new CommonMethods();
        startService(new Intent(AppControler.this, GPSTracker.class));
        startService(new Intent(AppControler.this, RegistrationIntentService.class));
    }
}
