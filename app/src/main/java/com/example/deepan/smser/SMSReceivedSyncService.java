package com.example.deepan.smser;

import android.app.IntentService;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.Random;

/**
 * Created by deepan on 24/1/18.
 */

public class SMSReceivedSyncService extends IntentService {

    private ReceivedSMS sms;
    private AppDatabase database;
    private String androidId;

    public SMSReceivedSyncService() {
        super("SMSReceivedSyncService");
    }

    private void scheduleFutureSync(long smsId) {
        // In case initial sync fails
        Random rand = new Random();
        int jobId = rand.nextInt(10000);
        JobInfo.Builder builder = new JobInfo.Builder(
                jobId, new ComponentName(
                getApplicationContext(), SMSScheduleFutureSyncService.class));
        builder.setMinimumLatency(10 * 1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putLong("smsId", smsId);
        builder.setExtras(bundle);

        JobScheduler tm = (JobScheduler) getSystemService(getApplicationContext().JOB_SCHEDULER_SERVICE);
        tm.schedule(builder.build());

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sms = (ReceivedSMS) intent.getExtras().get("receivedSMS");
        sms.setAndroidId(androidId);
        Log.v("SMSTesting", "From worker: " + sms.getMessage());
        String url = "https://afteryou.co/v1/sms_received";
        database = AppDatabase.getAppDatabase(getApplicationContext());

        AndroidNetworking.post(url)
                .addBodyParameter(sms)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        sms.setSynced(true);
                        database.rSMSDao().update(sms);
                        Log.v("SMSTesting", "Successful Post");
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.v("SMSTesting", "Unsucessful: " + error.getErrorBody());
                        scheduleFutureSync(sms.getUid());
                    }
                });

    }
}
