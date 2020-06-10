package com.alex.eyewitness.eyewitness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("eyewitness", "onReceive: " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("eyewitness", "startForegroundService" );
                context.startForegroundService(new Intent(context, GeoService2.class));
            }else{
                Log.d("eyewitness", "startService" );
                context.startService(new Intent(context, GeoService2.class));
            }

            Toast toast = Toast.makeText(context.getApplicationContext(),
                    "GeoService2 succesfull start from BOOT.", Toast.LENGTH_LONG);
            toast.show();
        }

    }
}
