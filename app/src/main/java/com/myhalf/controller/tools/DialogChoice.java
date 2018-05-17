package com.myhalf.controller.tools;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.myhalf.R;
import com.myhalf.controller.myUser;
import com.myhalf.model.entities.UserSeeker;

import java.util.Arrays;
import java.util.List;


public class DialogChoice {

    private static UserSeeker activityUser = myUser.getUserSeeker();
    static Context mContext;
//    private boolean myChoiceFlag;


    // ---------Dialog Multi-Choice----------
    public static void dialogMultiChoice(final String[] stringOptions, final String title, final EditText editText) {
        final boolean[] boolOption = new boolean[stringOptions.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        final boolean[] flag = {false};
        builder.setMultiChoiceItems(stringOptions, boolOption, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                boolOption[which] = isChecked;
                String currentItem = stringOptions[which];
//                myChoiceFlag = true;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (myChoiceFlag) {
                        if (title.equals(R.string.view)){
                            activityUser.getAboutMe().setView(allChoicesToArray(stringOptions, boolOption));
//                            myChoiceFlag = false;
                            markAsSigned(editText,null);
                        }else if (title.equals(R.string.witness)) {
                            activityUser.getAboutMe().setWitness(allChoicesToArray(stringOptions, boolOption));
//                            myChoiceFlag = false;
                            markAsSigned(editText,null);
                        }
                }
        });
        builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // ----------------- help function -----------------

    public static List<String> allChoicesToArray(String[] str, boolean[] bool) {
        int arraySize = countTrues(bool);
        String[] array = new String[arraySize];
        int j = 0;
        for (int i = 0; i < str.length; i++)
            if (bool[i]) {
                array[j] = str[i];
                j++;
            }
        return Arrays.asList(array);
    }

    private static int countTrues(boolean[] bool) {
        int count = 0;
        for (int i = 0; i < bool.length; i++) {
            if (bool[i]) {
                count++;
            }

        }
        return count;
    }

    private static void markAsSigned(EditText editText, @Nullable String myChoice) {

        Drawable img = mContext.getResources().getDrawable(R.drawable.ic_done_all_white_24dp);
        int h = img.getIntrinsicHeight();
        int w = img.getIntrinsicWidth();
        img.setBounds(0, 0, w, h);

        if (myChoice != "" && myChoice != null)
            editText.setText(myChoice);
        else
            editText.setText(editText.getHint());
//        editText.setCompoundDrawables(null, null, img, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            ObjectAnimator animator = ObjectAnimator.ofArgb(editText,"textColor" , ContextCompat.getColor(mContext.getApplicationContext(), R.color.black)
                    ,ContextCompat.getColor(mContext.getApplicationContext(), R.color.marked_blue));
            animator.setDuration(1000);
            animator.setStartDelay(300);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setEvaluator(new ArgbEvaluator());
            animator.start();
        }else {
            editText.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.marked_blue));
        }
    }


}
