package com.myhalf.controller.asynctasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.myhalf.R;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.datasource.Tools;
import com.myhalf.model.entities.UserSeeker;

import java.lang.ref.WeakReference;

public class RegisterAsync extends AsyncTask<UserSeeker, ProgressBar, Void> {
    private DBManager dbManager;
    private WeakReference<Activity> mActivity;
    private Implementation implementation;

    public RegisterAsync(Activity activity, DBManager dbManager,Implementation implementation) {
        this.dbManager = dbManager;
        mActivity = new WeakReference<>(activity);
        this.implementation = implementation;
    }

    @Override
    protected Void doInBackground(UserSeeker... userSeeker) {
        // ----------------- Send to data base -----------------------
        String id = dbManager.addUser(userSeeker[0]);
        userSeeker[0].setId(id);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(mActivity.get(),
                R.string.registrationDone, Toast.LENGTH_LONG).show();
        implementation.onPostExecute();
    }

    public interface Implementation {
        void onPostExecute();
    }
}
