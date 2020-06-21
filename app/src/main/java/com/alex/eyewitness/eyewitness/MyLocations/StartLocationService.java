package com.alex.eyewitness.eyewitness.MyLocations;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.alex.eyewitness.eyewitness.DBHelper;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StartLocationService {

    private long UPDATE_INTERVAL = 3 * 100 * 1000;  /* 300 secs */
    private long FASTEST_INTERVAL = 2 * 100 * 1000; /* 200 sec */

    private  FusedLocationProviderClient mFusedLocationClient;
    private  LocationRequest mLocationRequest;
    private  LocationCallback mLocationCallback;
    private static StartLocationService mSls;

    private StartLocationService(){

    }

    public static StartLocationService getInstance(){
        if (null == mSls){
            mSls = new StartLocationService();
        }
        return mSls;
    }

    public void init(Context pContext) {
        Log.d("eyewitness", "StartLocationService");

        final Context vCont = pContext;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(pContext);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(300);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(pContext);
        Task <LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener <LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d("eyewitness", "addOnSuccessListener");
                String mStr = locationSettingsResponse.getLocationSettingsStates().toString();
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });


        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    Log.d("eyewitness", "addOnFailureListener");
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    //resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                }
            }
        });

        mLocationCallback = new LocationCallback() {
            private Location currentLocation = null;
            private long locationUpdatedAt = Long.MIN_VALUE;

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("eyewitness", "LocationCallback");

                Location lastLocation = null;

                for (Location location : locationResult.getLocations()) {
                    if (lastLocation != null) {
                        if (location.getTime() < lastLocation.getTime()) {
                            lastLocation = location;
                        }
                    } else {
                        lastLocation = location;
                    }
                }
                if (lastLocation != null) {
                    Log.d("eyewitness", "LocationCallback loop");
                    String gLatityde = Double.toString(lastLocation.getLatitude());
                    String gLongitude = Double.toString(lastLocation.getLongitude());
                    Log.v("qwe123", "gLatityde='" + gLatityde + "' gLongitude='" + gLongitude + "'");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(lastLocation.getTime());

                    ContentValues cv = new ContentValues();

                    cv.put("lat", lastLocation.getLatitude());
                    cv.put("lng", lastLocation.getLongitude());
                    cv.put("type", lastLocation.getProvider());
                    cv.put("inserted", dateFormat.format(date));
                    cv.put("Accuracy", lastLocation.getAccuracy());

                    DBHelper.getInstance(vCont).saveCoords(cv);
                    //long rowID = db.insert("Coordinatestable_3", null, cv);
                    Toast.makeText(vCont, "Coordinates saved succesfull.", Toast.LENGTH_LONG).show();
                }
            };
        };
        startLocationUpdates(pContext);
    }


    private void startLocationUpdates(Context pContext) {
        Log.d("eyewitness", "startLocationUpdates");
        if (ActivityCompat.checkSelfPermission(pContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(pContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("eyewitness", "requestLocationUpdates");
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                Looper.myLooper() /* Looper */);
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        mFusedLocationClient = null;
        mLocationRequest = null;
        mLocationCallback = null;
    }

}
