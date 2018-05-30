package com.myhalf.controller.asynctasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.myhalf.R;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.datasource.Tools;
import com.myhalf.model.entities.UserSeeker;

import java.lang.ref.WeakReference;
import java.nio.channels.Channels;

public class RegisterAsync extends AsyncTask<UserSeeker, ProgressBar, Void> {
    private DBManager dbManager;
    private WeakReference<Activity> mActivity;

    public RegisterAsync(Activity activity, DBManager dbManager) {
        this.dbManager = dbManager;
        mActivity = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(UserSeeker... userSeeker) {
        // ----------------- Send to data base -----------------------
        String id = dbManager.addUser(Tools.userSeekerToContentValues(userSeeker[0]));
        userSeeker[0].setId(id);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(mActivity.get(),
                R.string.registrationDone, Toast.LENGTH_LONG).show();
    }
}
