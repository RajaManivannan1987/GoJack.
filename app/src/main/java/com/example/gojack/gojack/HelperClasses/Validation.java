package com.example.gojack.gojack.HelperClasses;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Im033 on 1/18/2017.
 */

public class Validation {
    public static String userNameError = "User Name must be greater than 6";
    public static String passwordError = "password must be greater than 6";
    public static String mobileNoError = "Enter 10 digit mobile number or mailId";
    public static String otpError = "Enter Valid Otp";

    public static boolean isUserNameValid(String text) {
        return !TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    public static boolean isMobileNoValid(String text) {
        return !TextUtils.isEmpty(text) && Patterns.PHONE.matcher(text).matches();
    }

    public static boolean isPasswordValid(String text) {
        return text.length() >= 6;
    }

    public static boolean isOtpValid(String text) {
        return !TextUtils.isEmpty(text) && text.length() >= 4;
    }
}
