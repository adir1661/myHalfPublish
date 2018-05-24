package com.myhalf.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.myhalf.R;

public class MatchMakerRegistering extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_maker_registering);
        final RadioGroup radioGender = (RadioGroup) findViewById(R.id.radioGroupGender);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        final TextView tvToLogin = (TextView) findViewById(R.id.tvToLogin); //for registed user


        //registering Button
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        //for registed user
        tvToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}

