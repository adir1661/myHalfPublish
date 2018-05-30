package com.myhalf.controller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myhalf.controller.navigation.EditProfile;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.R;

public class LoginActivity extends AppCompatActivity {
    DBManager userList = DBManagerFactory.getSeekerManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button bLogin= (Button)findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etEmail = (EditText) findViewById(R.id.etEmail);
                final EditText etPassword = (EditText) findViewById(R.id.etPassword);
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (userList.isExist(email)){
                    Intent LoginIntent = new Intent(LoginActivity.this, EditProfile.class);
                    startActivity(LoginIntent);
                }else{
                    Toast.makeText(getApplicationContext(),"User Doesn't Exist!",Toast.LENGTH_LONG).show();
                }
            }
        });
//        final TextView tvToEmailConfig = (TextView) findViewById(R.id.tvToEmailConfirmation);
//        tvToEmailConfig.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //methud to go to Email Confirmation Activity----> Meanwhile :
//                Toast.makeText(getApplicationContext(),"not supported yet",Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}
