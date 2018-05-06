package com.myhalf.controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.myhalf.R;
import com.myhalf.controller.navigation.NavigationDraw;
import com.myhalf.controller.tools.Formatter;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.datasource.Tools;
import com.myhalf.model.entities.DateBuilt;
import com.myhalf.model.entities.Enums;
import com.myhalf.model.entities.UserSeeker;

import java.util.Calendar;
import java.util.Date;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG= RegisterActivity.class.getSimpleName();
    DBManager DB_users = DBManagerFactory.getSeekerManager();
    UserSeeker activityUser = myUser.getUserSeeker();

    private EditText etUserName;
    private EditText etEmail;
    private EditText etPassword;
    private RadioGroup radioGroupGender;
    private RadioButton radioWoman;
    private RadioButton radioMan;
    private Button bBirthday;
    private Button bRegister;
    private TextView tvToLogin;
    private String birthday = "";
    private boolean flag_dateClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);
        findViews();
    }


    private void findViews() {
        etUserName = (EditText)findViewById( R.id.etUserName );
        etEmail = (EditText)findViewById( R.id.etEmail );
        etPassword = (EditText)findViewById( R.id.etPassword );
        radioWoman = (RadioButton)findViewById( R.id.radio_woman );
        radioMan = (RadioButton)findViewById( R.id.radio_man );
        bBirthday = (Button) findViewById(R.id.bBirthday);
        bRegister = (Button)findViewById( R.id.bRegister );
        tvToLogin = (TextView)findViewById( R.id.tvToLogin );
        radioGroupGender= (RadioGroup) findViewById(R.id.radioGroupGender);

        radioWoman.setOnClickListener(this);
        radioMan.setOnClickListener(this);
        bRegister.setOnClickListener( this );
        bBirthday.setOnClickListener( this );
        tvToLogin.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
         if ( v == bRegister ) {
            bRegisterOnClick();
         }
         else if (v == bBirthday){
             birthDayOnClick();
         }else if (v == tvToLogin) {
            //for registered user
             toLoginOnClick();
         }else if (v == radioMan){

         }else if (v == radioWoman) {
             String herName = ((EditText)findViewById(R.id.etUserName)).getText().toString();

         }
    }

    private void toLoginOnClick() {
        Intent toLoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        RegisterActivity.this.startActivity(toLoginIntent);
    }

    //----------------- AsyncTask -------------------------
    private class RegisterAsync extends AsyncTask<UserSeeker, ProgressBar, Void> {
        @Override
        protected Void doInBackground(UserSeeker... userSeeker) {

        // ----------------- Send to data base -----------------------
            String id =DB_users.addUser(Tools.userSeekerToContentValues(userSeeker[0]));
            userSeeker[0].setId(id);
            //-------------- Shared Preferences  - save in the app ---------------
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor =  sharedPreferences.edit();
            editor.putString(Finals.DB.User.ID , userSeeker[0].getId());
            editor.putString(Finals.DB.User.NAME, userSeeker[0].getAboutMe().getName());
            editor.commit();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),
                    R.string.registrationDone, Toast.LENGTH_LONG).show();
        }
    }

    private void birthDayOnClick() {
        flag_dateClicked = true;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                date = DateBuilt.Tools.clarifyDateString(date);// i made a func to make sure tht all the date is orginized as "dd/nn/yyyy"
                if (date == null) {
                    Toast.makeText(getApplicationContext(),
                        "exeption occured in Datebuilt Class", Toast.LENGTH_LONG).show();
                }
                bBirthday.setText(date);
                birthday = date;

            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                            RegisterActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            onDateSetListener,
                            year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        dialog.getDatePicker().setMaxDate(date.getTime());
        dialog.show();
    }

    private void bRegisterOnClick() {
//        if (
////                etUserName.getText().toString().isEmpty()
////                        || etPassword.getText().toString().isEmpty()
////                        || etEmail.getText().toString().isEmpty()
////                        || !(radioMan.isChecked() || radioWoman.isChecked())
////                        || !flag_dateClicked
//            )
//        {   Toast.makeText(getApplicationContext(),
//                    R.string.fillVariable, Toast.LENGTH_LONG).show();
//
//        }else if (!Formatter.isEmailValid(etEmail.getText().toString())) {
//            Toast.makeText(getApplicationContext(),
//                    "כתובת המייל אינה תקינה", Toast.LENGTH_LONG).show();
//        }else {
            String username = etUserName.getText().toString();
            String password = etPassword.getText().toString();
            String email = etEmail.getText().toString();

            int selectedId = radioGroupGender.getCheckedRadioButtonId();
            Enums.Gender gender;
            if (selectedId == R.id.radio_man)
                gender = Enums.Gender.MALE;
            else
                gender = Enums.Gender.FEMALE;
            myUser.dismissUserSeeker();
            activityUser = myUser.getUserSeeker();
            activityUser.getAboutMe().setName(username);
            activityUser.setEmailAdress(email);
            activityUser.setPassword(password);
            activityUser.setFirebaseToken(FirebaseInstanceId.getInstance().getToken());
            activityUser.getAboutMe().setBirthday(new DateBuilt(birthday));
            activityUser.getAboutMe().setGender(gender);
            activityUser.setId("0");

            //----------------Async---------------
            RegisterAsync async = new RegisterAsync();
            async.execute(activityUser);
            Intent intent = new Intent(RegisterActivity.this, NavigationDraw.class);
            startActivity(intent);
    }
    }


