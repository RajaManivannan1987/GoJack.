package com.example.gojack.gojack.HelperClasses.WebService;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.gojack.gojack.HelperClasses.Common.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.ServiceClass.MyLocation;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.ServiceClass.GPSTracker;

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

    public void login(Activity activity, String username, String password, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyPostData(GoJackServerUrls.LOGIN, jsonObject, activity, new VolleyResponseListerner() {
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
    public void getPilotStatus( final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.GET_PILOT_STATUS, jsonObject, new VolleyResponseListerner() {
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

    public void verifyPaytmOTP(final String otp, String state, final VolleyResponseListerner listener) {
        String url = GoJackServerUrls.GetAccessToken;
        String Authorization = GoJackServerUrls.PaytmAuthorization;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("otp", otp);
            jsonObject.put("state", state);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setPaytmListerner(listener, url, "Authorization", Authorization, jsonObject);
    }

    public void SendOTP(String mobileNo, String emailId, final VolleyResponseListerner listener) {
        String url = GoJackServerUrls.SendOTP;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", emailId);
            jsonObject.put("phone", mobileNo);
            jsonObject.put("clientId", "merchant-calljacktech-staging");
            jsonObject.put("scope", "wallet");
            jsonObject.put("responseType", "token");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner(url, listener, context, jsonObject);
    }

    public void checkBalance(String tokenHeader, final VolleyResponseListerner listener) {
        String url = "http://trust-uat.paytm.in/wallet-web/checkBalance";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", GoJackServerUrls.paytmMID);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setPaytmListerner(listener, url, "ssotoken", tokenHeader, jsonObject);
    }

    public void generateAddmoneyChecksum(String orderId, String custId, String amount, String requestType, String paytmtoken, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
        String url = "http://calljacktech.com/generateChecksum.php";
        try {
            jsonObject.put("MID", "CallJa65607497328098");
            jsonObject.put("ORDER_ID", orderId);
            jsonObject.put("CUST_ID", custId);
            jsonObject.put("INDUSTRY_TYPE_ID", "Retail");
            jsonObject.put("CHANNEL_ID", "WAP");
            jsonObject.put("TXN_AMOUNT", amount);
            jsonObject.put("WEBSITE", "APP_STAGING");
            jsonObject.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
            jsonObject.put("REQUEST_TYPE", requestType);
            jsonObject.put("SSO_TOKEN", paytmtoken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setListerner(url, listener, context, jsonObject);
    }

    public void generateWithDrawChecksum(String orderId, String custId, String amount, String requestType, String paytmtoken, String deviceId, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
        String url = "http://calljacktech.com/generateWithdrawChecksum.php";
        try {
            jsonObject.put("AppIP", "127.0.0.1");
            jsonObject.put("MID", "CallJa65607497328098");
            jsonObject.put("OrderId", orderId);
            jsonObject.put("CustId", custId);
            jsonObject.put("IndustryType", "Retail");
            jsonObject.put("Channel", "WEB");
            jsonObject.put("TxnAmount", amount);
            jsonObject.put("ReqType", requestType);
            jsonObject.put("Currency", "INR");
            jsonObject.put("DeviceId", deviceId);
            jsonObject.put("SSOToken", paytmtoken);
            jsonObject.put("PaymentMode", "PPI");
            jsonObject.put("AuthMode", "USRPWD");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListerner(url, listener, context, jsonObject);
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
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.PILOT_CANCELTRIP, jsonObject, new VolleyResponseListerner() {
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

    public void startTrip(String rideId, String lat, String lang, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("rideid", rideId);
            jsonObject.put("slatitude", lat);
            jsonObject.put("slongitude", lang);
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

    public void hailStartTrip(String address, String lat, String lang, String endlat, String endlang, String endaddress, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("startinglatitude", lat);
            jsonObject.put("startinglongitude", lang);
            jsonObject.put("startingaddress", address);
            jsonObject.put("endinglatitude", endlat);
            jsonObject.put("endinglongitude", endlang);
            jsonObject.put("endingaddress", endaddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.HAIL_START_TRIP, jsonObject, new VolleyResponseListerner() {
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

    public void updatePaytmToken(String paytmtoken, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("paytm_token", paytmtoken);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        volleyClass.volleyPostData(GoJackServerUrls.UPDATE_PAYTMTOKEN, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listener.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listener.onError(message, title);
            }
        });
    }

    public void hideEndTrip(String rideId, String lat, String lang, String address, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("rideid", rideId);
            jsonObject.put("endinglatitude", lat);
            jsonObject.put("endinglongitude", lang);
            jsonObject.put("endingaddress", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.HAIL_END_TRIP, jsonObject, new VolleyResponseListerner() {
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

    public void getTodayDetails(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.TODAY_DETAILS, jsonObject, new VolleyResponseListerner() {
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

    public void getTableDetails(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyPostData(GoJackServerUrls.TABLE_DETAILS, jsonObject, (Activity) context, new VolleyResponseListerner() {
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

    public void forgotPassword(String username, final VolleyResponseListerner listener) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        volleyClass.volleyPostData(GoJackServerUrls.FORGOTPASSWORD, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listener.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listener.onError(message, title);
            }
        });
    }

    public void validateOtp(String customerId, String otp, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("driverid", customerId);
            jsonObject.put("otpcode", otp);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        volleyClass.volleyPostData(GoJackServerUrls.VALIDATEOTP, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listener.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listener.onError(message, title);
            }
        });
    }

    public void reSendOtp(String customerId, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("driverid", customerId);
//            jsonObject.put("mobilenumber", mobileno);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        volleyClass.volleyPostData(GoJackServerUrls.RESENDOTP, jsonObject, (Activity) context, new VolleyResponseListerner() {
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

    public void changePassword(String customerId, String password, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("driverid", customerId);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        volleyClass.volleyPostData(GoJackServerUrls.UPDATEPASSWORD, jsonObject, (Activity) context, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                listener.onResponse(response);
            }

            @Override
            public void onError(String message, String title) {
                listener.onError(message, title);
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

    public void callAcceptOnly(String rideId, final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("rideid", rideId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        volleyClass.volleyNoProgressPostData(GoJackServerUrls.ACCEPT_ONLY, jsonObject, new VolleyResponseListerner() {
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

    private void setPaytmListerner(final VolleyResponseListerner listerner, String url, String key, String header, JSONObject jsonObject) {
        volleyClass.volleyPaytmPostData(url, jsonObject, key, header, new VolleyResponseListerner() {
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

    private void setListerner(String url, final VolleyResponseListerner listerner, Context context, JSONObject jsonObject) {
        volleyClass.volleyPostData(url, jsonObject, (Activity) context, new VolleyResponseListerner() {
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
