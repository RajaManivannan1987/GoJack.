package com.example.gojack.gojack.HelperClasses.Common;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class GoJackServerUrls {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    //    public static String SERVER_URL = "http://imaginetventures.net/sample/gojack/webservice/";
//    public static String SERVER_URL = "http://www.shoutjack.com/manage/webservice/";
    //    public static String SERVER_URL = "http://vvnear.com/gojack/manage/webservice/";
//    public static String SERVER_URL = "http://www.calljacktech.com/manage/webservice/";

    public static String SERVER_URL = "http://www.dial-jack.com/manage/webservice/";
    public static String LOGIN = SERVER_URL + "pilotlogin";
    public static String REGISTER_LOCATION = SERVER_URL + "updatepilotlocation";
    public static String UPDATE_PILOT_STATUS = SERVER_URL + "updatepilotstatus";
    public static String GET_PILOT_STATUS = SERVER_URL + "checkpilotstatus";
    public static String SEND_DEVICE_ID = SERVER_URL + "updatepilotdevice";
    public static String CHECK_STATUS = SERVER_URL + "checkpilotstatus";
    public static String ACCEPTORCANCEL = SERVER_URL + "pilotacceptcancel";
    public static String START_TRIP = SERVER_URL + "starttrip";
    public static String PILOT_CANCELTRIP = SERVER_URL + "pilotcanceltrip";
    public static String CHECK_RIDE_STATUS = SERVER_URL + "checkpilotridestatus";
    public static String PILOT_CANCEL = SERVER_URL + "pilotcancelreason";
    public static String END_TRIP = SERVER_URL + "endtrip";
    public static String LOGOUT = SERVER_URL + "pilotlogout";
    public static String HISTORY = SERVER_URL + "pilottripsummary";
    public static String HISTORY_DETAILS = SERVER_URL + "pilottripdetails";
    public static String UPDATE_DELIVERY_PERSON = SERVER_URL + "deliveryperson";
    public static String HAIL_START_TRIP = SERVER_URL + "hailrequest";
    public static String HAIL_END_TRIP = SERVER_URL + "hailendrequest";
    public static String FORGOTPASSWORD = SERVER_URL + "forgotpasswordpilot";
    public static String VALIDATEOTP = SERVER_URL + "fppilototpverify";
    public static String UPDATEPASSWORD = SERVER_URL + "updatepasswordpilot";
    public static String NOTIFY_CUSTOMER = SERVER_URL + "notifycustomer";
    public static String RESENDOTP = SERVER_URL + "pilotresendotp";
    public static String ACCEPT_ONLY = SERVER_URL + "accept";
    public static String TODAY_DETAILS = SERVER_URL + "todaydriveraccounts";
    public static String TABLE_DETAILS = SERVER_URL + "graphaccountdetail";
    public static String UPDATE_PAYTMTOKEN = SERVER_URL + "updatepaytmtokenpilot";
    public static String UNLINK_PAYTM = SERVER_URL + "unlinkpaytm";


    //Paytm
    // For staging
//    public static final String PaytmAuthorization = "Basic bWVyY2hhbnQtY2FsbGphY2t0ZWNoLXN0YWdpbmc6ZDE2MjQ2ODEtN2M5YS00Y2E1LWE0MDItNWJlNTdlMzc5Y2Jk";
    //   For Live
    public static final String PaytmAuthorization = "Basic bWVyY2hhbnQtY2FsbGphY2t0ZWNoOjE1NmFlMDE1LWViMmYtNDcyZS04N2RhLTMxMDRhMjc4NDdmMA==";
    public static final String StagingOuthUrl = "https://accounts-uat.paytm.com";
    public static final String productionOuthUrl = "https://accounts.paytm.com";
    public static final String SendOTP = productionOuthUrl + "/signin/otp";
    public static final String GetAccessToken = productionOuthUrl + "/signin/validate/otp";
    public static final String GetUserDetails = productionOuthUrl + "/user/details";
    public static final String PAYTM_LOGOUT = productionOuthUrl + "/oauth2/accessToken/";

    //Paytm wallet Api
    public static String ADDMONET_RQUESTTYPE = "ADD_MONEY";
    public static String WITHDRAW_RQUESTTYPE = "WITHDRAW";
    public static String CALLBACKURL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";


    public static String paytmMID = "CallJa98407114735517";
    public static String paytm_Client_Id = "merchant-calljacktech";
    public static String paytmWebsite = "CallJaWAP";
    public static String paytmIndustry_type_ID = "Travel";
    public static String paytmChannel_ID = "WAP";
    public static final String walletStagingUrl = "https://pguat.paytm.com/";
    public static final String walletProductionUrl = "https://secure.paytm.in/";
    public static final String checkBalance = "https://trust.paytm.in/service/checkUserBalance";
    //    public static final String checkBalance_Staging = "https://trust-uat.paytm.in/service/checkUserBalance";
    public static final String addMoney = walletProductionUrl + "oltp-web/processTransaction";
    public static final String widthdrawChecksumGenerate = "http://www.dial-jack.com/generateWithdrawChecksum_pilot.php";
    public static final String addMoneyChecksumGenerate = "http://www.dial-jack.com/generateChecksum_pilot.php";
//    public static final String checksumVerify = "http://calljacktech.com/verifyChecksum.php";

}

