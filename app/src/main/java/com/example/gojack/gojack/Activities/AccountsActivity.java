package com.example.gojack.gojack.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.gojack.gojack.AdapterClasses.AccountAdapter;
import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.ModelClasses.AccountsModel;
import com.example.gojack.gojack.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class AccountsActivity extends CommonNavigstionBar {
    private String TAG = "AccountsActivity";
    private TextView amountTextView, tripstTextView, commissionTextView;
    private RecyclerView acountRecyclerView;
    private AccountAdapter adapter;
    private ArrayList<AccountsModel> list = new ArrayList<>();
    private WebServiceClasses webServiceClasses;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_account);
        webServiceClasses = new WebServiceClasses(AccountsActivity.this, TAG);
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        tripstTextView = (TextView) findViewById(R.id.tripstTextView);
        commissionTextView = (TextView) findViewById(R.id.commissionTextView);
        acountRecyclerView = (RecyclerView) findViewById(R.id.acountRecyclerView);
        acountRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountAdapter(AccountsActivity.this, list);
        acountRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTodayAccount();
    }

    private void getTodayAccount() {
        webServiceClasses.getTodayDetails(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1")) {
                    if (response.getString("status").equalsIgnoreCase("1")) {
                        if (response.getJSONObject("data").getString("amount").equalsIgnoreCase("null")) {
                            amountTextView.setText("0.0");
                        } else {
                            amountTextView.setText(response.getJSONObject("data").getString("amount"));
                        }
                        tripstTextView.setText(response.getJSONObject("data").getString("total_rides") + " Rides");
                        commissionTextView.setText("Commission: " + response.getJSONObject("data").getString("commission"));

                        getTables();
                    } else {
                        CommonMethods.toast(AccountsActivity.this, response.getString("message"));
                    }
                }
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    private void getTables() {
        webServiceClasses.getTableDetails(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                list.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                        list.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(), AccountsModel.class));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }
}
