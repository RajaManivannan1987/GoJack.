package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.gojack.gojack.AdapterClasses.HelperViewPagerAdapter;
import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.R;

import java.util.ArrayList;

/**
 * Created by IM0033 on 8/1/2016.
 */
public class Help extends CommonNavigstionBar implements ViewPager.OnPageChangeListener {
    private String TAG = Help.class.getName();
    private ViewPager mPager;
    private TextView skipButton;
    private int[] imageResources = {R.drawable.lock_icon, R.drawable.gojack_logo, R.drawable.success};
    private ArrayList<Integer> imageArray = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_help);
        loadScreens();
    }

    private void loadScreens() {
        skipButton = (TextView) findViewById(R.id.skipButton);
        for (int i = 0; i < imageResources.length; i++) {
            imageArray.add(imageResources[i]);
        }
        mPager = (ViewPager) findViewById(R.id.pager_introduction);
        mPager.setAdapter(new HelperViewPagerAdapter(Help.this, imageArray));


        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Help.this, GoOffline.class));
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
