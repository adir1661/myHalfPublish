package com.myhalf.controller.tools;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class MyFragmentManager {

    public static void replaceFragmentInContainer(Activity activity, Fragment targetFragment, int containerId) {

        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        // Replace fragmentCotainer with your container id
        Fragment currentFragment = activity.getFragmentManager().findFragmentById(containerId);
        transaction.replace(containerId, targetFragment);
        addToBackStack(targetFragment, transaction, currentFragment);
        // Commit the transaction
        transaction.commit();
    }

    public static void addToBackStack(Fragment targetFragment, FragmentTransaction transaction, Fragment currentFragment) {
        //if the current fragment not equal to the last fragment in the backStack
        if (!(currentFragment.getClass().equals(targetFragment.getClass())))
            transaction.addToBackStack(null);
    }
}
