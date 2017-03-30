package com.example.gojack.gojack.Activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.DialogBox.AlertDialogManager;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 9/2/2016.
 */
public class HistoryDetails extends CommonActionBar {
    private String TAG = "HistoryDetails";
    private HistoryDetails activity = HistoryDetails.this;
    //private WebServiceClasses webServiceClasses;
    private TextView detailFromLocationTextView, detailToLocationTextView, detailFinalRateTextView, detailpaymentTypeTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        setActionBar();
        findViewById();
        String rideId = getIntent().getExtras().getString("rideId");
        if (getIntent().getExtras().getString("rideId") != null) {
            loadData(rideId);
        }
    }

    private void loadData(String rideId) {
        final ProgressDialog progressBar = new ProgressDialog(HistoryDetails.this);
        progressBar.setMessage("Fetch data...");
        progressBar.setCancelable(false);
        progressBar.show();
        //webServiceClasses = new WebServiceClasses(activity, TAG);
        WebServiceClasses.getWebServiceClasses(HistoryDetails.this, TAG).getHistoryDetails(rideId, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                progressBar.dismiss();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    detailFromLocationTextView.setText(jsonObject.getString("driver_s_address"));
                    detailToLocationTextView.setText(jsonObject.getString("driver_e_address"));
                    detailpaymentTypeTextView.setText(jsonObject.getString("payment_mode"));
                    detailFinalRateTextView.setText(getResources().getString(R.string.rs) + " " + jsonObject.getString("final_amount"));
                }
            }

            @Override
            public void onError(String message, String title) {
                progressBar.dismiss();
                AlertDialogManager.showAlertDialog(HistoryDetails.this,title,message,false);
            }
        });
    }

    private void findViewById() {
        detailFromLocationTextView = (TextView) findViewById(R.id.detailFromLocationTextView);
        detailToLocationTextView = (TextView) findViewById(R.id.detailToLocationTextView);
        detailFinalRateTextView = (TextView) findViewById(R.id.detailFinalRateTextView);
        detailpaymentTypeTextView = (TextView) findViewById(R.id.detailpaymentTypeTextView);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/rupee_foradian.ttf");
        detailFinalRateTextView.setTypeface(face);
    }
}
