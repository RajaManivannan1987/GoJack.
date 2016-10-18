package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.R;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class ForgotPassword extends CommonActionBar {
    SeekBar mySeek;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        setActionBar();
        findViewById(R.id.forgotButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPassword.this, CodeConfirmation.class));
            }
        });
        mySeek = (SeekBar) findViewById(R.id.myseek);
        mySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (progress > 95) {
                    seekBar.setThumb(getResources().getDrawable(R.drawable.accounts_menu_icon));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() > 95) {

                } else {
                    seekBar.setThumb(getResources().getDrawable(R.drawable.about_menu_icon));
                }

            }
        });
    }
}
