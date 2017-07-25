package com.example.gojack.gojack.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.gojack.gojack.AdapterClasses.HistoryAdapter;
import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.ModelClasses.HistoryModel;
import com.example.gojack.gojack.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class History extends CommonNavigstionBar {
    private String TAG = "";
    private History activity = History.this;
    private RecyclerView history_RecyclerView;
    private HistoryAdapter adapter;
    //private WebServiceClasses webServiceClasses;
    private ArrayList<HistoryModel> historyLlist = new ArrayList<>();
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_history);
//        setContentView(R.layout.activity_history);
//        setActionBar();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHistoryList();
        AppControler.getsInstance().setConnectivitylistener(this);
    }

    private void loadData() {
        //webServiceClasses = new WebServiceClasses(activity, TAG);
        history_RecyclerView = (RecyclerView) findViewById(R.id.history_RecyclerView);
        history_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(activity, historyLlist);
        history_RecyclerView.setAdapter(adapter);
    }

    private void getHistoryList() {
        final ProgressDialog progressBar = new ProgressDialog(History.this);
        progressBar.setMessage("Fetch data...");
        progressBar.setCancelable(false);
        progressBar.show();
        WebServiceClasses.getWebServiceClasses(History.this, TAG).getHistoryList(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                progressBar.dismiss();
                historyLlist.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                       /* JSONObject object = jsonArray.getJSONObject(i);
                        HistoryModel setValues = new HistoryModel();
                        setValues.setRide_id(object.getString("ride_id"));
                        setValues.setDate_time(object.getString("date_time"));
                        setValues.setDriver_s_address(object.getString("driver_s_address"));
                        setValues.setDriver_e_address(object.getString("driver_e_address"));
                        setValues.setFinal_amount(object.getString("final_amount"));
                        setValues.setRide_type(object.getString("ride_type"));
                        historyLlist.add(setValues);*/
                        historyLlist.add(gson.fromJson(response.getJSONArray("data").getJSONObject(i).toString(),HistoryModel.class));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message, String title) {
                progressBar.dismiss();
                CommonMethods.showSnakBar(message, history_RecyclerView);
            }
        });
    }
}
