package com.myhalf.controller.navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myhalf.R;
import com.myhalf.controller.activities.RegisterActivity;
import com.myhalf.controller.asynctasks.getUserAsync;
import com.myhalf.controller.activities.MyUser;
import com.myhalf.controller.tools.MyFragmentManager;
import com.myhalf.controller.tools.OtherTools;
import com.myhalf.controller.tools.UpdateAsync;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.UserSeeker;
import com.yalantis.ucrop.UCrop;

public class NavigationDraw extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public UserSeeker activityUser = MyUser.getUserSeeker();
    private DBManager DB_manager = DBManagerFactory.getSeekerManager();
    private final String TAG = "NavigationDraw";

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    //TODO: execute all actions on their preffered time ... , for example make an animation until we got the user then then execute the fragment EditProfile......
    @Override
    protected void onResume() {
        super.onResume();
//        startService(new Intent(getBaseContext(), CheckNewsService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_draw);
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(Finals.App.FROM_REGISTER_ACTIVITY)) {
            signIn();
        } else
            logInDefaultUser();


//        signInFirebaseAnonymously();


        Toolbar toolbar = findViewById(R.id.tool_bar);
//        toolbar.setTitle("My Half");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//    // Register which actions the Broadcast can receive
//        MyBroadcastReceiver br = new MyBroadcastReceiver();
//        IntentFilter actionsFilter = new IntentFilter();
//        actionsFilter.addAction("android.intent.action.TIME_SET");
//        actionsFilter.addAction("android.intent.action.BOOT_COMPLETED");
//        actionsFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
////        actionsFilter.addAction("com.javacodegeeks.android.A_CUSTOM_INTENT");
//        actionsFilter.addAction("android.intent.action.BATTERY_LOW");
//        actionsFilter.addAction("android.intent.action.HEADSET_PLUG");
////        startService(new Intent(this,CheckNewsService.class));
//        this.registerReceiver(br, actionsFilter);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                activityUser = MyUser.getUserSeeker();
            }
        });
    }

    private void logInDefaultUser() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(Finals.FireBase.Authentication.DEFAULT_EMAIL, Finals.FireBase.Authentication.DEFAULT_PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            mUser = mAuth.getCurrentUser();
                            if (mUser.isAnonymous())
                                Toast.makeText(NavigationDraw.this, "testing account online", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(NavigationDraw.this, "testing account adir is online", Toast.LENGTH_LONG).show();

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(NavigationDraw.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                        signIn();
                    }
                });
    }

    private void signIn() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        activityUser = MyUser.getUserSeeker();

        if (mUser != null) {
            getUserAsync getUserAsync = new getUserAsync(this, DB_manager, new getUserAsync.Implementation() {
                @Override
                public void onPreExecute() {
                }

                @Override
                public void onPostExecute(UserSeeker userSeeker) {
                    MyUser.setUserSeeker(userSeeker);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    EditProfile fragment = new EditProfile();
                    fragmentTransaction.add(R.id.NavigationDrawContainer, fragment);
                    fragmentTransaction.commit();
                    //TODO end progressanimation
                }
            });
            getUserAsync.execute(mUser.getEmail());
        } else
            signOut();

    }

    private void signInFirebaseAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                resultUri = OtherTools.bitmapToUri(this, bitmap, activityUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            EditProfile fragment = (EditProfile) getFragmentManager().findFragmentById(R.id.NavigationDrawContainer);
//            String buttonId = data.getStringExtra(Finals.App.BUTTON_KEY);
            fragment.updatePictureFromActivity(resultUri, null/*TODO:here for future update of Ucrop*/);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int count = getFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {//TODO: becareful with this code!
            int index = getFragmentManager().getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();
            Fragment fragment = getFragmentManager().findFragmentByTag(tag);
            if (fragment instanceof EditProfile) {
                EditProfile fragmentEditProfile = (EditProfile) fragment;
                UpdateAsync updateAsync = new UpdateAsync(DB_manager,activityUser,this);
                updateAsync.execute(fragmentEditProfile.activityUser);
            }
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ;
        } else if (id == R.id.menu_sign_out) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Intent signOut = new Intent(getApplicationContext(), RegisterActivity.class);
        mAuth.signOut();
        MyUser.dismissUserSeeker();
        signOut.putExtra(Finals.App.SIGN_OUT, true);
        startActivity(signOut);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.itMyProfile) {
            //Move to frgment full profile getting this user profile....
            FragmentTransaction ftMoveToFullProfile = getFragmentManager().beginTransaction();
            ftMoveToFullProfile.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            FullProfile fragmentProfile = new FullProfile();
            Bundle bundle = new Bundle();
            activityUser = MyUser.getUserSeeker();
            bundle.putSerializable(Finals.App.USER_SEEKER, activityUser);
            fragmentProfile.setArguments(bundle);
            ftMoveToFullProfile.replace(R.id.NavigationDrawContainer, fragmentProfile);
            ftMoveToFullProfile.addToBackStack(null);
            ftMoveToFullProfile.commit();

            // Handle the camera action
        } else if (id == R.id.itSearchSettings) {
            MyFragmentManager.replaceFragmentInContainer(this, new Search(), R.id.NavigationDrawContainer);

        } else if (id == R.id.itSearchResults) {
            MyFragmentManager.replaceFragmentInContainer(this, new SearchResults(), R.id.NavigationDrawContainer);
//        }
//            else if (id == R.id.itConversations) {
//            // Create new fragment and transaction
//            ChatListFragment chatListFragment = new ChatListFragment();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            // Replace whatever is in the fragment_container view with this fragment,
//            // and add the transaction to the back stack if needed
//            transaction.replace(R.id.NavigationDrawContainer, chatListFragment);
//            transaction.addToBackStack(null);
//            // Commit the transaction
//            transaction.commit();
//
        } else if (id == R.id.itSetting) {
            MyFragmentManager.replaceFragmentInContainer(this,new Settings(),R.id.NavigationDrawContainer);
        } else if (id == R.id.itTakanon) {
            MyFragmentManager.replaceFragmentInContainer(this,new Takanon(),R.id.NavigationDrawContainer);



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
