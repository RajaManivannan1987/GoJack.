package com.example.gojack.gojack.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.R;

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
