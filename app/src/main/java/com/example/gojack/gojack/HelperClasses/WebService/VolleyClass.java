package com.example.gojack.gojack.HelperClasses.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gojack.gojack.Activities.LoginActivity;
import com.example.gojack.gojack.ApplicationClass.AppControler;
import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class VolleyClass {
    private Context context;
    private String TAG = "";
    // private PrefManager prefManager;
    String networkErrorMessage = "Network error – please try again.";
    String poorNetwork = "Your data connection is too slow – please try again when you have a better network connection";
    String timeout = "Response timed out – please try again.";
    String authorizationFailed = "Authorization failed – please try again.";
    String serverNotResponding = "Server not responding – please try again.";
    String parseError = "Data not received from server – please check your network connection.";

    String networkErrorTitle = "Network error";
    String poorNetworkTitle = "Poor Network Connection";
    String timeoutTitle = "Network Error";
    String authorizationFailedTitle = "Network Error";
    String serverNotRespondingTitle = "Server Error";
    String parseErrorTitle = "Network Error";

    public VolleyClass(Context context, String tag) {
        this.context = context;
        this.TAG = tag + " VolleyClass";
        // prefManager = new PrefManager(context);
    }

    public void volleyPostData(final String url, JSONObject jsonObject, final Activity context, final VolleyResponseListerner listerner) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        Log.d(TAG, "volleyPostData  url - " + url);
        Log.d(TAG, "volleyPostData  data - " + jsonObject.toString());
        if (isOnLline()) {
            if (pDialog != null) {
//                if (!context.isFinishing())
                try {
                    pDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            //RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostData  response - " + response.toString());
                            try {
                                if (response.has("token_status")) {
                                    if (response.getString("token_status").equalsIgnoreCase("1")) {
                                        listerner.onResponse(response);
                                    } else {
                                        PrefManager.getPrefManager(context).logout();
                                        CommonMethods.logoutIntent(context, LoginActivity.class);
                                        CommonMethods.toast(context, response.getString("message"));
                                    }
                                } else {
                                    listerner.onResponse(response);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (pDialog != null) {
                                try {
                                    pDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (error instanceof TimeoutError) {
                        listerner.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listerner.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listerner.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listerner.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listerner.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listerner.onError(parseError, parseErrorTitle);
                    }

                }
            });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppControler.getsInstance().addToRequestQueue(jsonRequest);
        } else {
            Log.d(TAG, "volleyPostData response - No Internet");
            listerner.onError(parseError, parseErrorTitle);
        }
    }

    public void volleyNoProgressPostData(final String url, JSONObject jsonObject, final VolleyResponseListerner listerner) {
        Log.d(TAG, "volleyPostData  url - " + url);
        Log.d(TAG, "volleyPostData  data - " + jsonObject.toString());
        if (isOnLline()) {
//            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostData  response - " + response.toString());
                            try {
                                if (response.has("token_status")) {
                                    if (response.getString("token_status").equalsIgnoreCase("1")) {
                                        listerner.onResponse(response);
                                    } else {
                                        PrefManager.getPrefManager(context).logout();
                                        CommonMethods.logoutIntent(context, LoginActivity.class);
                                        CommonMethods.toast(context, response.getString("message"));
                                    }
                                } else {
                                    listerner.onResponse(response);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof TimeoutError) {
                        listerner.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listerner.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listerner.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listerner.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listerner.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listerner.onError(parseError, parseErrorTitle);
                    }

                }
            });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(jsonRequest);
            AppControler.getsInstance().addToRequestQueue(jsonRequest);
        } else {
            Log.d(TAG, "volleyPostData response - No Internet");
            listerner.onError(parseError, parseErrorTitle);
        }
    }

    public void volleyNoProgressGet(final String url, final VolleyResponseListerner listerner) {
        Log.d(TAG, "volleyPostData  url - " + url);

        if (isOnLline()) {
//            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostData  response - " + response.toString());
                            try {
                                if (response.has("token_status")) {
                                    if (response.getString("token_status").equalsIgnoreCase("1")) {
                                        listerner.onResponse(response);
                                    } else {
                                        PrefManager.getPrefManager(context).logout();
                                        CommonMethods.logoutIntent(context, LoginActivity.class);
                                        CommonMethods.toast(context, response.getString("message"));
                                    }
                                } else {
                                    listerner.onResponse(response);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof TimeoutError) {
                        listerner.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listerner.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listerner.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listerner.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listerner.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listerner.onError(parseError, parseErrorTitle);
                    }

                }
            });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(jsonRequest);
            AppControler.getsInstance().addToRequestQueue(jsonRequest);
        } else {
            Log.d(TAG, "volleyPostData response - No Internet");
            listerner.onError(parseError, parseErrorTitle);
        }
    }

    public void volleyUpdateLocation(final String url, JSONObject jsonObject, final VolleyResponseListerner listerner) {
        Log.d(TAG, "volleyPostData  url - " + url);
        Log.d(TAG, "volleyPostData  data - " + jsonObject.toString());
        if (isOnLline()) {
//            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostData  response - " + response.toString());

                            try {
                                listerner.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof TimeoutError) {
                        listerner.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listerner.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listerner.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listerner.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listerner.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listerner.onError(parseError, parseErrorTitle);
                    }

                }
            });
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(jsonRequest);
            AppControler.getsInstance().addToRequestQueue(jsonRequest);
        } else {
            Log.d(TAG, "volleyPostData response - No Internet");
            listerner.onError(parseError, parseErrorTitle);
        }
    }

    public void volleyPaytmPostData(String type, final String url, JSONObject jsonObject, final String key, final String header, final VolleyResponseListerner listener) {
        int posttype;
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        Log.d(TAG, "volleyPostData request url - " + url);
        Log.d(TAG, "volleyPostData request data - " + jsonObject.toString());
        if (isOnLline()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            if (type.equalsIgnoreCase("delete")) {
                posttype = Request.Method.DELETE;
            } else {
                posttype = Request.Method.POST;
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(posttype,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostData response - " + response.toString());
                            try {
                                listener.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                pDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                    if (error instanceof TimeoutError) {
                        listener.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listener.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listener.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listener.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listener.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listener.onError(parseError, parseErrorTitle);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put(key, header);
                    Log.d(key, header);
                    return headers;
                }

            };
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppControler.getsInstance().addToRequestQueue(jsonObjReq);
        } else {
            Log.d(TAG, "volleyPostData response - No Internet");
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public void volleyPaytmGETData(final String url, JSONObject jsonObject, final String key, final String header, final VolleyResponseListerner listener) {
        Log.d(TAG, "volleyPostData request url - " + url);
        Log.d(TAG, "volleyPostData request data - " + jsonObject.toString());
        if (isOnLline()) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volleyPostData response - " + response.toString());
                            try {
                                listener.onResponse(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError) {
                        listener.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listener.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listener.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listener.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listener.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listener.onError(parseError, parseErrorTitle);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put(key, header);
                    Log.d(key, header);
                    return headers;
                }


            };
            int MY_SOCKET_TIMEOUT_MS = 30000;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            queue.add(jsonObjReq);
            AppControler.getsInstance().addToRequestQueue(jsonObjReq);
        } else {
            Log.d(TAG, "volleyPostData response - No Internet");
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public boolean isOnLline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
