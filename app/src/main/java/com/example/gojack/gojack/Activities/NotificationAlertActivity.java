package com.example.gojack.gojack.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gojack.gojack.HelperClasses.ActionCompletedSingleton;
import com.example.gojack.gojack.HelperClasses.CommonIntent;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.NotifyCustomerSingleton;
import com.example.gojack.gojack.HelperClasses.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by IM0033 on 8/8/2016.
 */
public class NotificationAlertActivity extends Activity {
    private String TAG = "NotificationAlertActivity";
    private CircleImageView notificationGenderImageView;
    private Button notificationAcceptButton, notificationCancelButton, notifyCustomerButton;
    // private WebServiceClasses webServiceClasses;
    private String rideId, gender, notifiyType;
    private LinearLayout buttonMainLayout;
    private TextView messageTextView;
    //private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_alert);
        // prefManager = new PrefManager(NotificationAlertActivity.this);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        // webServiceClasses = new WebServiceClasses(NotificationAlertActivity.this, TAG);
        notificationAcceptButton = (Button) findViewById(R.id.notificationAcceptButton);
        notificationCancelButton = (Button) findViewById(R.id.notificationCancelButton);
        notificationGenderImageView = (CircleImageView) findViewById(R.id.notificationGenderImageView);
        buttonMainLayout = (LinearLayout) findViewById(R.id.buttonMainLayout);
        notifyCustomerButton = (Button) findViewById(R.id.notifyCustomerButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            messageTextView.setText(bundle.getString(CommonIntent.message, ""));
            rideId = bundle.getString(CommonIntent.rideId, "");
            gender = bundle.getString(CommonIntent.gender, "");
            notifiyType = bundle.getString(CommonIntent.typeKey, "");
            if (notifiyType.startsWith("riderequest")) {
                notificationAcceptButton.setText("Accept");
            } else if (notifiyType.startsWith("reachedlocation")) {
                notifyCustomerButton.setVisibility(View.VISIBLE);
                buttonMainLayout.setVisibility(View.GONE);
//                ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();
                NotifyCustomerSingleton.actionCompletedSingleton().ActionCompleted();
            } else if (notifiyType.startsWith("ridecancelledbycustomer")) {
                notificationAcceptButton.setText("Ok");
                notificationCancelButton.setVisibility(View.GONE);
            } else {
                notificationAcceptButton.setText("Ok");
                notificationCancelButton.setVisibility(View.GONE);
            }

        }

        if (gender.equalsIgnoreCase("male")) {
            notificationGenderImageView.setImageResource(R.drawable.male_icon);
        } else {
            notificationGenderImageView.setImageResource(R.drawable.female_icon);
        }
        notificationCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptAndCancelRide("0");
            }
        });
        notifyCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebServiceClasses.getWebServiceClasses(NotificationAlertActivity.this, TAG).notifyCustomer(rideId, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getString("token_status").equalsIgnoreCase("1")) {
//                            ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();
                            NotifyCustomerSingleton.actionCompletedSingleton().ActionCompleted();
                            CommonMethods.toast(NotificationAlertActivity.this, response.getString("message"));
                            finish();
                        } else {
                            CommonMethods.toast(NotificationAlertActivity.this, response.getString("message"));
                            finish();
                        }
                    }

                    @Override
                    public void onError(String message, String title) {

                    }
                });

            }
        });
        notificationAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notifiyType.startsWith("riderequest")) {
                    acceptAndCancelRide("1");
                } else if (notifiyType.startsWith("ridecancelledbycustomer")) {
                    ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();
                    finish();
                } else {
                    NotificationAlertActivity.this.finish();
                }
            }
        });
    }

    private void acceptAndCancelRide(String type) {
        WebServiceClasses.getWebServiceClasses(NotificationAlertActivity.this, TAG).acceptRequest(rideId, type, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1")) {
                    if (response.getString("status").equalsIgnoreCase("1")) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        Intent i = new Intent(getApplicationContext(), GoOffline.class);
                        i.putExtra("riderName", jsonObject.getString("name"));
                        i.putExtra("gender", jsonObject.getString("gender"));
                        i.putExtra("p_num", jsonObject.getString("mobilenumber"));
                        i.putExtra("start_lat", jsonObject.getString("startinglatitude"));
                        i.putExtra("start_lang", jsonObject.getString("startinglongitude"));
                        i.putExtra("end_lat", jsonObject.getString("endinglatitude"));
                        i.putExtra("end_lang", jsonObject.getString("endinglongitude"));
                        i.putExtra("rideId", rideId);
                        i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(i);
                        NotificationAlertActivity.this.finish();
                    } else if (response.getString("status").equalsIgnoreCase("2")) {
                        CommonMethods.toast(NotificationAlertActivity.this, response.getString("message"));
                        NotificationAlertActivity.this.finish();
                    } else if (response.getString("status").equalsIgnoreCase("0")) {
                        CommonMethods.toast(NotificationAlertActivity.this, response.getString("message"));
                        NotificationAlertActivity.this.finish();
                    } else {
                        NotificationAlertActivity.this.finish();
                    }
                } else {
                    CommonMethods.toast(NotificationAlertActivity.this, response.getString("token_message"));
                    PrefManager.getPrefManager(NotificationAlertActivity.this).logout();
                    startActivity(new Intent(NotificationAlertActivity.this, LoginActivity.class));
                }

            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
