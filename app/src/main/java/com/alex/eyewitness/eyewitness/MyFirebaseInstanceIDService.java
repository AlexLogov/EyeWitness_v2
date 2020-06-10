package com.alex.eyewitness.eyewitness;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Alex on 14.03.2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private final String fTokenID = "NOTIF_TOKEN_ID";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("q", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

        ServerWorker.SaveUser(refreshedToken,this.getApplicationContext() );

        DBHelper.getInstance(getApplication().getApplicationContext()).setStringVal(fTokenID, refreshedToken);
    }

    public static String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }
}
