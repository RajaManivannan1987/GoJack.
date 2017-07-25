package com.example.gojack.gojack.Activities;

/**
 * Created by Im033 on 5/30/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gojack.gojack.CommonActivityClasses.CommonActionBar;
import com.example.gojack.gojack.CommonActivityClasses.CommonNavigstionBar;
import com.example.gojack.gojack.HelperClasses.Common.GoJackServerUrls;
import com.example.gojack.gojack.HelperClasses.Interface.VolleyResponseListerner;
import com.example.gojack.gojack.HelperClasses.Session.PrefManager;
import com.example.gojack.gojack.HelperClasses.WebService.WebServiceClasses;
import com.example.gojack.gojack.R;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends CommonNavigstionBar {
    private String TAG = "PaymentActivity";
    private TextView paymentTypeBalanceTextView, addMoneyButton;
    private static float paytmBalance;
    private PrefManager prefManager;
    private String orderId, checksums = "";
    private WebServiceClasses webServices;
    private EditText amountEditText;
    private int amountValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_payment_type);
        prefManager = new PrefManager(PaymentActivity.this);
        webServices = new WebServiceClasses(PaymentActivity.this, TAG);

        paymentTypeBalanceTextView = (TextView) findViewById(R.id.paytmWalletBalanceEditText);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        addMoneyButton = (TextView) findViewById(R.id.addmoneyButton);
        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!amountEditText.getText().toString().equalsIgnoreCase("")) {
                    amountValue = Integer.parseInt(amountEditText.getText().toString());
                    if (amountValue >= 150) {
//                    if (amountValue >= 10) {
                        generateChecksum();
                    } else {
                        Toast.makeText(PaymentActivity.this, "Add a minimum of Rs.150 to wallet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PaymentActivity.this, "Enter amount", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!prefManager.getPilotPaytmtoken().equalsIgnoreCase("")) {
            checkBalance(prefManager.getPilotPaytmtoken());
        } else {
            startActivity(new Intent(PaymentActivity.this, PaytmLogin.class));
        }
    }

    private void checkBalance(final String access_token) {
        webServices.checkBalance(access_token, new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                paytmBalance = Float.parseFloat(response.getJSONObject("response").getString("amount"));
                paymentTypeBalanceTextView.setText("Rs " + response.getJSONObject("response").getString("amount"));
            }

            @Override
            public void onError(String message, String title) {

            }
        });
    }

    public void addBalance(int amount, String checksums) {

        PaytmPGService Service = PaytmPGService.getStagingService();
//        PaytmPGService Service = PaytmPGService.getProductionService();

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", "CallJa65607497328098");
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", prefManager.getPilotId());
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", String.valueOf(amount));
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("CALLBACK_URL", GoJackServerUrls.ADDMONET_CALLBACKURL);
        paramMap.put("REQUEST_TYPE", GoJackServerUrls.ADDMONET_RQUESTTYPE);
        paramMap.put("SSO_TOKEN", prefManager.getPilotPaytmtoken());
        paramMap.put("CHECKSUMHASH", checksums);
        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {

            @Override
            public void onTransactionResponse(Bundle bundle) {
                if (bundle.getString("STATUS").equalsIgnoreCase("TXN_SUCCESS")) {
                    Toast.makeText(getApplicationContext(), "Payment Transaction Successful", Toast.LENGTH_LONG).show();
                    onResume();
                }else {
                    Toast.makeText(getApplicationContext(), bundle.getString("RESPMSG") + " Your transaction is : " + bundle.getString("TXNID"), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void networkNotAvailable() {
                Log.d(TAG, "networkNotAvailable ");
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Log.d(TAG, "clientAuthenticationFailed " + s);
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.d(TAG, "someUIErrorOccurred " + s);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.d(TAG, "onErrorLoadingWebPage " + s);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Log.d(TAG, "onBackPressedCancelTransaction");
            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Log.d(TAG, "Payment Transaction Failed " + bundle);
                Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
            }


        });
    }

    private void generateChecksum() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "dialjackpilot" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(100);

        new WebServiceClasses(PaymentActivity.this, TAG).generateAddmoneyChecksum(orderId, prefManager.getPilotId(), amountEditText.getText().toString(), GoJackServerUrls.ADDMONET_RQUESTTYPE, prefManager.getPilotPaytmtoken(), new VolleyResponseListerner() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                String checksumgenerate = response.getString("CHECKSUMHASH");
                checksums = checksumgenerate.replaceAll("\\\\", "");
                addBalance(amountValue, checksums);
            }

            @Override
            public void onError(String message, String title) {

            }
        });

    }
}

