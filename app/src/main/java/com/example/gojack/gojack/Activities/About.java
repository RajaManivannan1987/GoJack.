package com.example.gojack.gojack.Activities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class About extends CommonNavigstionBar {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_about);

    }
}
