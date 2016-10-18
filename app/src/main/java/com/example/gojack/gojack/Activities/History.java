package com.example.gojack.gojack.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.gojack.gojack.AdapterClasses.HistoryAdapter;
import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.ModelClasses.HistoryModel;
import com.example.gojack.gojack.R;

import org.json.JSONArray;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_history);
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHistoryList();
    }

    private void loadData() {
        //webServiceClasses = new WebServiceClasses(activity, TAG);
        history_RecyclerView = (RecyclerView) findViewById(R.id.history_RecyclerView);
        history_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(activity, historyLlist);
        history_RecyclerView.setAdapter(adapter);
    }

    private void getHistoryList() {
        WebServiceClasses.getWebServiceClasses(History.this, TAG).getHistoryList(new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                historyLlist.clear();
                if (response.getString("status").equalsIgnoreCase("1")) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        HistoryModel setValues = new HistoryModel();
                        setValues.setRide_id(object.getString("ride_id"));
                        setValues.setDate_time(object.getString("date_time"));
                        setValues.setStarting_address(object.getString("starting_address"));
                        setValues.setEnding_address(object.getString("ending_address"));
                        setValues.setFinal_amount(object.getString("final_amount"));
                        setValues.setRide_type(object.getString("ride_type"));
                        historyLlist.add(setValues);
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
