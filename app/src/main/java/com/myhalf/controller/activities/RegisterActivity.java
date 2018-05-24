package com.myhalf.controller.activities;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.myhalf.R;
import com.myhalf.controller.asynctasks.RegisterAsync;
import com.myhalf.controller.navigation.NavigationDraw;
import com.myhalf.controller.navigation.TakanonActivity;
import com.myhalf.controller.tools.Formatter;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.DateBuilt;
import com.myhalf.model.entities.Enums;
import com.myhalf.model.entities.UserSeeker;
import com.nipunbirla.boxloader.BoxLoaderView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = RegisterActivity.class.getSimpleName();
    DBManager DB_users = DBManagerFactory.getSeekerManager();
    UserSeeker activityUser = MyUser.getUserSeeker();
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mFBAuth;
    private FirebaseUser mFBUSer;



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
    private CheckBox cbTakanon;

    private TextView tvTakanon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);
//        callFireBaseAuthUi();
        findViews();
        mFBAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null&&bundle.containsKey(Finals.App.SIGN_OUT)&& bundle.getBoolean(Finals.App.SIGN_OUT)){
            FragmentManager fm = getFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mFBUSer = mFBAuth.getCurrentUser();
        updateUI(mFBUSer,false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }

    private void callFireBaseAuthUi() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    private void findViews() {
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        radioWoman = findViewById(R.id.radio_woman);
        radioMan = findViewById(R.id.radio_man);
        bBirthday = findViewById(R.id.bBirthday);
        bRegister = findViewById(R.id.bRegister);
        tvToLogin = findViewById(R.id.tvToLogin);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        cbTakanon = findViewById(R.id.cbTakanon);
        tvTakanon = findViewById(R.id.tvTakanon);

        radioWoman.setOnClickListener(this);
        radioMan.setOnClickListener(this);
        bRegister.setOnClickListener(this);
        bBirthday.setOnClickListener(this);
        tvToLogin.setOnClickListener(this);
        tvTakanon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bRegister) {
            bRegisterOnClick();
        } else if (v == bBirthday) {
            birthDayOnClick();
        } else if (v == tvToLogin) {
            //for registered user
            toLoginOnClick();
        } else if (v == radioMan) {

        } else if (v == radioWoman) {
            String herName = ((EditText) findViewById(R.id.etUserName)).getText().toString();
        } else if (v == tvTakanon) {
            takanonOnClick();
        }
    }

    private void takanonOnClick() {
        Intent takanon = new Intent(RegisterActivity.this, TakanonActivity.class);
        RegisterActivity.this.startActivity(takanon);
    }

    private void toLoginOnClick() {
        Intent toLoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        RegisterActivity.this.startActivity(toLoginIntent);
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
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
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
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        dialog.getDatePicker().setMaxDate(date.getTime());
        dialog.show();
    }

    private void bRegisterOnClick() {
        if (
                etUserName.getText().toString().isEmpty()
                        || etPassword.getText().toString().isEmpty()
                        || etEmail.getText().toString().isEmpty()
                        || !(radioMan.isChecked() || radioWoman.isChecked())
                        || !flag_dateClicked
                ) {
            Toast.makeText(getApplicationContext(),
                    R.string.fillVariable, Toast.LENGTH_LONG).show();

        } else if (!Formatter.isEmailValid(etEmail.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "כתובת המייל אינה תקינה", Toast.LENGTH_LONG).show();
        } else if (!cbTakanon.isChecked()) {
            Toast.makeText(getApplicationContext(),
                    "Please accept the rules", Toast.LENGTH_LONG).show();
        } else {
            String username = etUserName.getText().toString();
            String password = etPassword.getText().toString();
            String email = etEmail.getText().toString();

            int selectedId = radioGroupGender.getCheckedRadioButtonId();
            Enums.Gender gender;
            if (selectedId == R.id.radio_man)
                gender = Enums.Gender.MALE;
            else
                gender = Enums.Gender.FEMALE;
            MyUser.dismissUserSeeker();
            activityUser = MyUser.getUserSeeker();
            activityUser.getAboutMe().setName(username);
            activityUser.setEmailAdress(email);
            activityUser.setPassword(password);
            activityUser.setFirebaseToken(FirebaseInstanceId.getInstance().getToken());
            activityUser.getAboutMe().setBirthday(new DateBuilt(birthday));
            activityUser.getAboutMe().setGender(gender);
            activityUser.setId("0");

            mFBAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mFBAuth.getCurrentUser();
                                        updateUI(user, true);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null, true);
                                    }

                                    // ...
                                }
                            }
                    );
            BoxLoaderView boxLoader = findViewById(R.id.progress);

        }
    }

    private void updateUI(@Nullable FirebaseUser user, boolean firstSigned) {
        if (user!=null){//user already signed in
            if (firstSigned) {
                registerUser();
            }
            goToNavDraw();
        }else {
            if (firstSigned){
                //inform user that he cannot use specific email
                Toast.makeText(this,"User email already used!",Toast.LENGTH_SHORT).show();
            }else {
                // he is first logged in. or signed out, and then in.
            }

        }

    }

    private void registerUser() {
        RegisterAsync async = new RegisterAsync(this,DB_users);
        async.execute(activityUser);
    }

    private void goToNavDraw() {
        Intent intent = new Intent(RegisterActivity.this, NavigationDraw.class);
        intent.putExtra(Finals.App.FROM_REGISTER_ACTIVITY,true);
        startActivity(intent);
    }
}


