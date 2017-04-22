package com.example.deepan.smser;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.provider.Settings.Secure;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String androidId = Secure.getString(
                getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID
        );
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.v("SMSTesting", "androidID " +androidId);
        Log.v("SMSTesting", "token " + refreshedToken);

    }


    public void getSMSPermission(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
            Toast.makeText(getApplicationContext(), "Already Have SMS Permission", Toast.LENGTH_LONG).show();
        }
//        sendSMS("+6598164254", "testing");
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void postMessage(View view) {
//        String url = "https://requestb.in/wssm90ws?inspect";
//        String charset = "UTF-8";
//        String param1 = "value1";
//        String param2 = "value2";
//
//        String query = String.format("param1=%s&param2=%s",
//                URLEncoder.encode(param1, charset),
//                URLEncoder.encode(param2, charset));
//
//        URLConnection connection = new URL(url).openConnection();
//        connection.setDoOutput(true); // Triggers POST.
//        connection.setRequestProperty("Accept-Charset", charset);
//        connection.setRequestProperty("Content-Type", "application/json");
//
//        try (OutputStream output = connection.getOutputStream()) {
//            output.write(query.getBytes(charset));
//        }
//        InputStream response = connection.getInputStream();
//        Toast.makeText(getApplicationContext(), "Tofu", Toast.LENGTH_LONG).show();
//        new CallAPI(getApplicationContext()).execute("+6598164254", "message contents");
        Toast.makeText(getApplicationContext(), "Tofu", Toast.LENGTH_LONG).show();

    }

}
