package com.myhalf.controller.navigation;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.myhalf.R;
import com.myhalf.controller.SingleChatActivity;
import com.myhalf.controller.myUser;
import com.myhalf.controller.tools.OtherTools;
import com.myhalf.controller.tools.Storage;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.UserSeeker;

public class FullProfile extends Fragment implements View.OnClickListener {

    DBManager DB_users = DBManagerFactory.getSeekerManager();
    UserSeeker activityUser = myUser.getUserSeeker();
    UserSeeker userProfile;

    private ImageView vbMainPicture;
    private TextView tvNameAndAge;
    private TextView tvStatus;
    private TextView tvView;
    private TextView tvCity;
    private ImageView ImageView1;
    private ImageView ImageView2;
    private ImageView ImageView3;
    private ImageView ImageView4;
    private ImageView ImageView5;
    private TextView tvFreeDescription;
    private TextView tvFreeDescriptionTitle;


    private TextView tvHeight;
    private TextView tvEda;
    private TextView tvLanguage;
    private Button bToChat;
    private Button bChangeDetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Storage.getFromStorage(this,Finals.FireBase.storage.MAIN_PICTURE,vbMainPicture,userProfile);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        userProfile = (UserSeeker) bundle.getSerializable(Finals.App.USER_SEEKER);

        findViews();
        setItemsText();
        if (userProfile == activityUser) {
            bChangeDetails.setVisibility(View.VISIBLE);
            bToChat.setVisibility(View.GONE);

        }
        Storage.getFromStorage(this,Finals.FireBase.storage.MAIN_PICTURE,vbMainPicture,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_1,ImageView1,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_2,ImageView2,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_3,ImageView3,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_4,ImageView4,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_5,ImageView5,activityUser);
    }

    private void changeDetailsOnClick() {
        // Create new fragment and transaction
        Fragment editProfileFragment = new EditProfile();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.NavigationDrawContainer, editProfileFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }


    private void findViews() {
        View v = getView();
        vbMainPicture = (ImageView) v.findViewById(R.id.ivMainPicture);
        tvNameAndAge = (TextView) v.findViewById(R.id.tvName);
        tvStatus = (TextView) v.findViewById(R.id.tvStatus);
        tvView = (TextView) v.findViewById(R.id.tvView);
        tvCity = (TextView) v.findViewById(R.id.tvCity);
        ImageView1 = (ImageView) v.findViewById(R.id.ImageView1);
        ImageView2 = (ImageView) v.findViewById(R.id.ImageView2);
        ImageView3 = (ImageView) v.findViewById(R.id.ImageView3);
        ImageView4 = (ImageView) v.findViewById(R.id.ImageView4);
        ImageView5 = (ImageView) v.findViewById(R.id.ImageView5);
        tvFreeDescription = (TextView) v.findViewById(R.id.tvFreeDescription);
        tvHeight = (TextView) v.findViewById(R.id.tvHeight);
        tvEda = (TextView) v.findViewById(R.id.tvEda);
        tvLanguage = (TextView) v.findViewById(R.id.tvLanguage);
        bToChat = (Button) v.findViewById(R.id.bToChat);
        bChangeDetails = (Button) v.findViewById(R.id.bChangeDetails);
        tvFreeDescriptionTitle = (TextView) v.findViewById(R.id.tvFreeDescriptionTitle);

        bToChat.setOnClickListener(this);
        bChangeDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bToChat) {
            // Handle clicks for bToChat
            Intent moveToChatActivity = new Intent(getActivity().getApplicationContext(), SingleChatActivity.class);
            moveToChatActivity.putExtra(Finals.App.USER_SEEKER, userProfile);
            startActivity(moveToChatActivity);
        } else if (v == bChangeDetails) {
            changeDetailsOnClick();


        }
    }

    private void setItemsText() {
        tvNameAndAge.setText(fixLinesAndSize(userProfile.getAboutMe().getName()) + " " + userProfile.getAboutMe().getBirthday().findAge());
        if (userProfile.getAboutMe().getStatus() != null)
            tvStatus.setText(fixLinesAndSize(userProfile.getAboutMe().getStatus().toString()));
        else {
            tvStatus.setVisibility(View.GONE);
        }
        if (userProfile.getAboutMe().getCity() != null && userProfile.getAboutMe().getCity() != "")
            tvCity.setText(fixLinesAndSize(userProfile.getAboutMe().getCity()));
        else
            tvCity.setVisibility(View.GONE);
        if (userProfile.getAboutMe().getView() != null && userProfile.getAboutMe().getView().size() > 0) {
            tvView.setText(fixLinesAndSize(userProfile.getAboutMe().getView().get(0)));
        } else {
            tvView.setVisibility(View.GONE);
        }
        if (userProfile.getAboutMe().getFreeDescription() != null && userProfile.getAboutMe().getFreeDescription() != "")
            tvFreeDescription.setText(fixLinesAndSize(userProfile.getAboutMe().getFreeDescription()));
        else {
            tvFreeDescription.setVisibility(View.GONE);
            tvFreeDescriptionTitle.setVisibility(View.GONE);
        }
        if (userProfile.getAboutMe().getWitness() != null)
            tvEda.setText(tvEda.getText() + "  " + fixLinesAndSize(userProfile.getAboutMe().getWitness().toString()));
        else {
            tvEda.setVisibility(View.GONE);
        }
        tvHeight.setText(tvHeight.getText() + "  " + userProfile.getAboutMe().getHeight() + "cm");
        tvLanguage.setVisibility(View.GONE);
    }

    private String fixLinesAndSize(@NonNull String string) {
        return AdapterRecycleView.fixLinesAndSize(string);
    }


}
