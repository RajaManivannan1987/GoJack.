package com.example.gojack.gojack.Interface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/2/2016.
 */
public interface VolleyResponseListerner {
    void onResponse(JSONObject response) throws JSONException;

    void onError(String message, String title);
}
