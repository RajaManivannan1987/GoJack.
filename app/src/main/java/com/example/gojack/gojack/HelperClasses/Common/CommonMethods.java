package com.example.gojack.gojack.HelperClasses.Common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gojack.gojack.Activities.About;
import com.example.gojack.gojack.Activities.GoOffline;
import com.example.gojack.gojack.Activities.GoOnline;
import com.example.gojack.gojack.Activities.HailActivity;
import com.example.gojack.gojack.Activities.LoginActivity;
import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class CommonMethods extends AppCompatActivity {

    public static void logoutIntent(Context context, Class<LoginActivity> activity) {
        context.startActivity(new Intent(context, activity).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void locationDirection(Context context, String lat, String lang) {
        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?&daddr=" + lat + "," + lang);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public static void callFunction(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);

    }

    public static boolean checkProvider(Activity context) {
        boolean isGps = false, isNetwork = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGps && !isNetwork) {
            return false;
        } else {
            return true;
        }
    }

    public static void showLocationAlert(final Context context) {
        String message = "To select/set area, enable location services";
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Location!");
        dialog.setMessage(message);
        dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                listener.yes();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                listener.no();
                dialogInterface.dismiss();
            }
        });
        dialog.create().show();
    }

    public static boolean checkLocationPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkmarshmallowPermission(Activity activity, String permision, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, permision) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permision}, requestCode);
            return true;
        }
        return false;
    }

    public static String getMarkerMovedAddress(Context context, LatLng dragPosition) {
        String adres = "";
        String location = "";
         /*Geocoder geocoder = new Geocoder(context, Locale.getDefault());
       try {
            List<Address> addresses = geocoder.getFromLocation(dragPosition.latitude, dragPosition.longitude, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                if (addresses.get(0).getSubLocality() != null) {
                    location = addresses.get(0).getSubLocality();
                } else if (addresses.get(0).getLocality() != null) {
                    location = addresses.get(0).getLocality();
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(returnedAddress.getAddressLine(i));
                }
                adres = stringBuilder.toString();
                Log.d("address", adres);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
            List<Address> addresses = geocoder.getFromLocation(dragPosition.latitude, dragPosition.longitude, 1);
            StringBuilder str = new StringBuilder();
            if (geocoder.isPresent()) {
                Address returnAddress = addresses.get(0);
                str.append(returnAddress.getAddressLine(0));
                adres = str.toString();
            } else {
                Toast.makeText(context,
                        "geocoder not present", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adres;
    }

    public static void closeIntent(Activity activity) {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(a);
        activity.finish();
        System.exit(0);
    }

    public static void showSnakBar(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void hideKeyboard(Context context, View v) {
        InputMethodManager input = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}

