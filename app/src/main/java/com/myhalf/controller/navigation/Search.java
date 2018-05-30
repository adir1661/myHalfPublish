package com.myhalf.controller.navigation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.myhalf.R;
import com.myhalf.controller.MyUser;
import com.myhalf.controller.tools.DialogChoice;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.Enums;
import com.myhalf.model.entities.UserSeeker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Search extends Fragment implements View.OnFocusChangeListener,View.OnClickListener {

    private final String TAG = "search";
    private static int id = 0;
    DBManager DB_users = DBManagerFactory.getSeekerManager();
    UserSeeker activityUser = MyUser.getUserSeeker();

    private Activity activity;

    private LinearLayout dummyLayout;
    private EditText fromAge;
    private EditText toAge;
    private EditText fromHeight;
    private EditText toHeight;
    private EditText etStatus;
    private EditText etWitness;
    private EditText etLivingArea;
    private EditText etView;
    private CheckBox cbWithChildren;
    private Button bSearch;

    private final static int MY_PERMISSION_FINE_LOCATOIN = 101;
    private final static int PLACE_PICKER_REQUEST = 1;

    private boolean mySingleChoiceFlag;
    private String mySingleChoice;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean myChoiceFlag;
    Resources res;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "id= " + ++id);
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        res = getResources();
        findViews();
//        setNumberPickers();
//        requestPermissions();
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                placeAddressText.setText(place.getAddress());
//            }
//
//            @Override
//            public void onError(Status status) {
//
//            }
//        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();
    }

//    private void requestPermissions() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATOIN);
//            }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case MY_PERMISSION_FINE_LOCATOIN:
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    finish();
//                }
//                break;
//        }
//    }

    private void findViews() {
        View v = getView();
        dummyLayout = v.findViewById(R.id.dummyLayout);
        fromAge = v.findViewById(R.id.fromAge);
        toAge = v.findViewById(R.id.toAge);
        fromHeight = v.findViewById(R.id.fromHeight);
        toHeight = v.findViewById(R.id.toHeight);
        etStatus = v.findViewById(R.id.etStatus);
        etWitness = v.findViewById(R.id.etWitness);
        etLivingArea = v.findViewById(R.id.etLivingArea);
        etView = v.findViewById(R.id.etView);
        cbWithChildren = v.findViewById(R.id.cbWithChildren);
        bSearch = v.findViewById(R.id.search);

        activity = getActivity();

        //-----------------OnClick & OnFocus---------------------
        etStatus.setOnFocusChangeListener(this);
        etWitness.setOnFocusChangeListener(this);
        etLivingArea.setOnFocusChangeListener(this);
        etView.setOnFocusChangeListener(this);

        bSearch.setOnClickListener(this);
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Resources res = getResources();
            if (v == etStatus) {
                if (activityUser.getAboutMe().getGender() == Enums.Gender.MALE || activityUser.getAboutMe().getGender() == Enums.Gender.NULL)
                    DialogChoice.dialogMultiChoice(activity, res.getStringArray(R.array.StatusArrayForWoman), res.getString(R.string.status), etStatus);
                else if (activityUser.getAboutMe().getGender() == Enums.Gender.FEMALE)
                    DialogChoice.dialogMultiChoice(activity, res.getStringArray(R.array.StatusArrayForMan), res.getString(R.string.status), etStatus);
            } else if (v == etWitness) {
                DialogChoice.dialogMultiChoiceLimited(activity, res.getStringArray(R.array.Witness), res.getString(R.string.witness), etWitness, 2);
            } else if (v == etView) {
                DialogChoice.dialogMultiChoiceLimited(activity, res.getStringArray(R.array.ViewArray), res.getString(R.string.view), etView, 2);
            } else if (v == etLivingArea) {
                DialogChoice.dialogMultiChoice(activity, res.getStringArray(R.array.livingAreaArray), res.getString(R.string.livingArea), etLivingArea);
            }
            dummyLayout.requestFocus();
        }
    }


    @Override
    public void onClick(View v) {
//        if (v == etStatus) {
//            dialogSingleChoice(res.getStringArray(R.array.StatusArrayForMan), "status");
//        } else if (v == etWitness) {
//            dialogSingleChoice(res.getStringArray(R.array.WitnessArray), "My Eda");
//        } else if (v == etLivingArea) {
//            placePicker();
//        } else if (v == etView) {
//            dialogMultiChoice(res.getStringArray(R.array.ViewArray), "My view");
        if (v == bSearch) {
            bSearchOnClick();
        }
    }

    private void placePicker() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = builder.build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                etLivingArea.setText(place.getAddress());
            }
        }
    }

    //----------------------- Dialog of single choice -----------------------
    private void dialogSingleChoice(final String[] stringsList, final String title) {//TODO: move all to multichoice....(take care also to transfer to shared preferenced in bulks...)
        final String[] Strings = stringsList;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final String[] choice = {""};

        builder.setSingleChoiceItems(Strings, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                choice[0] = stringsList[whichButton];
                //enter the choice to the field in the class
            }
        });
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ------------ Enter values to Share preference ----------------
                switch (title) {
                    case "status": {
                        etStatus.setText(choice[0]);
                        editor.putString(Finals.DB.AboutMe.STATUS, choice[0]);
                        editor.commit();

                        break;
                    }
                    case "My Eda": {
                        editor.putString(Finals.DB.AboutMe.EDA, choice[0]);
                        editor.commit();

                        break;
                    }
                }
            }
        });

        builder.setNegativeButton("ביטול", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //--------------------Dialog of multi choice---------------------
    private void dialogMultiChoice(final String[] stringOptions, final String title) {

        final boolean[] boolOptions = new boolean[stringOptions.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final boolean[] flag = {false};
        builder.setMultiChoiceItems(stringOptions, boolOptions, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                boolOptions[which] = isChecked;
                String currentItem = stringOptions[which];
            }
        });
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (myChoiceFlag) {
                    switch (title) {
                        case "My view": {
                            Set<String> mySet = new HashSet<String>(Arrays.asList(allChoicesToArray(stringOptions, boolOptions)));
                            editor.putStringSet(Finals.DB.AboutMe.VIEW, mySet);
                            break;
                        }
                    }
                    myChoiceFlag = false;
                }
            }
        });
        builder.setNegativeButton("ביטול", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String[] enterToFields(String[] str, boolean[] bool) {
        String[] field = new String[]{};
        int j = 0;
        for (int i = 0; i < str.length; i++)
            if (bool[i]) {
                field[j] = str[i];
                j++;
            }
        return field;
    }

    // ----------------- help function -----------------
    public String[] allChoicesToArray(String[] str, boolean[] bool) {
        String[] array = new String[]{};
        int j = 0;
        for (int i = 0; i < str.length; i++)
            if (bool[i]) {
                array[j] = str[i];
                j++;
            }
        return array;
    }


    private void bSearchOnClick() {
//        if (fromAge.getText()!= null)
//            editor.putInt(Finals.DB.FROM_AGE, Integer.valueOf(fromAge.getText().toString()));
//        if (toAge.getText()!= null)
//            editor.putInt(Finals.DB.TO_AGE, Integer.valueOf(toAge.getText().toString()));
//        if (fromHeight.getText()!= null)
//            editor.putInt(Finals.DB.FROM_HEIGHT, Integer.valueOf(fromHeight.getText().toString()));
//        if (toHeight.getText()!= null)
//            editor.putInt(Finals.DB.TO_HEIGHT, Integer.valueOf(toHeight.getText().toString()));
//        editor.commit();
//-----------------move to fragment 'SearchResults' and stay on the navigation draw-------------------------
        // Create new fragment and transaction
        Fragment searchResultFragment = new SearchResults();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.NavigationDrawContainer, searchResultFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }//what happened




}
