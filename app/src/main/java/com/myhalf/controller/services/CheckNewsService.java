package com.myhalf.controller.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.myhalf.R;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.model.entities.UserSeeker;

public class CheckNewsService extends IntentService {
    DBManager DB_users = DBManagerFactory.getSeekerManager();
    static long activityId = 0;

    static int count = 1;
    int id = 0, startId = -1;
    boolean isRun = false;
    final String TAG = "testService";

    public CheckNewsService() {
        super("CheckNewsService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CheckNewsService(String name) {
        super(name);
    }


    private String serviceInfo() {
        return "service [" + id + "] startId = " + startId;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, serviceInfo() + " onDestroy ...");
        isRun = false;
        super.onDestroy();
    }


    private int minute() {
        return 1000 * 60;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //this.startId = startId;
        isRun = true;
        Log.d(TAG, serviceInfo() + " onHandleIntent start ...");
        try {
            while (isRun) {
                long maxId = DB_users.getMaxId();
                Log.d(TAG, "<<<<<<<<<<<<<<<   service active!   >>>>>>>>>>>>>>>>");
//            Log.d(TAG, serviceInfo() + " print ...");
                if ( activityId ==0) {
                    activityId = maxId;
                }else if (maxId != activityId) {
                    Notification.Builder nBuilder = new Notification.Builder(getBaseContext());
                    nBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
                    nBuilder.setContentTitle("There is new matches for you!");
                    nBuilder.setContentText("Some more users joined our cummonity!, You are welcome to check for new matches in our community..");
                    Notification notification = nBuilder.build();
                    Object obj = getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationManager notificationManager = (NotificationManager) obj;
                    notificationManager.notify(1234, notification);
//                    notificationManager.cancel(1234);
                    //methud here....
                    activityId = maxId;
                    Toast.makeText(getApplicationContext(), "hi! service ", Toast.LENGTH_LONG).show();
                }
                Thread.sleep(minute());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, serviceInfo() + " onHandleIntent end ...");
    }
}