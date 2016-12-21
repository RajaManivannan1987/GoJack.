package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.R;

/**
 * Created by IM0033 on 8/2/2016.
 */
public class ForgotPassword extends CommonActionBar {
    SeekBar mySeek;
    Button forgotButton;
    private EditText userNameEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        setActionBar();
        forgotButton = (Button) findViewById(R.id.forgotButton);
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userNameEditText.getText().toString().equalsIgnoreCase("")) {
                    userNameEditText.setError(null);
                    startActivity(new Intent(ForgotPassword.this, CodeConfirmation.class));
                } else {
                    userNameEditText.requestFocus();
                    userNameEditText.setError("Enter 10 digit mobile number");
                }

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
