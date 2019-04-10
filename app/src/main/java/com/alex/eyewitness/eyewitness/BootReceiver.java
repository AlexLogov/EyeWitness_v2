package com.alex.eyewitness.eyewitness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
/*
        Log.d("BootReceiver", "onReceive: " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, GeoService2.class));
            Toast toast = Toast.makeText(context.getApplicationContext(),
                "GeoService2 succesfull start from BOOT.", Toast.LENGTH_LONG);
        toast.show();
        }
        */
    }
}
