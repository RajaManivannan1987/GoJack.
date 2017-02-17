package com.example.gojack.gojack.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class PrefManager {
    private static PrefManager prefManager = null;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor, vehicleEditor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "GoJack";
    private static final String IS_LOGIN = "IsLogIn";
    private static final String PILOT_ID = "pilot_id";
    private static final String PILOT_TOKEN = "pilot_token";
    private static final String PILOT_NAME = "pilot_name";
    private static final String PILOT_PHOTO = "pilot_photo";
    private static final String PILOT_LOCATION = "pilot_location";
    private static final String GENDER = "gender";
    private static final String VEHICLE_MAKE = "vehicle_make";
    private static final String VEHICLE_MODEL = "vehicle_model";
    private static final String VEHICLE_NUMBER = "vehicle_number";
    private static final String VEHICLE_PHOTO = "bike_photo";
    private static final String BALANCE_STATUS = "balance_status";
    private static final String BALANCE_MESSAGE = "balance_message";
    private static final String ISNOTIFY = "isNotify";

    public static PrefManager getPrefManager(Context context) {
        if (prefManager == null) {
            prefManager = new PrefManager(context);
        }
        return prefManager;
    }

    public PrefManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
        vehicleEditor = preferences.edit();
    }

    public void setLoginDetails(String token, String pilotName, String pilotlocation, String pilotId, String gender) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(PILOT_TOKEN, token);
        editor.putString(PILOT_NAME, pilotName);
        editor.putString(PILOT_LOCATION, pilotlocation);
        editor.putString(PILOT_ID, pilotId);
        editor.putString(GENDER, gender);
        editor.commit();
    }

    public void setVehileDetails(String make, String model, String num, String photo, String b_status, String b_message, String userPhoto) {
        vehicleEditor.putString(VEHICLE_MAKE, make);
        vehicleEditor.putString(VEHICLE_MODEL, model);
        vehicleEditor.putString(VEHICLE_NUMBER, num);
        vehicleEditor.putString(VEHICLE_PHOTO, photo);
        vehicleEditor.putString(BALANCE_STATUS, b_status);
        vehicleEditor.putString(BALANCE_MESSAGE, b_message);
        vehicleEditor.putString(PILOT_PHOTO, userPhoto);
        vehicleEditor.commit();
    }

    public boolean isLogin() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public String getPilotName() {
        return preferences.getString(PILOT_NAME, "");
    }

    public String getPilotPhoto() {
        return preferences.getString(PILOT_PHOTO, "");
    }

    public String getPilotToken() {
        return preferences.getString(PILOT_TOKEN, "");
    }

    public String getPilotLocation() {
        return preferences.getString(PILOT_LOCATION, "");
    }

    public String getGender() {
        return preferences.getString(GENDER, "");
    }

    public String getPilotId() {
        return preferences.getString(PILOT_ID, "");
    }

    public String getVehicleMake() {
        return preferences.getString(VEHICLE_MAKE, "");
    }

    public String getVehicleModel() {
        return preferences.getString(VEHICLE_MODEL, "");
    }

    public String getVehicleNumber() {
        return preferences.getString(VEHICLE_NUMBER, "");
    }

    public String getVehiclePhoto() {
        return preferences.getString(VEHICLE_PHOTO, "");
    }

    public String getBalanceStatus() {
        return preferences.getString(BALANCE_STATUS, "");
    }

    public String getBalanceMessage() {
        return preferences.getString(BALANCE_MESSAGE, "");
    }

    public void logout() {
        editor.clear();
        editor.commit();
        vehicleEditor.clear();
        vehicleEditor.commit();
    }


}
