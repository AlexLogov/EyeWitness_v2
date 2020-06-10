package com.alex.eyewitness.eyewitness;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.SeekBar;

import com.alex.eyewitness.eyewitness.messages.NotificationInfo;
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
import com.alex.eyewitness.eyewitness.Constants;

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
        double fPresition = 0.0;
        Date fBeginDateOfAction = null;
        Date fEndDateOfAction = null;
        String fDescription = "";
        String fUserID = "";
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

            NotificationInfo notificationInfo = new NotificationInfo(
                    fLat,
                    fLng,
                    fPresition,
                    fBeginDateOfAction,
                    fEndDateOfAction,
                    fDescription,
                    fUserID,
                    fMinDistanle);

            createNotificationChannel();
            notifyMainActivity(notificationInfo);
            showNotification(notificationInfo);
            //GoogleMap vMap = MenyActivity.getvMap();

            /*
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
            */
        }


    }
    private void notifyMainActivity(NotificationInfo notificationInfo) {
        Intent intent = new Intent();
        intent.setAction(Constants.NOTIFICATION_BROADCAST_RECEIVER_MESSAGE_RECEIVED);
        intent.putExtra(Constants.PARAM_NOTIFICATION_INFO, notificationInfo);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Eye";
            String description = "PelpMyPlz";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Eye", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showNotification(NotificationInfo notificationInfo) {

        Intent intentNotificationClicked = new Intent(this, GeoService2.class);
        intentNotificationClicked.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentNotificationClicked.putExtra(Constants.PARAM_NOTIFICATION_INFO, notificationInfo);
        PendingIntent resultIntentNotificationClicked = PendingIntent.getActivity(this, 0, intentNotificationClicked, PendingIntent.FLAG_ONE_SHOT);

        String title = "Просьба о помощи";
        String message = notificationInfo.message;

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this, "Eye")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntentNotificationClicked);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID = Integer.parseInt(new SimpleDateFormat("HHmmssSSS").format(new Date()));
        notificationManager.notify(notificationID, mNotificationBuilder.build());
    }
    private void scheduleJob() {
    }
}
