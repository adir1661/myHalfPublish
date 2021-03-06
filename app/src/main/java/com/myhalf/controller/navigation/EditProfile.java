package com.myhalf.controller.navigation;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myhalf.R;
import com.myhalf.controller.MyUser;
import com.myhalf.controller.tools.DialogChoice;
import com.myhalf.controller.tools.GoogleApiTools;
import com.myhalf.controller.tools.Listeners;
import com.myhalf.controller.tools.OtherTools;
import com.myhalf.controller.tools.Storage;
import com.myhalf.controller.tools.UpdateAsync;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.DBManagerFactory;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.Enums;
import com.myhalf.model.entities.UserSeeker;
import com.yalantis.ucrop.UCrop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EditProfile extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1113;
    //TODO: make setting on dialogs saved state....
    private final String NAME_OF_BUTTON = "nameofbutton";

    DBManager DB_users = DBManagerFactory.getSeekerManager();
    public UserSeeker activityUser = MyUser.getUserSeeker();

    LinearLayout dummyLayout;
    private SeekBar sbHeight;
    private ImageButton ibMainPicture;
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageButton imageButton4;
    private ImageButton imageButton5;
    private EditText etStatus;
    private EditText etCity;
    private EditText etWitness;
    private EditText etView;
    private EditText etDescription;
    private EditText tvHeight;
    private TextView tvName;
    private Button bGoToSearch;
    private RadioGroup rgChildren;
    private EditText etLivingArea;

    private String myUCropChoiceButton = "";


    private Uri pictureUri;
    boolean uploadedJustNow = false;
    private StorageReference storageReference;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        attachUserToView();

    }

    private void attachUserToView() {
        if (activityUser.getAboutMe().getGender() == Enums.Gender.FEMALE)
            ibMainPicture.setImageResource(R.drawable.student_female);
        storageReference = FirebaseStorage.getInstance().getReference();
        if (activityUser != null)
            tvName.setText(activityUser.getAboutMe().getName() + " " + activityUser.getAboutMe().getBirthday().findAge());
        List<EditText> listEditText = new ArrayList<>(Arrays.asList(etWitness, etStatus, etCity, etDescription, etLivingArea, etView));
        OtherTools.setEditText(getActivity(), etStatus, activityUser.getAboutMe().getStatus());
        OtherTools.setEditText(getActivity(), etWitness, activityUser.getAboutMe().getWitness());
        OtherTools.setEditText(getActivity(), etView, activityUser.getAboutMe().getView());
        OtherTools.setEditText(getActivity(), etCity, activityUser.getAboutMe().getCity());
        OtherTools.setEditText(getActivity(), etDescription, activityUser.getAboutMe().getFreeDescription());
        OtherTools.setEditText(getActivity(), etLivingArea, activityUser.getAboutMe().getLivingArea());
        tvHeight.setText(String.valueOf(activityUser.getAboutMe().getHeight()));

//        activityUser.getAboutMe().setHeight(sbHeight.getProgress());
        Storage.getFromStorage(this, Finals.FireBase.storage.MAIN_PICTURE, ibMainPicture, activityUser);
        Storage.getFromStorage(this, Finals.FireBase.storage.SMALL_PICTURE_1, imageButton1, activityUser);
        Storage.getFromStorage(this, Finals.FireBase.storage.SMALL_PICTURE_2, imageButton2, activityUser);
        Storage.getFromStorage(this, Finals.FireBase.storage.SMALL_PICTURE_3, imageButton3, activityUser);
        Storage.getFromStorage(this, Finals.FireBase.storage.SMALL_PICTURE_4, imageButton4, activityUser);
        Storage.getFromStorage(this, Finals.FireBase.storage.SMALL_PICTURE_5, imageButton5, activityUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!uploadedJustNow) {
//            Storage.getFromStorage(this,Finals.FireBase.storage.MAIN_PICTURE,ibMainPicture,activityUser);
        } else
            uploadedJustNow = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        updateUserDetails();
//        new UpdateAsync().execute(activityUser);
    }

    private void findViews() {
        activityUser = MyUser.getUserSeeker();
        View v = getView();
        if (v != null) {
            dummyLayout = v.findViewById(R.id.dummyLayout);
            ibMainPicture = v.findViewById(R.id.ibMainPicture);
            imageButton1 = v.findViewById(R.id.imageButton1);
            imageButton2 = v.findViewById(R.id.imageButton2);
            imageButton3 = v.findViewById(R.id.imageButton3);
            imageButton4 = v.findViewById(R.id.imageButton4);
            imageButton5 = v.findViewById(R.id.imageButton5);
            etStatus = v.findViewById(R.id.etStatus);
            etCity = v.findViewById(R.id.etCity);
            etWitness = v.findViewById(R.id.etWitness);
            etView = v.findViewById(R.id.etView);
            bGoToSearch = v.findViewById(R.id.bGoToSearch);
            etDescription = v.findViewById(R.id.etDescription);
            tvName = v.findViewById(R.id.tvName);
            rgChildren = v.findViewById(R.id.radioGroupChildren);
            etLivingArea = v.findViewById(R.id.etLivingArea);
            sbHeight = v.findViewById(R.id.sbHeight);
            tvHeight = v.findViewById(R.id.etHieght);
        }
        activity = getActivity();

        //----------------OnClick---------------------
        ibMainPicture.setOnClickListener(this);
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);

        bGoToSearch.setOnClickListener(this);
        etLivingArea.setOnClickListener(this);
        Listeners.attachEditTextToSeekBar(getActivity(), sbHeight, tvHeight, 130);

        etStatus.setOnFocusChangeListener(this);
        etCity.setOnFocusChangeListener(this);
        etWitness.setOnFocusChangeListener(this);
        etView.setOnFocusChangeListener(this);
        etLivingArea.setOnFocusChangeListener(this);

//        etStatus.setOnClickListener(this);
//        etCity.setOnClickListener(this);
//        etWitness.setOnClickListener(this);
//        etView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Resources res = getResources();
        if (v == ibMainPicture) {
            onClickImage(Finals.App.PROFILE_PICTURE);
        } else if (v == imageButton1) {
            onClickImage(Finals.App.SMALL_PICRTURE_1);
        } else if (v == imageButton2) {
            onClickImage(Finals.App.SMALL_PICRTURE_2);
        } else if (v == imageButton3) {
            onClickImage(Finals.App.SMALL_PICRTURE_3);
        } else if (v == imageButton4) {
            onClickImage(Finals.App.SMALL_PICRTURE_4);
        } else if (v == imageButton5) {
            onClickImage(Finals.App.SMALL_PICRTURE_5);
        } else if (v == bGoToSearch) {
            bGoToSearch();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Resources res = getResources();
            if (v == etStatus) {
                if (activityUser.getAboutMe().getGender() == Enums.Gender.MALE || activityUser.getAboutMe().getGender() == Enums.Gender.NULL)
                    DialogChoice.dialogSingleChoice(activity, res.getStringArray(R.array.StatusArrayForMan), res.getString(R.string.status), etStatus, rgChildren);
                else if (activityUser.getAboutMe().getGender() == Enums.Gender.FEMALE)
                    DialogChoice.dialogSingleChoice(activity, res.getStringArray(R.array.StatusArrayForWoman), res.getString(R.string.status), etStatus, rgChildren);
            } else if (v == etWitness) {
                DialogChoice.dialogMultiChoiceLimited(activity, res.getStringArray(R.array.WitnessArray), res.getString(R.string.witness), etWitness, 2);
            } else if (v == etView) {
                DialogChoice.dialogMultiChoiceLimited(activity, res.getStringArray(R.array.ViewArray), res.getString(R.string.view), etView, 2);
            } else if (v == etLivingArea) {
                DialogChoice.dialogSingleChoice(activity, res.getStringArray(R.array.livingAreaArray), res.getString(R.string.livingArea), etLivingArea, null);
            } else if (v == etCity) {
                GoogleApiTools.callGooglePlaces(this, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
            dummyLayout.requestFocus();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onClickImage(String buttonId) {
        myUCropChoiceButton = buttonId;
        dialogChoosePicture(buttonId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void dialogChoosePicture(final String buttonID) {
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        final String[] items = new String[]{getString(R.string.from_camera), getString(R.string.from_mm_card)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, items);//what the?
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//?
        builder.setTitle(R.string.select_image);
        builder.setCancelable(true);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // if choose camera
                    takePicFromCamera(buttonID);
                    dialog.cancel();
                } else if (which == 1) {
                    //if choose gallery
                    takePicFromGallery(buttonID);
                    dialog.cancel();
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void takePicFromCamera(String nameOfButton) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(Finals.App.BUTTON_KEY, nameOfButton);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void takePicFromGallery(String nameOfButton) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Finals.App.BUTTON_KEY, nameOfButton);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_image)), PICK_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {
            // pick from Gallery
            if (requestCode == PICK_FROM_GALLERY && data != null && data.getData() != null) {
                pictureUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), pictureUri);
                    pictureUri = OtherTools.bitmapToUri(getActivity(), bitmap, activityUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cropImageUCrop(data, pictureUri);

            }
            // pick from Camera
            if (requestCode == PICK_FROM_CAMERA && data != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    Uri uri = OtherTools.bitmapToUri(getActivity(), bitmap, activityUser);
//                    UCrop.Options options = new UCrop.Options();
//                    UCrop.of(uri, uri).withAspectRatio(1, 1).withOptions(options).start(getActivity(), this);
                    cropImageUCrop(data, uri);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    ibMainPicture.setImageBitmap(bitmap);
                    pictureUri = resultUri;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadPicture(resultUri, Finals.FireBase.storage.MAIN_PICTURE);
            }
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                handleGooglePlacesResult(resultCode, data);
            }
        }

    }

    private void cropImageUCrop(Intent data, Uri picture) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(fetchColor(R.attr.colorPrimary));
        options.setStatusBarColor(fetchColor(R.attr.colorPrimaryDark));
        options.setImageToCropBoundsAnimDuration(1000);
        options.setActiveWidgetColor(fetchColor(R.attr.colorAccent));
        // add the bundle to get on activity result (of father activity)
        String buttonString = data.getStringExtra(Finals.App.BUTTON_KEY);
//                Bundle bundle = new Bundle();// see T-o-d-o here
//                bundle.putString(Finals.App.BUTTON_KEY,buttonString);
        UCrop uCrop = UCrop.of(picture, picture);
        //____________________^source^^_^^dest^_________________________
        uCrop.withAspectRatio(1, 1).withOptions(options);
        //uCrop.withTag(bundle);//TODO:future update of Ucrop , not yet supported
//                myUCropChoiceButton =buttonString;
        uCrop.start(getActivity());
    }

    private int fetchColor(int resourseAttrId) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, new int[]{resourseAttrId});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;

    }

    private int fetchDarkPrimaryColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimaryDark});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    private int fetchPrimaryColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    private void handleGooglePlacesResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(getActivity(), data);
            String city = (String) place.getName();
            activityUser.getAboutMe().setCity(city);
            Log.i(getTag(), "Place: " + place.getName());
            OtherTools.markAsSigned(getActivity(),etCity,city);
//            DialogChoice.markAsSigned(etCity, city, null);
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(getActivity(), data);
            Log.i(getTag(), status.getStatusMessage());
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

    //should stay on EDIT.PROFILE
    public void updatePictureFromActivity(final Uri resultUri, String button_reqCode) {
        switch (myUCropChoiceButton) {//TODO: future ucrop update: switch (button_reqCode)
            case Finals.App.PROFILE_PICTURE: {
                updatePicture(getActivity(), ibMainPicture, resultUri, Finals.FireBase.storage.MAIN_PICTURE);
                break;
            }
            case Finals.App.SMALL_PICRTURE_1: {
                updatePicture(getActivity(), imageButton1, resultUri, Finals.FireBase.storage.SMALL_PICTURE_1);
                break;
            }
            case Finals.App.SMALL_PICRTURE_2: {
                updatePicture(getActivity(), imageButton2, resultUri, Finals.FireBase.storage.SMALL_PICTURE_2);
                break;
            }
            case Finals.App.SMALL_PICRTURE_3: {
                updatePicture(getActivity(), imageButton3, resultUri, Finals.FireBase.storage.SMALL_PICTURE_3);
                break;
            }
            case Finals.App.SMALL_PICRTURE_4: {
                updatePicture(getActivity(), imageButton4, resultUri, Finals.FireBase.storage.SMALL_PICTURE_4);
                break;
            }
            case Finals.App.SMALL_PICRTURE_5: {
                updatePicture(getActivity(), imageButton5, resultUri, Finals.FireBase.storage.SMALL_PICTURE_5);
                break;
            }
            case "": {
                Log.i("Tag", "exception while setting myUCropChoiceButton <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                break;
            }
        }

    }

    private void updatePicture(Activity activity, final ImageView imageView, final Uri resultUri, String id) {
        imageView.setImageURI(null);
        imageView.setImageURI(resultUri);
        uploadPicture(resultUri, id);
    }

    private void uploadPicture(Uri imageUri, String imageName) {
        uploadedJustNow = true;
        Storage.sendToStorage(getActivity(), imageName, activityUser, imageUri);
    }


    private void bGoToSearch() {
        updateUserDetails();
        Fragment searchFragment = new Search();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.NavigationDrawContainer, searchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void updateUserDetails() {
        String sDescription = etDescription.getText().toString();
        if (!sDescription.isEmpty())
            activityUser.getAboutMe().setFreeDescription(sDescription);
        Integer height = Integer.valueOf(tvHeight.getText().toString());
        if (height != null) {
            activityUser.getAboutMe().setHeight(height);
        }
        int isChildren = rgChildren.getCheckedRadioButtonId();
        if (isChildren == R.id.withChildren)
            activityUser.getAboutMe().setIsChildren(true);
        else
            activityUser.getAboutMe().setIsChildren(false);
        UpdateAsync updateAsync = new UpdateAsync(DB_users, activityUser, getActivity());
        updateAsync.execute(activityUser);
    }


}