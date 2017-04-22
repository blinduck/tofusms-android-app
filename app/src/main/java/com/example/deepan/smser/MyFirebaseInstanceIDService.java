package com.example.deepan.smser;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by deepan on 13/4/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    // Token dpQsE20FDPQ:APA91bHUllrC01z4SBrd4NDx91DRMB-4_MO_kKQsm8wsNh0JtfXsaUur0b2r7_yv3PlPS5EiRtPWOPK_D8RRL-O9PvM1EM7CAwe2v2-j5KFYZxP8flf28WkynNpsTRYeKEQBpIwKpgZJ
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("SMSTesting", "Refreshed Token: " + refreshedToken);

    }
}
