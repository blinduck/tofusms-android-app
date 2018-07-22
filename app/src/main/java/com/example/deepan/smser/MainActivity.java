package com.example.deepan.smser;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.provider.Settings.Secure;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    private SMSSender sender;
    private AppDatabase database;
    private ListView listView;
    private Context context;
    private int mJobId = 0;
    ArrayList<ReceivedSMS> smses;
    List<ReceivedSMS> smsesFromDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String androidId = Secure.getString(
                getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID
        );
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        context = getApplicationContext();

        Log.v("SMSTesting", "androidID " + androidId);
        Log.v("SMSTesting", "token " + refreshedToken);
        database = AppDatabase.getAppDatabase(getApplicationContext());
        AndroidNetworking.initialize(getApplicationContext());
        initializeList();
    }

    public void initializeList() {
        smses = new ArrayList<ReceivedSMS>();
        smsesFromDb = database.rSMSDao().last10();
        smses.addAll(smsesFromDb);
        SMSAdapter adapter = new SMSAdapter(this, smses);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    public void getSMSPermission(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
            Toast.makeText(getApplicationContext(), "Already Have ReceivedSMS Permission", Toast.LENGTH_LONG).show();
        }
    }
    public void refreshMessages(View view) {
        initializeList();
        Toast.makeText(context, "List Updated", Toast.LENGTH_SHORT).show();
    }


    public void test(View view) {
        SMSSender sender = new SMSSender(context);
        sender.sendSMS(
                "+6598164254",
                "testing testing",
                10
        );
    }


    private class SMSAdapter extends ArrayAdapter<ReceivedSMS> {
        public SMSAdapter(Context context, ArrayList<ReceivedSMS> smses) {
            super(context, 0, smses);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ReceivedSMS sms = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item, parent, false);
            }
            // Lookup view for data population
            TextView uid = (TextView) convertView.findViewById(R.id.uid);
            TextView message = (TextView) convertView.findViewById(R.id.message);
            TextView synced = (TextView) convertView.findViewById(R.id.synced);
            // Populate the data into the template view using the data object
            uid.setText(String.valueOf(sms.getUid()));
            message.setText(sms.getMessage());
            if (sms.isSynced()) {
                synced.setText("\u2713");
            } else {
                synced.setText("\u2717");
            }
            // Return the completed view to render on screen
            return convertView;
        }
    }

}
