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
        String url = GoJackServerUrls.LOGIN;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setListerner(url, listerner, context, jsonObject);
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
        setVolleyNoProgressBarPostData(GoJackServerUrls.UPDATE_PILOT_STATUS, listerner, jsonObject);
    }

    public void getPilotStatus(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setVolleyNoProgressBarPostData(GoJackServerUrls.GET_PILOT_STATUS, listerner, jsonObject);
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
        setPaytmListerner(listener, "post", url, "Authorization", Authorization, jsonObject);
    }

    public void SendOTP(String mobileNo, String emailId, final VolleyResponseListerner listener) {
        String url = GoJackServerUrls.SendOTP;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", emailId);
            jsonObject.put("phone", mobileNo);
            jsonObject.put("clientId", GoJackServerUrls.paytm_Client_Id);
            jsonObject.put("scope", "wallet");
            jsonObject.put("responseType", "token");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner(url, listener, context, jsonObject);
    }

    public void checkBalance(String tokenHeader, final VolleyResponseListerner listener) {
        Log.d("", "ssotoken" + ",  " + tokenHeader);
        String url = GoJackServerUrls.checkBalance;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", GoJackServerUrls.paytmMID);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setPaytmListerner(listener, "post", url, "ssotoken", tokenHeader, jsonObject);
    }

    public void logoutPaytm(final VolleyResponseListerner listener) {
        String url = GoJackServerUrls.PAYTM_LOGOUT + PrefManager.getPrefManager(context).getPilotPaytmtoken();
        setPaytmListerner(listener, "delete", url, "Authorization", GoJackServerUrls.PaytmAuthorization, new JSONObject());
    }

    public void generateAddmoneyChecksum(String orderId, String custId, String amount, String requestType, String paytmEmail, String paytmMobile, String paytmtoken, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
//        String url = "http://www.dial-jack.com/generateChecksum.php";
        String url = GoJackServerUrls.addMoneyChecksumGenerate;
        try {
            jsonObject.put("MID", GoJackServerUrls.paytmMID);
            jsonObject.put("ORDER_ID", orderId);
            jsonObject.put("CUST_ID", custId);
            jsonObject.put("INDUSTRY_TYPE_ID", GoJackServerUrls.paytmIndustry_type_ID);
            jsonObject.put("CHANNEL_ID", GoJackServerUrls.paytmChannel_ID);
            jsonObject.put("TXN_AMOUNT", amount);
            jsonObject.put("WEBSITE", GoJackServerUrls.paytmWebsite);
            jsonObject.put("CALLBACK_URL", GoJackServerUrls.CALLBACKURL);
            jsonObject.put("REQUEST_TYPE", requestType);
            jsonObject.put("EMAIL", paytmEmail);
            jsonObject.put("MOBILE_NO", paytmMobile);
            jsonObject.put("SSO_TOKEN", paytmtoken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setListerner(url, listener, context, jsonObject);
    }

    public void generateWithDrawChecksum(String orderId, String custId, String amount, String requestType, String paytmtoken, String deviceId, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
//        String url = "http://www.dial-jack.com/generateWithdrawChecksum.php";
        String url = GoJackServerUrls.widthdrawChecksumGenerate;
        try {
//            jsonObject.put("AppIP", "127.0.0.1");
            jsonObject.put("AppIP", "192.168.1.215");
            jsonObject.put("MID", GoJackServerUrls.paytmMID);
            jsonObject.put("OrderId", orderId);
            jsonObject.put("CustId", custId);
            jsonObject.put("IndustryType", GoJackServerUrls.paytmIndustry_type_ID);
            jsonObject.put("Channel", GoJackServerUrls.paytmChannel_ID);
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
        setVolleyNoProgressBarPostData(GoJackServerUrls.CHECK_STATUS, listerner, jsonObject);
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
        setVolleyNoProgressBarPostData(GoJackServerUrls.ACCEPTORCANCEL, listerner, jsonObject);
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
        setVolleyNoProgressBarPostData(GoJackServerUrls.PILOT_CANCELTRIP, listerner, jsonObject);
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
        setVolleyNoProgressBarPostData(GoJackServerUrls.START_TRIP, listerner, jsonObject);

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
        setVolleyNoProgressBarPostData(GoJackServerUrls.HAIL_START_TRIP, listerner, jsonObject);
    }

    public void checkRideStatus(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setVolleyNoProgressBarPostData(GoJackServerUrls.CHECK_RIDE_STATUS, listerner, jsonObject);
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
        setVolleyNoProgressBarPostData(GoJackServerUrls.NOTIFY_CUSTOMER, listerner, jsonObject);
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
        setVolleyNoProgressBarPostData(GoJackServerUrls.END_TRIP, listerner, jsonObject);
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
        setListerner(GoJackServerUrls.UPDATE_PAYTMTOKEN, listener, context, jsonObject);

    }

    public void checkPaytmUserValidate(String tokenHeader, final VolleyResponseListerner listener) {
        String url = GoJackServerUrls.GetUserDetails;
        setPaytmListerner1(listener, url, "session_token", tokenHeader, new JSONObject());

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
        setVolleyNoProgressBarPostData(GoJackServerUrls.HAIL_END_TRIP, listerner, jsonObject);
    }


    public void logout(final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setListerner(GoJackServerUrls.LOGOUT, listener, context, jsonObject);

    }

    public void unLinkPaytm(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("userid", PrefManager.getPrefManager(context).getPilotId());
            jsonObject.put("type", "pilot");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setListerner(GoJackServerUrls.UNLINK_PAYTM, listerner, context, jsonObject);
    }

    public void getHistoryList(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setListerner(GoJackServerUrls.HISTORY, listerner, context, jsonObject);
    }

    public void getTodayDetails(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setVolleyNoProgressBarPostData(GoJackServerUrls.TODAY_DETAILS, listerner, jsonObject);
    }

    public void getTableDetails(final VolleyResponseListerner listerner) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", PrefManager.getPrefManager(context).getPilotToken());
            jsonObject.put("driverid", PrefManager.getPrefManager(context).getPilotId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setListerner(GoJackServerUrls.TABLE_DETAILS, listerner, context, jsonObject);
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
        setListerner(GoJackServerUrls.HISTORY_DETAILS, listerner, context, jsonObject);
    }

    public void forgotPassword(String username, final VolleyResponseListerner listener) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner(GoJackServerUrls.FORGOTPASSWORD, listener, context, jsonObject);
    }

    public void validateOtp(String customerId, String otp, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("driverid", customerId);
            jsonObject.put("otpcode", otp);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner(GoJackServerUrls.VALIDATEOTP, listener, context, jsonObject);
    }

    public void reSendOtp(String customerId, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("driverid", customerId);
//            jsonObject.put("mobilenumber", mobileno);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        setListerner(GoJackServerUrls.RESENDOTP, listener, context, jsonObject);
    }

    public void changePassword(String customerId, String password, final VolleyResponseListerner listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("driverid", customerId);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        setListerner(GoJackServerUrls.UPDATEPASSWORD, listener, context, jsonObject);
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
        setListerner(GoJackServerUrls.UPDATE_DELIVERY_PERSON, listerner, context, jsonObject);
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
        setVolleyNoProgressBarPostData(GoJackServerUrls.ACCEPT_ONLY, listerner, jsonObject);
    }

    private void setVolleyNoProgressBarPostData(String url, final VolleyResponseListerner listerner, JSONObject jsonObject) {
        volleyClass.volleyNoProgressPostData(url, jsonObject, new VolleyResponseListerner() {
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

    private void setPaytmListerner(final VolleyResponseListerner listerner, String type, String url, String key, String header, JSONObject jsonObject) {
        volleyClass.volleyPaytmPostData(type, url, jsonObject, key, header, new VolleyResponseListerner() {
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

    private void setPaytmListerner1(final VolleyResponseListerner listerner, String url, String key, String header, JSONObject jsonObject) {
        volleyClass.volleyPaytmGETData(url, jsonObject, key, header, new VolleyResponseListerner() {
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
