package com.example.gojack.gojack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.AlertDialogManager;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.Validation;
import com.example.gojack.gojack.HelperClasses.WebServiceClasses;
import com.example.gojack.gojack.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.R;

import org.json.JSONException;
import org.json.JSONObject;

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
                if (Validation.isMobileNoValid(userNameEditText.getText().toString())) {
                    userNameEditText.setError(null);
                    new WebServiceClasses(ForgotPassword.this,"ForgotPassword").forgotPassword(userNameEditText.getText().toString(), new VolleyResponseListerner() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                startActivity(new Intent(getApplicationContext(), CodeConfirmation.class).putExtra("customerId", response.getString("userid")));
                            } else {
                                CommonMethods.toast(ForgotPassword.this, response.getString("message"));
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            AlertDialogManager.showAlertDialog(ForgotPassword.this,title,message,false);
                        }
                    });

                } else {
                    userNameEditText.requestFocus();
                    userNameEditText.setError(Validation.mobileNoError);
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
