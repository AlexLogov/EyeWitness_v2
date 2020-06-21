package com.alex.eyewitness.eyewitness.EveryJobs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

public class SaveMiddleCoords {
    private int UPDATE_INTERVAL = 12 * 100 * 1000;  /* 1200 secs */
    private int FASTEST_INTERVAL = 6 * 100 * 1000; /* 600 sec */

    public void runSaveMiddleJob(Context pContext) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(pContext));
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("some_key","some_value");
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(Every20MinuteJob.class)
                // uniquely identifies the job
                .setTag("my-unique-tag")
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(UPDATE_INTERVAL, FASTEST_INTERVAL))
                .setRecurring(true)
                .setExtras(myExtrasBundle)
                .setReplaceCurrent(true)
                .build();

        int result = dispatcher.schedule(myJob);
        if(result !=FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS)
        {
            Log.d("JOB_TAG", "ERROR ON SCHEDULE");
        }
    }



}
