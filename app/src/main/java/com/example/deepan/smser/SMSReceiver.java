package com.example.deepan.smser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by deepan on 13/4/17.
 */

public class SMSReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();

                AppDatabase database = AppDatabase.getAppDatabase(context);

                ReceivedSMS sms = new ReceivedSMS();
                sms.setContact(sender);
                sms.setMessage(message);
                sms.setSynced(false);
                long uid = database.rSMSDao().add(sms);
                sms.setUid(uid);
                Log.v("SMSTesting", "UID: "+ uid);

                Intent sync_intent = new Intent(context, SMSReceivedSyncService.class);
                sync_intent.putExtra("receivedSMS", sms);
                context.startService(sync_intent);
                // prevent any other broadcast receivers from receiving broadcast
                // abortBroadcast();
            }
        }
    }
}
