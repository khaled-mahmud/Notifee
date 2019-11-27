package com.pgdit101.iit.notifee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.security.Signature;
import java.util.concurrent.ExecutionException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class ApiActivity extends AppCompatActivity {

    String upc_code;
    String auth_key = "Ca84J5r0q3Ec1Io1";
    String app_key = "/4bliPUjRivm";
    String signature;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        TextView textView = findViewById(R.id.textView);

        upc_code = getIntent().getExtras().getString("upc_code");

        // $signature = base64_encode(hash_hmac('sha1', $upc_code, $auth_key, $raw_output = true));

        try {
            signature = hash_hmac(upc_code, auth_key);

        } catch (Exception e) {

            Log.wtf("HMAC:","stop");
            textView.setText("Error!");
            e.printStackTrace();
        }


        // $url = 'https://digit-eyes.com/gtin/v2_0/?upc_code='. $upc_code .'&app_key='. $app_key .'&language=en&field_names=all&signature='. $signature .'';
        String url = "https://digit-eyes.com/gtin/v2_0/?upc_code=" + upc_code + "&app_key=" + app_key + "&language=en&field_names=all&signature=" + signature + "";
        textView.setText(signature);


        // IMplementing AsyncTask
        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            result = getRequest.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }




    private String hash_hmac(String upc_code, String auth_key) throws Exception{

        Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
        //byte[] string = upc_code.getBytes();
        //String stringInBase64 = Base64.encodeToString(string, Base64.DEFAULT);
        SecretKeySpec secretKey = new SecretKeySpec(auth_key.getBytes(), "HmacSHA1");
        sha1_HMAC.init(secretKey);
        String hash = Base64.encodeToString(sha1_HMAC.doFinal(upc_code.getBytes()), Base64.DEFAULT);
        return hash;

    }
}
