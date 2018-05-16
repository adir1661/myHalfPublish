package com.myhalf.controller.asynctasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.myhalf.model.backend.DBManager;
import com.myhalf.model.entities.UserSeeker;

import java.lang.ref.WeakReference;

/**
 * Created by Adir on 5/15/2018.
 */
public class getUserAsync extends AsyncTask<String, Void, UserSeeker> {

    DBManager dbManager;
    WeakReference<Activity> mActivity;
    Implementation completeListener;

    public getUserAsync( Activity activity, DBManager dbManager, Implementation onCompleteListener) {
        this.dbManager = dbManager;
        mActivity = new WeakReference<>(activity);
        completeListener = onCompleteListener;
    }

    @Override
    protected void onPostExecute(UserSeeker userSeeker) {
        super.onPostExecute(userSeeker);
        completeListener.onPostExecute(userSeeker);
    }

    @Override
    protected UserSeeker doInBackground(String... strings) {
        return (UserSeeker) dbManager.getUser(strings[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        completeListener.onPreExecute();

        //TODO: make on progress animation
    }
    public interface Implementation {
        void onPostExecute(UserSeeker userSeeker);
        void onPreExecute();

    }
}
