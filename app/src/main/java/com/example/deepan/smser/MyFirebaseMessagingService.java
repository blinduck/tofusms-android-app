package com.example.deepan.smser;

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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.v("SMSTesting", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            data = remoteMessage.getData();
            String sendToNumber = data.get("sendToNumber");
            String message = data.get("message");

            Log.d("SMSTesting", "sendToNumber: " + sendToNumber);
            Log.d("SMSTesting", "message: " + message);
            this.sendSMS(sendToNumber, message);


//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }


    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();

            ArrayList<String> parts = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(phoneNo, null, parts, null, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static HashMap<String, String> jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }
        return map;

    }
}
