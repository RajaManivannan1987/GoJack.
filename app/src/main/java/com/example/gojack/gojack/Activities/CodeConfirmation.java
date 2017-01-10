package com.example.gojack.gojack.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.HelperClasses.CommonMethods;
import com.example.gojack.gojack.HelperClasses.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.ImcomeSmsReceiver;
import com.example.gojack.gojack.Interface.ImComeSms;
import com.example.gojack.gojack.R;

/**
 * Created by IM0033 on 8/3/2016.
 */
public class CodeConfirmation extends CommonActionBar {
    private EditText userNameEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_confirmation);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        setActionBar();
        ImcomeSmsReceiver.bindMessageListener(new ImComeSms() {
            @Override
            public void messageReceived(String messageText) {
                userNameEditText.setText(messageText);
            }
        });
        findViewById(R.id.codeSendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userNameEditText.getText().toString().equalsIgnoreCase("")) {
                    userNameEditText.setError(null);
                    startActivity(new Intent(CodeConfirmation.this, ChangePassword.class));
                } else {
                    userNameEditText.setError("Enter otp");
                    userNameEditText.requestFocus();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!CommonMethods.checkmarshmallowPermission(CodeConfirmation.this, Manifest.permission.RECEIVE_SMS, GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION)) {
            CommonMethods.showLocationAlert(CodeConfirmation.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GoJackServerUrls.MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                onStart();
            }
        }
    }
}
