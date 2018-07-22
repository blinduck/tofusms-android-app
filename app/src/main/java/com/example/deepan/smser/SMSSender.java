package com.example.deepan.smser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by deepan on 22/1/18.
 */

public class SMSSender {
    private Context mContext;
    private String url = "https://afteryou.co/v1/update_sent_sms_status";

    public SMSSender(Context context) {
        mContext = context;
    }

    private void log(String message) {
        Log.v("SMSTesting", message);
    }

    private void makeNetworkCall(JSONObject json)  {
        AndroidNetworking.put(url)
                .setContentType("application/json")
                .addJSONObjectBody(json)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        log("Response Okay: " + String.valueOf(response));
                    }

                    @Override
                    public void onError(ANError anError) {
                        log("Response Error: " + anError.getErrorBody());
                    }
                });

    }

    private void updateSentStatus(int resultCode, int smsId) {
        boolean sentConfirm = false;
        String sentConfirmMessage = "";
        switch (resultCode) {
            case Activity.RESULT_OK:
                sentConfirm = true;
                log("ReceivedSMS sent");
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                sentConfirmMessage = "Generic failure";
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                sentConfirmMessage = "No Service";
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                sentConfirmMessage = "Null PDU";
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                sentConfirmMessage = "Radio Off";
                break;
        }
        log("Sent Confirm: " + sentConfirmMessage);
        JSONObject json = new JSONObject();
        try {
            json.put("smsId", smsId);
            json.put("sentConfirm", sentConfirm);
            if (!sentConfirm) {
                json.put("sentConfirmMessage", sentConfirmMessage);
            }
            makeNetworkCall(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateDeliveredStatus(int resultCode, int smsId) {
        boolean deliverConfirm = false;
        switch (resultCode) {
            case Activity.RESULT_OK:
                deliverConfirm = true;
                log("ReceivedSMS delivered");
                break;
            case Activity.RESULT_CANCELED:
                deliverConfirm = false;
                log("ReceivedSMS not delivered");
                break;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("smsId", smsId);
            json.put("deliverConfirm", deliverConfirm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        makeNetworkCall(json);

    }

    public void sendSMS(String phoneNo, String msg, final int smsId) {

        String SENT = "SMS_SENT_" + smsId;
        String DELIVERED = "SMS_DELIVERED_" + smsId;

        PendingIntent sentPI = PendingIntent.getBroadcast(
                mContext,
                0,
                new Intent(SENT),
                0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(
                mContext,
                0,
                new Intent(DELIVERED),
                0);

        //---when the ReceivedSMS has been sent---
        BroadcastReceiver sentReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateSentStatus(getResultCode(), smsId);
            }
        };
        mContext.registerReceiver(sentReciever, new IntentFilter(SENT));

        //---when the ReceivedSMS has been delivered---
        final BroadcastReceiver deliverReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateDeliveredStatus(getResultCode(), smsId);
            }
        };
        mContext.registerReceiver(deliverReceiver, new IntentFilter(DELIVERED));

        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        sentIntents.add(sentPI);
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
        deliveryIntents.add(deliveredPI);

        try {
            SmsManager smsManager = SmsManager.getDefault();

            ArrayList<String> parts = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(
                    phoneNo,
                    null,
                    parts, sentIntents, deliveryIntents);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
