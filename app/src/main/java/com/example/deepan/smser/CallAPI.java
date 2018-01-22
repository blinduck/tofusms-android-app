package com.example.deepan.smser;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.provider.Settings.Secure;

/**
 * Created by deepan on 13/4/17.
 */


public class CallAPI extends AsyncTask<String, String, String> {
    // Class for informing server that a new sms has been received.

    private class SMSData {
        public String contact;
        public String message;
        public String androidId ;
    }


    private Context mContext;
    private String androidId;

    public CallAPI(Context context) {
        mContext = context;
        androidId = Secure.getString(mContext.getContentResolver(),
                                                    Secure.ANDROID_ID);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {

//        String url = "http://django.afteryou.co/v1/sms_received";
        String url = "https://afteryou.co/v1/sms_received";
        String charset = "UTF-8";
        String json;
        URLConnection connection;

        try {
            SMSData sd = new SMSData();
            sd.contact = params[0];
            sd.message = params[1];
            sd.androidId = androidId;

            json = new Gson().toJson(sd);

//            JSONObject jsonObject = new JSONObject();
//            jsonObject.accumulate("contact[number]", params[0]);
//            jsonObject.accumulate("message", params[1]);
//            jsonObject.accumulate("androidId", androidId);
//            json = jsonObject.toString();


//            query = String.format("param1=%s&param2=%s",
//                    URLEncoder.encode(param1, charset),
//                    URLEncoder.encode(param2, charset));
        } catch (Exception e) {
            return "encoding error: %s".format(e.toString());
        }
        try {

            connection = new URL(url).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json");
            try (OutputStream output = connection.getOutputStream()) {
                output.write(json.getBytes(charset));
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());

            return e.getMessage();

        }

        try {
            InputStream response = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "what";
    }


    @Override
    protected void onPostExecute(String result) {
        //Update the UI
    }
}