package com.alex.eyewitness.eyewitness.EveryJobs;

import com.alex.eyewitness.eyewitness.Coordinates;
import com.alex.eyewitness.eyewitness.CoordinatesWorker;
import com.alex.eyewitness.eyewitness.DBHelper;
import com.alex.eyewitness.eyewitness.MyLocations.StartLocationService;
import com.alex.eyewitness.eyewitness.ServerWorker;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class EveryOneDayJob extends JobService {


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // запуск сервиса определения координат, если он еще не запущен
        StartLocationService fStartLocationService = new StartLocationService();
        fStartLocationService.StartLocationService(this);

        // удаление старых записей по координатам
        DBHelper.getInstance(this).delOldValues();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
