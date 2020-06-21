package com.alex.eyewitness.eyewitness.EveryJobs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alex.eyewitness.eyewitness.Coordinates;
import com.alex.eyewitness.eyewitness.CoordinatesWorker;
import com.alex.eyewitness.eyewitness.MyFirebaseInstanceIDService;
import com.alex.eyewitness.eyewitness.MyLocations.StartLocationService;
import com.alex.eyewitness.eyewitness.ServerWorker;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class Every20MinuteJob extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // получить средние координаты
        Coordinates fC = CoordinatesWorker.getMediumCoordinates(getApplicationContext());

        ServerWorker.SaveMiddleCoords(fC,getApplicationContext() );


        Log.d("eyewitness", "GeoService2 after onStartCommand" );
        try {
            StartLocationService fStartLocationService = StartLocationService.getInstance();
            fStartLocationService.init(this);
        } catch (Exception ex) {
            Log.d("eyewitness", "GeoService2 Exception1 ", ex );
        }

        String fM = MyFirebaseInstanceIDService.getToken();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }





}
