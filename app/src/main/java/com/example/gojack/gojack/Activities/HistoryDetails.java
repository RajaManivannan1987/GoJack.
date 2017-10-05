package com.example.gojack.gojack.Activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 9/2/2016.
 */
public class HistoryDetails extends CommonActionBar {
    private String TAG = "HistoryDetails";
    private HistoryDetails activity = HistoryDetails.this;
    private TextView detailFromLocationTextView, detailToLocationTextView, detailFinalRateTextView, detailpaymentTypeTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_history_details);
        detailFromLocationTextView = (TextView) findViewById(R.id.detailFromTextView);
        detailToLocationTextView = (TextView) findViewById(R.id.detailToLocationTextView);
        detailFinalRateTextView = (TextView) findViewById(R.id.detailFinalRateTextView);
        detailpaymentTypeTextView = (TextView) findViewById(R.id.detailpaymentTextView);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/rupee_foradian.ttf");
        detailFinalRateTextView.setTypeface(face);

        String rideId = getIntent().getExtras().getString("rideId");
        if (getIntent().getExtras().getString("rideId") != null) {
            loadData(rideId);
        }
    }

    private void loadData(String rideId) {
        WebServiceClasses.getWebServiceClasses(HistoryDetails.this, TAG).getHistoryDetails(rideId, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    detailFromLocationTextView.setText(jsonObject.getString("driver_s_address"));
                    detailToLocationTextView.setText(jsonObject.getString("driver_e_address"));
                    detailpaymentTypeTextView.setText(jsonObject.getString("payment_mode"));
                    detailFinalRateTextView.setText( "Rs " + jsonObject.getString("final_amount"));
                }
            }

            @Override
            public void onError(String message, String title) {
                CommonMethods.showSnakBar(message, detailFinalRateTextView);
            }
        });
    }

}
