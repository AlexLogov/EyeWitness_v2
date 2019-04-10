package com.alex.eyewitness.eyewitness.EveryJobs;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alex.eyewitness.eyewitness.Coordinates;
import com.alex.eyewitness.eyewitness.CoordinatesWorker;
import com.alex.eyewitness.eyewitness.ServerWorker;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class Every20MinuteJob extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // получить средние координаты
        Coordinates fC = CoordinatesWorker.getMediumCoordinates(getApplicationContext());



        ServerWorker.SaveMiddleCoords(fC,getApplicationContext() );

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }





}
