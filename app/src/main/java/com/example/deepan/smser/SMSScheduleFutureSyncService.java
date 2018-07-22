package com.example.deepan.smser;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by deepan on 24/1/18.
 */

public class SMSScheduleFutureSyncService extends JobService {
    private ReceivedSMS sms;
    private AppDatabase database;
    private Context context;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        long id = jobParameters.getExtras().getLong("smsId");
        Log.v("SMSTesting", "id is: " + Long.toString(id));
        context = getApplicationContext();
        database = AppDatabase.getAppDatabase(context);
        sms = database.rSMSDao().getById(id);
        Log.v("SMSTesting", "Message: " + sms.getMessage());
        Intent sync_intent = new Intent(context, SMSReceivedSyncService.class);
        sync_intent.putExtra("receivedSMS", sms);
        context.startService(sync_intent);
//        sms  = (ReceivedSMS) jobParameters.getExtras().get("receivedSMS");
//        Log.v("SMSTesting", "Message: " + sms.getMessage());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
