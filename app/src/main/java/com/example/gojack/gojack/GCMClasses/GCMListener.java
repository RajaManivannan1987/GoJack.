package com.example.gojack.gojack.GCMClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.example.gojack.gojack.Activities.NotificationAlertActivity;
import com.example.gojack.gojack.HelperClasses.CommonIntent;
import com.example.gojack.gojack.HelperClasses.PrefManager;
import com.example.gojack.gojack.R;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.Calendar;

/**
 * Created by IM028 on 4/20/16.
 */

public class GCMListener extends GcmListenerService {

    private static final String TAG = "GCMListener";
   // private PrefManager prefManager;


    String senderUUID = "", senderName = "";
    SpannableStringBuilder ssString;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message", "");
        String type = data.getString("type", "");
        String rideId = data.getString("rideid", "");
        String gender = data.getString("gender", "");

        //prefManager = new PrefManager(this);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "type: " + type);
        Log.d(TAG, "rideid: " + rideId);
        Log.d(TAG, "gender: " + gender);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
        if (PrefManager.getPrefManager(GCMListener.this).isLogin()) {
            sendNotification(message, type, rideId, gender);
        }
        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String type, String rideId, String gender) {
        newOrderNotification(message, type, rideId, gender);
    }

    private void newOrderNotification(String message, String type, String rideid, String gender) {
//        if (type.startsWith("ridecancelledbycustomer")) {
            //ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted1();
//        } else {
            Intent intent = new Intent(this, NotificationAlertActivity.class);
            intent.putExtra(CommonIntent.typeKey, type);
            intent.putExtra(CommonIntent.rideId, rideid);
            intent.putExtra(CommonIntent.gender, gender);
            intent.putExtra(CommonIntent.message, message);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    (int) Calendar.getInstance().getTimeInMillis() /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.male_pilot_icon)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) Calendar.getInstance().getTimeInMillis() /* ID of notification */, notificationBuilder.build());
//        }

    }
}
