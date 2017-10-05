package com.example.gojack.gojack.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.R;

/**
 * Created by Im033 on 9/20/2017.
 */

public class ReceiverInfoActivity extends AppCompatActivity{
    private TextView receiverNameText, receiverPhoneText, receiverLandmarkText, InstructionDetails;
    LinearLayout ReceiverLayout;
    private int defaultViewHeight;
    private int previousFingerPosition = 0;
    private int baseLayoutPosition = 0;

    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_receiverinfo);
        ReceiverLayout = (LinearLayout) findViewById(R.id.ReceiverLayout);
        receiverNameText = (TextView) findViewById(R.id.receiverNameText);
        receiverPhoneText = (TextView) findViewById(R.id.receiverPhoneText);
        receiverLandmarkText = (TextView) findViewById(R.id.receiverLandmarkText);
        InstructionDetails = (TextView) findViewById(R.id.InstructionDetails);

        if (getIntent().getExtras() != null) {
            receiverNameText.setText(getIntent().getExtras().getString("receivername"));
            receiverPhoneText.setText(getIntent().getExtras().getString("receiverphone"));
            receiverLandmarkText.setText(getIntent().getExtras().getString("receiverlandmark"));
            InstructionDetails.setText(getIntent().getExtras().getString("instructions"));
        }
        receiverPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.callFunction(ReceiverInfoActivity.this, getIntent().getExtras().getString("receiverphone"));
            }
        });
    }

}
