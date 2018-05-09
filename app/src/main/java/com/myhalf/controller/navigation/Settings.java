package com.myhalf.controller.navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.myhalf.R;
import com.myhalf.controller.DeleteAccount;
import com.myhalf.controller.tools.MyFragmentManager;

public class Settings extends Fragment implements View.OnClickListener{
    public Settings() {
        // Required empty public constructor
    }


    private CheckBox cbMessageRecieved;
    private CheckBox cbWatched;
    private CheckBox cbCommentRecieved;
    private RadioGroup rgCanWatchMe;
    private RadioButton rbAllUsers;
    private RadioButton rbByChoice;
    private EditText bStatus;
    private LinearLayout Age;
    private EditText fromAge;
    private EditText toAge;
    private EditText bView;
    private RadioGroup rgLanguage;
    private RadioButton rbHebrew;
    private RadioButton rbEnglish;
    private Button bGoToSearch;
    private Button bDeleteAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();

        //implement all stuff here
    }

    private void findViews() {
        View v = getView();
        cbMessageRecieved = v.findViewById( R.id.cbMessageRecieved );
        cbWatched = v.findViewById( R.id.cbWatched );
        cbCommentRecieved = v.findViewById( R.id.cbCommentRecieved );
        rgCanWatchMe = v.findViewById( R.id.rgCanWatchMe );
        rbAllUsers = v.findViewById( R.id.rbAllUsers );
        rbByChoice = v.findViewById( R.id.rbByChoice );
        bStatus = v.findViewById( R.id.bStatus );
        Age = v.findViewById( R.id.Age );
        fromAge = v.findViewById( R.id.fromAge );
        toAge = v.findViewById( R.id.toAge );
        bView = v.findViewById( R.id.bView );
        rgLanguage = v.findViewById( R.id.rgLanguage );
        rbHebrew = v.findViewById( R.id.rbHebrew );
        rbEnglish = v.findViewById( R.id.rbEnglish );
        bGoToSearch = v.findViewById( R.id.bGoToSearch );
        bDeleteAccount = v.findViewById( R.id.bDeleteAccount );

        rbAllUsers.setOnClickListener( this );
        rbByChoice.setOnClickListener( this );
        rbHebrew.setOnClickListener( this );
        rbEnglish.setOnClickListener( this );
        bGoToSearch.setOnClickListener( this );
        bDeleteAccount.setOnClickListener( this );
    }


    @Override
    public void onClick(View v) {
        if (v == rbAllUsers) {
            // Handle clicks for rbAllUsers
        } else if (v == rbByChoice) {
            // Handle clicks for rbByChoice
        } else if (v == rbHebrew) {
            // Handle clicks for rbHebrew
        } else if (v == rbEnglish) {
            // Handle clicks for rbEnglish
        } else if (v == bGoToSearch) {
            // Handle clicks for bGoToSearch
        } else if (v == bDeleteAccount) {
            Intent moveToDelete = new Intent(getActivity(), DeleteAccount.class);
            startActivity(moveToDelete);
        }
    }






}
