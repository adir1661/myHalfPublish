package com.myhalf.controller.tools;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myhalf.R;
import com.myhalf.controller.activities.MyUser;
import com.myhalf.model.entities.UserSeeker;

import java.util.List;

public class DialogChoice {

    private static String myChoice;
    private static UserSeeker activityUser = MyUser.getUserSeeker();
    static int count = 0;


    // ---------Dialog Single-Choice----------
    public static void dialogSingleChoice(final Activity activity, final String[] stringOptions,
                                          final String title, final EditText editText, final View view)
    {
        final String[] OptionsStrings = stringOptions;
        myChoice = null;
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
                        OtherTools.markAsSigned(activity, editText, myChoice);
                        // case of view is etStatus - open radio button:
                        if (view != null) {
                            if (!myChoice.equals(res.getString(R.string.single)) &&
                                !myChoice.equals(res.getString(R.string.wSingle)) )

                                    view.setVisibility(View.VISIBLE);
                             else
                                view.setVisibility(View.GONE);
                        }
                    } else if (title.equals(res.getString(R.string.livingArea)) ) {
                        activityUser.getAboutMe().setLivingArea(myChoice);
                        OtherTools.markAsSigned(activity, editText, myChoice);
                    }
                    }
//                    myChoiceFlag = false;


        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // ---------Dialog Multi-Choice----------
    public static void dialogMultiChoice(final Activity activity, final String[] stringOptions,
                                         final String title, final EditText editText ) {
        final boolean[] itemsChecked = new boolean[stringOptions.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMultiChoiceItems(stringOptions, itemsChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                itemsChecked[which] = isChecked;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Resources res =  activity.getResources();

                if (title.equals(res.getString(R.string.view))){
                    List<String> listOfChoices = OtherTools.allChoicesToArray(stringOptions, itemsChecked);
                    activityUser.getAboutMe().setView(listOfChoices);
                    String myChoices = OtherTools.ListToString(listOfChoices);
                    OtherTools.markAsSigned(activity, editText, myChoices);
                }else if (title.equals(res.getString(R.string.witness))) { //
                    List<String> listOfChoices = OtherTools.allChoicesToArray(stringOptions, itemsChecked);
                    activityUser.getAboutMe().setWitness(listOfChoices);
                    String myChoices = OtherTools.ListToString(listOfChoices);
                    OtherTools.markAsSigned(activity, editText, myChoices);
                }
            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.cancel), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // ---------Dialog Multi-Choice With Limited Choices----------
    public static void dialogMultiChoiceLimited(final Activity activity, final String[] stringOptions,
                                                final String title, final EditText editText, final int maxChoices) {
        count = 0;
        final boolean[] itemsChecked = new boolean[stringOptions.length];
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMultiChoiceItems(stringOptions, itemsChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                count += isChecked ? 1 : -1;
                itemsChecked[which] = isChecked;

                if (count > maxChoices) {
                    Toast.makeText(activity, "You selected too many.", Toast.LENGTH_SHORT).show();
                    itemsChecked[which] = false;
                    count--;
                    ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                }
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Resources res =  activity.getResources();

                if (title.equals(res.getString(R.string.view))){
                    List<String> listOfChoices = OtherTools.allChoicesToArray(stringOptions, itemsChecked);
                    activityUser.getAboutMe().setView(listOfChoices);
                    String myChoices = OtherTools.ListToString(listOfChoices);
                    OtherTools.markAsSigned(activity, editText, myChoices);
                }else if (title.equals(res.getString(R.string.witness))) { //
                    List<String> listOfChoices = OtherTools.allChoicesToArray(stringOptions, itemsChecked);
                    activityUser.getAboutMe().setWitness(listOfChoices);
                    String myChoices = OtherTools.ListToString(listOfChoices);
                    OtherTools.markAsSigned(activity, editText, myChoices);
                }
            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.cancel), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // ----------------- help function -----------------


}
