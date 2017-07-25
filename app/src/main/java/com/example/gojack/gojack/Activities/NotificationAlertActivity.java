package com.example.gojack.gojack.Activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gojack.gojack.HelperClasses.Interface.ActionCompleted;
import com.example.gojack.gojack.HelperClasses.Singleton.ActionCompletedSingleton;
import com.example.gojack.gojack.HelperClasses.DialogBox.AlertDialogManager;
import com.example.gojack.gojack.HelperClasses.Common.CommonIntent;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Singleton.NotifyCustomerSingleton;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
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
    NotificationManager nMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_alert);
        nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // prefManager = new PrefManager(NotificationAlertActivity.this);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        // webServiceClasses = new WebServiceClasses(NotificationAlertActivity.this, TAG);
        notificationAcceptButton = (Button) findViewById(R.id.notificationAcceptButton);
        notificationCancelButton = (Button) findViewById(R.id.notificationCancelButton);
        notificationGenderImageView = (CircleImageView) findViewById(R.id.notificationGenderImageView);
        buttonMainLayout = (LinearLayout) findViewById(R.id.buttonMainLayout);
        notifyCustomerButton = (Button) findViewById(R.id.notifyCustomerButton);
        NotifyCustomerSingleton.closActivity().setListener(new ActionCompleted() {
            @Override
            public void actionCompleted() {
                nMgr.cancelAll();
                CommonMethods.closeIntent(NotificationAlertActivity.this);
                finish();
            }
        });
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
//                finish();
            } else if (notifiyType.startsWith("ridecancelledbycustomer")) {
                notificationAcceptButton.setText("Ok");
                notificationCancelButton.setVisibility(View.GONE);
//                NotifyCustomerSingleton.actionCanceled().ActionCompleted();
            } else if (notifiyType.startsWith("ridetaken")) {
//                notificationAcceptButton.setText("Ok");
//                notificationCancelButton.setVisibility(View.GONE);
                nMgr.cancelAll();
//                NotifyCustomerSingleton.actionCanceled().ActionCompleted();
            } else {
                notificationAcceptButton.setText("Ok");
                notificationCancelButton.setVisibility(View.GONE);
//                nMgr.cancelAll();
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
                nMgr.cancelAll();
            }
        });
        notifyCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressBar = new ProgressDialog(NotificationAlertActivity.this);
                progressBar.setMessage("Fetch data...");
                progressBar.setCancelable(false);
                progressBar.show();
                WebServiceClasses.getWebServiceClasses(NotificationAlertActivity.this, TAG).notifyCustomer(rideId, new VolleyResponseListerner() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        progressBar.dismiss();
                        nMgr.cancelAll();
                        if (response.getString("token_status").equalsIgnoreCase("1")) {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                NotifyCustomerSingleton.actionCompletedSingleton().ActionCompleted();
                                CommonMethods.toast(NotificationAlertActivity.this, response.getString("message"));
                                finish();
                            } else {
                                ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();
                                CommonMethods.toast(NotificationAlertActivity.this, response.getString("message"));
                                finish();
                            }

                        } else {
                            ActionCompletedSingleton.actionCompletedSingleton().ActionCompleted();
                            CommonMethods.toast(NotificationAlertActivity.this, response.getString("message"));
                            finish();
                        }
                    }

                    @Override
                    public void onError(String message, String title) {
                        progressBar.dismiss();
                        CommonMethods.showSnakBar(message, messageTextView);
                    }
                });

            }
        });
        notificationAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nMgr.cancelAll();
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
//                        ActionCompletedSingleton.getHideHailSingleton().ActionCompleted();
                        JSONObject jsonObject = response.getJSONObject("data");
                        Intent i = new Intent(getApplicationContext(), GoOffline.class);
                        i.putExtra("riderName", jsonObject.getString("name"));
                        i.putExtra("gender", jsonObject.getString("gender"));
                        i.putExtra("p_num", jsonObject.getString("mobilenumber"));
                        i.putExtra("address", jsonObject.getString("address"));
                        i.putExtra("start_lat", jsonObject.getString("startinglatitude"));
                        i.putExtra("start_lang", jsonObject.getString("startinglongitude"));
                        i.putExtra("end_lat", jsonObject.getString("endinglatitude"));
                        i.putExtra("end_lang", jsonObject.getString("endinglongitude"));
                        i.putExtra("rideId", rideId);
                        i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        NotificationAlertActivity.this.finish();
                        callAccept(rideId);
                    } else if (response.getString("status").equalsIgnoreCase("2")) {
                        CommonMethods.toast(NotificationAlertActivity.this, response.getString("message"));
//                        CommonMethods.closeIntent(NotificationAlertActivity.this);
                        finish();
                    } else if (response.getString("status").equalsIgnoreCase("0")) {
                        CommonMethods.toast(NotificationAlertActivity.this, response.getString("message"));
//                        CommonMethods.closeIntent(NotificationAlertActivity.this);
//                        closeIntent();
                        finish();
                    } else {
                        finish();
//                        CommonMethods.closeIntent(NotificationAlertActivity.this);
                    }
                } else {
                    CommonMethods.toast(NotificationAlertActivity.this, response.getString("token_message"));
                    PrefManager.getPrefManager(NotificationAlertActivity.this).logout();
                    startActivity(new Intent(NotificationAlertActivity.this, LoginActivity.class));
                }

            }

            @Override
            public void onError(String message, String title) {
                CommonMethods.showSnakBar(message, messageTextView);
            }
        });
    }

    private void callAccept(String rideId) {
        WebServiceClasses.getWebServiceClasses(NotificationAlertActivity.this, TAG).callAcceptOnly(rideId, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
            }

            @Override
            public void onError(String message, String title) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
