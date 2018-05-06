package com.myhalf.controller.tools;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.myhalf.model.backend.DBManager;
import com.myhalf.model.datasource.Tools;
import com.myhalf.model.entities.UserSeeker;

import java.lang.ref.WeakReference;

/**
 * Created by Adir on 5/3/2018.
 */ //------------------------ button -----------------
//shouldn't stay on EDITPROFLE
public class UpdateAsync extends AsyncTask<UserSeeker, ProgressBar, Void> {
    UserSeeker seekerOfTheActivity;
    DBManager dbManager;
    WeakReference<Activity> mActivity;

    public UpdateAsync(DBManager database, UserSeeker currentUser, Activity activity) {
        dbManager = database;
        seekerOfTheActivity = currentUser;
        mActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected Void doInBackground(UserSeeker... userSeeker) {
        if (userSeeker[0].getId()!=null)
        dbManager.updateUser(userSeeker[0].getId(), Tools.userSeekerToContentValues(userSeeker[0]));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (seekerOfTheActivity.getId() == "0")
            Toast.makeText(mActivity.get(),
                    "לא עודכן, אין משתמש", Toast.LENGTH_LONG).show();
    }
}
