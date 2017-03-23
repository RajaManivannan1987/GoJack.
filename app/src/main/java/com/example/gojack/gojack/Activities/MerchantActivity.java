package com.example.gojack.gojack.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gojack.gojack.HelperClasses.Common.CommonMethods;
import com.example.gojack.gojack.R;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Im033 on 12/21/2016.
 */

public class MerchantActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paytm);
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        EditText orderIdEditText = (EditText) findViewById(R.id.order_id);
        orderIdEditText.setText(orderId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void onStartTransaction(View view) {

        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();
       /* paramMap.put("ORDER_ID", ((EditText) findViewById(R.id.order_id)).getText().toString());
        paramMap.put("MID", ((EditText) findViewById(R.id.merchant_id)).getText().toString());
        paramMap.put("CUST_ID", ((EditText) findViewById(R.id.customer_id)).getText().toString());
        paramMap.put("CHANNEL_ID", ((EditText) findViewById(R.id.channel_id)).getText().toString());
        paramMap.put("INDUSTRY_TYPE_ID", ((EditText) findViewById(R.id.industry_type_id)).getText().toString());
        paramMap.put("WEBSITE", ((EditText) findViewById(R.id.website)).getText().toString());
        paramMap.put("TXN_AMOUNT", ((EditText) findViewById(R.id.transaction_amount)).getText().toString());
        paramMap.put("THEME", ((EditText) findViewById(R.id.theme)).getText().toString());
        paramMap.put("EMAIL", ((EditText) findViewById(R.id.cust_email_id)).getText().toString());
        paramMap.put("MOBILE_NO", ((EditText) findViewById(R.id.cust_mobile_no)).getText().toString());
*/
        paramMap.put("ORDER_ID", ((EditText) findViewById(R.id.order_id)).getText().toString());
        paramMap.put("MID", "imagin69474426581763");
        paramMap.put("CUST_ID", ((EditText) findViewById(R.id.customer_id)).getText().toString());
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", ((EditText) findViewById(R.id.industry_type_id)).getText().toString());
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("TXN_AMOUNT", ((EditText) findViewById(R.id.transaction_amount)).getText().toString());
        paramMap.put("THEME", ((EditText) findViewById(R.id.theme)).getText().toString());
        paramMap.put("EMAIL", "rajamcaarg@gmail.com");
        paramMap.put("MOBILE_NO", "9865132365");


        PaytmOrder Order = new PaytmOrder(paramMap);

        final PaytmMerchant Merchant = new PaytmMerchant(
               /* "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/

             /*   "https://steptest.in/demo/lib/generateChecksum.php",
                "https://steptest.in/demo/lib/verifyChecksum.php");
*/

                "http://imaginetventures.me/sample/steptest/generateChecksum.php",
                "http://imaginetventures.me/sample/steptest/verifyChecksum.php");

        Service.initialize(Order, Merchant, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionSuccess(Bundle inResponse) {
                Log.d("MerchantActivity", "Payment Transaction is successful " + inResponse);
                Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionFailure(String s, Bundle inErrorMessage) {
                Log.d("MerchantActivity", "Payment Transaction Failed " + inErrorMessage);
                CommonMethods.toast(MerchantActivity.this, "onErrorLoadingWebPage " + s);
            }

            @Override
            public void networkNotAvailable() {
                CommonMethods.toast(MerchantActivity.this, "networkNotAvailable");
                Log.d("LOG", "Payment networkNotAvailable ");
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                CommonMethods.toast(MerchantActivity.this, "networkNotAvailable " + s);
                Log.d("MerchantActivity", "Payment clientAuthenticationFailed " + s);
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.d("MerchantActivity", "Payment someUIErrorOccurred " + s);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                CommonMethods.toast(MerchantActivity.this, "onErrorLoadingWebPage " + s);
                Log.d("MerchantActivity", "Payment onErrorLoadingWebPage " + s + " onErrorLoadingWebPage" + s1);
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Log.d("MerchantActivity", "Payment onBackPressedCancelTransaction");
            }
        });
    }

}
