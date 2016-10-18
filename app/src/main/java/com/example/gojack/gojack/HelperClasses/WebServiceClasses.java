package com.example.gojack.gojack.HelperClasses;

import android.app.Activity;
import android.content.Context;

import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.ServiceClass.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 8/9/2016.
 */
public class WebServiceClasses {

    public static WebServiceClasses webServiceClasses = null;
    private String TAG;
    Context context;
    private VolleyClass volleyClass;
    private GPSTracker gpsTracker;


    public static WebServiceClasses getWebServiceClasses(Context context, String tag) {
        if (webServiceClasses == null) {
            webServiceClasses = new WebServiceClasses(context, tag);
        }
        return webServiceClasses;
    }

    public WebServiceClasses(Context context, String Tag) {
        this.context = context;
        this.TAG = Tag;
        gpsTracker = new GPSTracker();
        volleyClass = new VolleyClass(context, TAG);
    }

    public void login(String username, String password, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyPostData(GoJackServerUrls.LOGIN, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void sendLocation(String lat, String lang, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("latitude", lat);
            jsonObject.put("longitude", lang);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyUpdateLocation(GoJackServerUrls.REGISTER_LOCATION, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void updateStatus(String status, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.UPDATE_PILOT_STATUS, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void updateDeviceId(String deviceid, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("deviceid", deviceid);
            jsonObject.put("os", "android");
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyUpdateLocation(GoJackServerUrls.SEND_DEVICE_ID, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void checkStatus(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.CHECK_STATUS, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void acceptRequest(String rideId, String status, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("rideid", rideId);
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("status", status);
            jsonObject.put("latitude", gpsTracker.getLatitude());
            jsonObject.put("longitude", gpsTracker.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.ACCEPTORCANCEL, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void pilotCancel(String rideId, String reason, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("rideid", rideId);
            jsonObject.put("reasonid", reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
        volleyClass.volleyPostData(GoJackServerUrls.PILOT_CANCELTRIP, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1")) {
                    listerner.onResponse(response);
                } else {
                    PrefManager.getPrefManager(context).logout();
                }
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void startTrip(String rideId, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("rideid", rideId);
            jsonObject.put("slatitude", gpsTracker.getLatitude());
            jsonObject.put("slongitude", gpsTracker.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.START_TRIP, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });

    }

    public void checkRideStatus(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.CHECK_RIDE_STATUS, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getString("token_status").equalsIgnoreCase("1")) {
                    listerner.onResponse(response);
                } else {
                    PrefManager.getPrefManager(context).logout();
                }
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void cancelReason(final VolleyResponseListerner listerner) {

        volleyClass.volleyNoProgressGet(GoJackServerUrls.PILOT_CANCEL, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void notifyCustomer(String rideId, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("rideid", rideId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.NOTIFY_CUSTOMER, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void endTrip(String rideId, String lat, String lang, String ridetype, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("rideid", rideId);
            jsonObject.put("elatitude", lat);
            jsonObject.put("elongitude", lang);
            jsonObject.put("ridetype", ridetype);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.END_TRIP, jsonObject, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void logout(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyPostData(GoJackServerUrls.LOGOUT, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void getHistoryList(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyPostData(GoJackServerUrls.HISTORY, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void getHistoryDetails(String rideId, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("rideid", rideId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyPostData(GoJackServerUrls.HISTORY_DETAILS, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }

    public void deliveryPerson(String rideId, String personName, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("rideid", rideId);
            jsonObject.put("deliveredperson", personName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyPostData(GoJackServerUrls.UPDATE_DELIVERY_PERSON, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listerner.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listerner.onError(message, title);
            }
        });
    }
}
