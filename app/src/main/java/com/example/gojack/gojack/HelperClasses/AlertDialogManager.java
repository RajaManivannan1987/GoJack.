package com.example.gojack.gojack.HelperClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.gojack.gojack.R;

/**
 * Created by Im033 on 2/15/2017.
 */

public class AlertDialogManager {

    public static void showAlertDialog(final Context context, String title, String message, Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (status != null)
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.wrong);

        // Setting ok button

        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
