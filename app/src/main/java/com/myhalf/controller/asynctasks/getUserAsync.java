package com.myhalf.controller.asynctasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.myhalf.model.backend.DBManager;
import com.myhalf.model.entities.UserSeeker;

import java.lang.ref.WeakReference;


public class getUserAsync extends AsyncTask<String, Void, UserSeeker> {

    private DBManager dbManager;
    private WeakReference<Activity> mActivity;
    private Implementation mImplementation;

    public getUserAsync( Activity activity, DBManager dbManager, Implementation implementation) {
        this.dbManager = dbManager;
        mActivity = new WeakReference<>(activity);
        mImplementation = implementation;
    }

    @Override
    protected void onPostExecute(UserSeeker userSeeker) {
        super.onPostExecute(userSeeker);
        mImplementation.onPostExecute(userSeeker);
    }

    @Override
    protected UserSeeker doInBackground(String... strings) {
        try {
            return (UserSeeker) dbManager.getUser(strings[0]);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(),e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mImplementation.onPreExecute();

        //TODO: make on progress animation
    }
    public interface Implementation {
        void onPostExecute(UserSeeker userSeeker);
        void onPreExecute();

    }
}
