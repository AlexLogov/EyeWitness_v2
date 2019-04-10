package com.alex.eyewitness.eyewitness;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("w", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("e", "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ false) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                try {
                    handleNow(remoteMessage.getData());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("s", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleNow(Map <String, String> pData) throws ParseException {
        double fLat = 0;
        double fLng = 0;
        double fPresition;
        Date fBeginDateOfAction = null;
        Date fEndDateOfAction = null;
        String fDescription;
        String fUserID;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String fKey : pData.keySet()) {
            String fValue = pData.get(fKey);
            switch (fKey) {
                case "lat":
                    fLat = Double.parseDouble(fValue);
                    break;
                case "lng":
                    fLng = Double.parseDouble(fValue);
                    break;
                case "pres":
                    fPresition = Double.parseDouble(fValue);
                    break;
                case "boa":
                    fBeginDateOfAction = dateFormat.parse(fValue);
                    break;
                case "eoa":
                    fEndDateOfAction = dateFormat.parse(fValue);
                    break;
                case "desc":
                    fDescription = fValue;
                    break;
                case "uid":
                    fUserID = fValue;
                    break;
            }
        }
        if (fBeginDateOfAction != null && fEndDateOfAction != null){
            ArrayList<Coordinates> fLastCoords = DBHelper.getInstance(this).getLastCoords(200);
            Double fMinDistanle = CoordinatesWorker.genMinDistance(fLng, fLat,fLastCoords );


            GoogleMap vMap = MenyActivity.getvMap();
            if (fMinDistanle < 0.002){
                vMap.addMarker(new MarkerOptions()
                        .title("Pos " + Integer.toString(1))
                        //.snippet("At " + dateFormat.format(fLastCoords.get(i).getInserted()))
                        .position(vMap.getCameraPosition().target).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }else{
                vMap.addMarker(new MarkerOptions()
                        .title("Pos " + Integer.toString(1))
                        //.snippet("At " + dateFormat.format(fLastCoords.get(i).getInserted()))
                        .position(new LatLng(fLat, fLng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }


    }

    private void scheduleJob() {
    }
}
