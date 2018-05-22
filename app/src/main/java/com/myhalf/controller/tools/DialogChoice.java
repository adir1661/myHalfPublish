package com.myhalf.controller.tools;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myhalf.R;
import com.myhalf.controller.MyUser;
import com.myhalf.model.entities.UserSeeker;

import java.util.Arrays;
import java.util.List;

public class DialogChoice {

    private static String myChoice;
    private static Boolean myChoiceFlag;
    private static UserSeeker activityUser = MyUser.getUserSeeker();
    static int count = 0;

    // ---------Dialog Single-Choice----------
    public static void dialogSingleChoice(final Activity activity, final String[] stringOptions,
                                          final String title, final EditText editText, final View view)
    {
        final String[] OptionsStrings = stringOptions;
        final int choiceMarked = -1;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);

        builder.setSingleChoiceItems(OptionsStrings, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //enter the choice to the field in the class
                myChoice = OptionsStrings[whichButton];
            }
        });

        builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    Resources res = activity.getResources();
                    if (title.equals(res.getString(R.string.status)) ) {
                        activityUser.getAboutMe().setStatus(myChoice);
                        markAsSigned(activity, editText, myChoice);
                    // case of view is etStatus:
                    if (view != null) {
                        if (myChoice.equals(res.getString(R.string.divorcee)) ||
                                myChoice.equals(res.getString(R.string.wDivorcee)) ||
                                myChoice.equals(res.getString(R.string.widow)) ||
                                myChoice.equals(res.getString(R.string.widower))) {
                            view.setVisibility(View.VISIBLE);
                        } else
                            view.setVisibility(View.GONE);
                        }
                    }
                    }
//                    myChoiceFlag = false;


        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myChoiceFlag = false;
            }
        });
        builder.setOnDismissListener(new AlertDialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                myChoiceFlag = false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


            // ---------Dialog Multi-Choice----------
    public static void dialogMultiChoice(final Activity activity, final String[] stringOptions, final String title, final EditText editText) {
        final boolean[] boolOption = new boolean[stringOptions.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        final boolean[] flag = {false};
        builder.setMultiChoiceItems(stringOptions, boolOption, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                boolOption[which] = isChecked;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            if (title.equals(R.string.view)){
                activityUser.getAboutMe().setView(allChoicesToArray(stringOptions, boolOption));
                markAsSigned(activity, editText,null);
            }else if (title.equals(R.string.witness)) {
                activityUser.getAboutMe().setWitness(allChoicesToArray(stringOptions, boolOption));
                markAsSigned(activity, editText,null);
            }
                }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.cancel), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // ---------Dialog Multi-Choice With Limited Choices----------
    public static void dialogMultiChoiceLimited(final Activity activity, final String[] stringOptions, final String title, final EditText editText, final int maxChoices) {

        final boolean[] itemsChecked = new boolean[stringOptions.length];
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMultiChoiceItems(stringOptions, itemsChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                    if(count < maxChoices) {
                        itemsChecked[which] = isChecked;
                        count++;
                    }else{
                        Toast.makeText(activity, "Impossible to choose more than two choices", Toast.LENGTH_LONG).show();
                    }
                }else{
                    count--;
                }
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            if (title.equals(R.string.view)){
                activityUser.getAboutMe().setView(allChoicesToArray(stringOptions, itemsChecked));
                markAsSigned(activity, editText,null);
            }else if (title.equals(R.string.witness)) {
                activityUser.getAboutMe().setWitness(allChoicesToArray(stringOptions, itemsChecked));
                markAsSigned(activity, editText,null);
            }
            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.cancel), null);
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

    private static void markAsSigned(Activity activity, EditText editText, @Nullable String myChoice) {

        Drawable img = activity.getResources().getDrawable(R.drawable.ic_done_all_white_24dp);
        int h = img.getIntrinsicHeight();
        int w = img.getIntrinsicWidth();
        img.setBounds(0, 0, w, h);

        if (myChoice != "" && myChoice != null)
            editText.setText(myChoice);
        else
            editText.setText(editText.getHint());
//        editText.setCompoundDrawables(null, null, img, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            ObjectAnimator animator = ObjectAnimator.ofArgb(editText,"textColor" , ContextCompat.getColor(activity.getApplicationContext(), R.color.black)
                    ,ContextCompat.getColor(activity.getApplicationContext(), R.color.marked_blue));
            animator.setDuration(1000);
            animator.setStartDelay(300);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setEvaluator(new ArgbEvaluator());
            animator.start();
        }else {
            editText.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.marked_blue));
        }
    }


}
