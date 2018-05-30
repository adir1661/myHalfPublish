package com.myhalf.controller.services;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.datasource.PhpTools;
import com.myhalf.model.datasource.Tools;
import com.myhalf.model.entities.UserSeeker;

public class MyJobService extends JobService {
    DBManager DB_users = DBManagerFactory.getSeekerManager();


    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        try {
            if (jobParameters.getTag() == Finals.App.JOB_UPDATE) {
                UserSeeker userSeeker = (UserSeeker) jobParameters.getExtras().get(Finals.App.USER_SEEKER);
                DB_users.updateUser(userSeeker.getId(), userSeeker);
                return true;
            } else {
                throw new Exception("there is error in job service bundle probably didn't came on the right way");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
