package com.example.deepan.smser;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by deepan on 13/4/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Map<String, String> data;
//    String SENT = "SMS_SENT";
//    String DELIVERED = "SMS_DELIVERED";
//    PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
//            SENT), 0);
//
//    // DELIVER PendingIntent
//    PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
//            new Intent(DELIVERED), 0);
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            data = remoteMessage.getData();
            Log.v("SMSTesting", data.toString());
            String sendToNumber = data.get("sendToNumber");
            String message = data.get("message");
            int smsId = Integer.parseInt(data.get("smsId"));

            Log.d("SMSTesting", "sendToNumber: " + sendToNumber);
            Log.d("SMSTesting", "message: " + message);

            // 1. Save to database
            // 2. set delivered to false
            // 3.
            SMSSender sender = new SMSSender(getApplicationContext());
            sender.sendSMS(sendToNumber, message, smsId);


//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }
    }

}
