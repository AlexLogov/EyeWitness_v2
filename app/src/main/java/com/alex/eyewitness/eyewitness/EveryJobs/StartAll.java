package com.alex.eyewitness.eyewitness.EveryJobs;


import android.content.Context;

public class StartAll {


    public void startAll(Context pContext){

        SaveMiddleCoords fMiddle = new SaveMiddleCoords();
        fMiddle.runSaveMiddleJob(pContext);

        RunGeoLocationShedule fRunGeoLocationShedule = new RunGeoLocationShedule();
        fRunGeoLocationShedule.runEveryOneDayJob(pContext);


    }





}
