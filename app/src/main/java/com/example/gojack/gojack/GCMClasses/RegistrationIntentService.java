package com.example.gojack.gojack.GCMClasses;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM028 on 4/20/16.
 */

public class RegistrationIntentService extends IntentService {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String token = "";

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.google_app_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);
            //MyApplication.deviceId = token;
            SendDeviceidTOServer(AppControler.deviceId = token, this);
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.i(TAG, "GCM Registration Token: " + token);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString("SendBirdGCMToken", token);
        e.commit();
    }

    private void SendDeviceidTOServer(String deviceId, final Context context) {
        WebServiceClasses webServiceClasses = new WebServiceClasses(context, TAG);
        webServiceClasses.updateDeviceId(deviceId, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
               // stopService(context, RegistrationIntentService.class);
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }


}
