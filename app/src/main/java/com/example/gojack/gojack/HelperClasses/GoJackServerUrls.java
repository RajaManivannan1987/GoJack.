package com.example.gojack.gojack.HelperClasses;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class GoJackServerUrls {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    //    public static String SERVER_URL = "http://imaginetventures.net/sample/gojack/webservice/";
//    public static String SERVER_URL = "http://www.shoutjack.com/manage/webservice/";
//    public static String SERVER_URL = "http://www.calljacktech.com/manage/webservice/";
    public static String SERVER_URL = "http://vvnear.com/gojack/manage/webservice/";
    public static String LOGIN = SERVER_URL + "pilotlogin";
    public static String REGISTER_LOCATION = SERVER_URL + "updatepilotlocation";
    public static String UPDATE_PILOT_STATUS = SERVER_URL + "updatepilotstatus";
    public static String SEND_DEVICE_ID = SERVER_URL + "updatepilotdevice";
    public static String CHECK_STATUS = SERVER_URL + "checkpilotstatus";
    public static String ACCEPTORCANCEL = SERVER_URL + "pilotacceptcancel";
    public static String START_TRIP = SERVER_URL + "starttrip";
    public static String PILOT_CANCELTRIP = SERVER_URL + "pilotcanceltrip";
    public static String CHECK_RIDE_STATUS = SERVER_URL + "checkpilotridestatus";
    public static String PILOT_CANCEL = SERVER_URL + "pilotcancelreason";
    public static String NOTIFY_CUSTOMER = SERVER_URL + "notifycustomer";
    public static String END_TRIP = SERVER_URL + "endtrip";
    public static String LOGOUT = SERVER_URL + "pilotlogout";
    public static String HISTORY = SERVER_URL + "pilottripsummary";
    public static String HISTORY_DETAILS = SERVER_URL + "pilottripdetails";
    public static String UPDATE_DELIVERY_PERSON = SERVER_URL + "deliveryperson";
    public static String HAIL_START_TRIP = SERVER_URL + "hailrequest";
    public static String HAIL_END_TRIP = SERVER_URL + "hailendrequest";
    public static String FORGOTPASSWORD = SERVER_URL + "forgotpassword?username=";
}

