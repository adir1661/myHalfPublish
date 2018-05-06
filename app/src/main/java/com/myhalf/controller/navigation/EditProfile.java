package com.myhalf.controller.navigation;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myhalf.R;
import com.myhalf.controller.myUser;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EditProfile extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1113;
    //TODO: make setting on dialogs saved state....
    private final String NAME_OF_BUTTON = "nameofbutton";

    DBManager DB_users = DBManagerFactory.getSeekerManager();
    public UserSeeker activityUser = myUser.getUserSeeker();

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

    private String myUCropChoiceButton = "";
    private String myChoice;
    private boolean myChoiceFlag = false;

    private Uri pictureUri;
    boolean uploadedJustNow = false;

    private StorageReference storageReference;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        if (activityUser.getAboutMe().getGender()== Enums.Gender.FEMALE)
            ibMainPicture.setImageResource(R.drawable.student_female);

        storageReference = FirebaseStorage.getInstance().getReference();
        if (activityUser != null)
            tvName.setText(activityUser.getAboutMe().getName());
        activityUser.getAboutMe().setHeight(sbHeight.getProgress());
        Storage.getFromStorage(this,Finals.FireBase.storage.MAIN_PICTURE,ibMainPicture,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_1,imageButton1,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_2,imageButton2,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_3,imageButton3,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_4,imageButton4,activityUser);
        Storage.getFromStorage(this,Finals.FireBase.storage.SMALL_PICTURE_5,imageButton5,activityUser);



    }

    @Override
    public void onResume() {
        super.onResume();
        if (!uploadedJustNow) {
//            Storage.getFromStorage(this,Finals.FireBase.storage.MAIN_PICTURE,ibMainPicture,activityUser);
        }else
            uploadedJustNow = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        updateUserDetails();
//        new UpdateAsync().execute(activityUser);
    }



    private void findViews() {
        View v = getView();
        dummyLayout = v.findViewById(R.id.dummyLayout);
        ibMainPicture = v.findViewById(R.id.ibMainPicture);
        imageButton1 = v.findViewById(R.id.imageButton1);
        imageButton2 = v.findViewById(R.id.imageButton2);
        imageButton3 = v.findViewById(R.id.imageButton3);
        imageButton4 = v.findViewById(R.id.imageButton4);
        imageButton5 = v.findViewById(R.id.imageButton5);
        etStatus = v.findViewById(R.id.bStatus);
        etCity = v.findViewById(R.id.bCity);
        etWitness = v.findViewById(R.id.bWitness);
        etView = v.findViewById(R.id.bView);
        bGoToSearch = v.findViewById(R.id.bGoToSearch);
        etView = v.findViewById(R.id.bView);
        etDescription = v.findViewById(R.id.etDescription);
        tvName = v.findViewById(R.id.tvName);
//        rgChildren =  v.findViewById(R.id.radioGroupChildren);

        sbHeight = v.findViewById(R.id.sbHeight);
        tvHeight = v.findViewById(R.id.etHieght);

        //----------------OnClick---------------------
        ibMainPicture.setOnClickListener(this);
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);
        etStatus.setOnFocusChangeListener(this);
        etCity.setOnFocusChangeListener(this);
        etWitness.setOnFocusChangeListener(this);
        etView.setOnFocusChangeListener(this);
        bGoToSearch.setOnClickListener(this);

        Listeners.attachEditTextToSeekBar(getActivity(),sbHeight, tvHeight,130);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
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
        }  else if (v == bGoToSearch) {
            bGoToSearch();
        }
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            Resources res = getResources();
            if (v == etStatus) {
                dialogSingleChoice(res.getStringArray(R.array.Status), "My status");
            } else if (v == etWitness) {
                dialogMultiChoice(res.getStringArray(R.array.Witness), "My Eda");
            } else if (v == etView) {
                dialogMultiChoice(res.getStringArray(R.array.View), "My view");
            } else if (v == etCity) {
                callGooglePlaces();
            }
            dummyLayout.requestFocus();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onClickImage(String buttonId) {
        myUCropChoiceButton = buttonId;
        dialogChoosePicture(buttonId);
    }

    private void callGooglePlaces() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .setCountry("IL")
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
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
                    != PackageManager.PERMISSION_GRANTED){
                return;
            }
        }

        final String[] items = new String[]{"מהמצלמה", "מכרטיס זיכרון"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, items);//what the?
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//?
        builder.setTitle("Select Image");
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
        intent.putExtra(Finals.App.BUTTON_KEY,nameOfButton);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void takePicFromGallery(String nameOfButton) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Finals.App.BUTTON_KEY, nameOfButton);
        startActivityForResult(Intent.createChooser(intent, "בחר תמונה"), PICK_FROM_GALLERY);
    }

    //
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
                    pictureUri = bitmapToUri(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cropImageUCrop(data,pictureUri);

            }
            // pick from Camera
            if (requestCode == PICK_FROM_CAMERA && data != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
                try {
                    Uri uri = bitmapToUri(bitmap);
//                    UCrop.Options options = new UCrop.Options();
//                    UCrop.of(uri, uri).withAspectRatio(1, 1).withOptions(options).start(getActivity(), this);
                    cropImageUCrop(data,uri);
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
                uploadPicture(resultUri,Finals.FireBase.storage.MAIN_PICTURE);
            }
//            Uri bitmapUri = getImageUri(getActivity().getApplicationContext(),bitmap);
//            UCrop.of(bitmapUri, bitmapUri)
//                    .withAspectRatio(16, 9)
//                    .withMaxResultSize(200, 200)
//                    .start(getActivity());

//            String nameOfButton = data.getCharSequenceExtra(NAME_OF_BUTTON);
//
//            switch (nameOfButton) {
//                case "ibMainPicture": {
//                    ibMainPicture.setImageBitmap(bitmap);
//                    break;
//                }
//                case "imageButton1": {
//                    imageButton1.setImageBitmap(bitmap);
//                    break;
//                }
//                case "imageButton2": {
//                    imageButton2.setImageBitmap(bitmap);
//                    break;
//                }
//                case "imageButton3": {
//                    imageButton3.setImageBitmap(bitmap);
//                    break;
//                }
//                case "imageButton4": {
//                    imageButton4.setImageBitmap(bitmap);
//                    break;
//                }
//                case "imageButton5": {
//                    imageButton5.setImageBitmap(bitmap);
//                    break;
//                }
//                default:
//                    break;
//            }
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                handleGooglePlacesResult(resultCode, data);
            }
        }

    }

    private void cropImageUCrop(Intent data, Uri picture) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(fetchColor( R.attr.colorPrimary ));
        options.setStatusBarColor(fetchColor( R.attr.colorPrimaryDark ));
        options.setImageToCropBoundsAnimDuration(1000);
        options.setActiveWidgetColor(fetchColor( R.attr.colorAccent ));
        // add the bundle to get on activity result (of father activity)
        String buttonString =  data.getStringExtra(Finals.App.BUTTON_KEY);
//                Bundle bundle = new Bundle();// see T-o-d-o here
//                bundle.putString(Finals.App.BUTTON_KEY,buttonString);
        UCrop  uCrop =UCrop.of(picture, picture);
        //____________________^source^^_^^dest^_________________________
        uCrop.withAspectRatio(1, 1).withOptions(options);
        //uCrop.withTag(bundle);//TODO:future update of Ucrop , not yet supported
//                myUCropChoiceButton =buttonString;
        uCrop.start(getActivity());
    }

    private int fetchColor(int resourseAttrId) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, new int[] { resourseAttrId });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;

    }

    private int fetchDarkPrimaryColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimaryDark });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    private int fetchPrimaryColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimary });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public Uri bitmapToUri(Bitmap bitmap) throws Exception {//here we alsoo define the path of the cropped image
        String username = activityUser.getAboutMe().getName();
        String imageFileName = username + "_" + Calendar.getInstance().getTimeInMillis();
        File file = null;
        String root = getActivity().getDir("Images", Context.MODE_PRIVATE).getAbsolutePath();
        File myDir = new File(root + "/Img");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        try {
            file = File.createTempFile(imageFileName, ".jpg", myDir.getAbsoluteFile());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (file != null) {
            FileOutputStream fout;
            try {
                fout = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, fout);
                fout.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Uri uri = Uri.fromFile(file);
            return uri;
        } else
            throw new Exception(">>Bitmap hasn't parsed into Uri!<<");
    }

    private void handleGooglePlacesResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(getActivity(), data);
            String city = (String) place.getName();
            activityUser.getAboutMe().setCity(city);
            Log.i(getTag(), "Place: " + place.getName());
            markAsSigned(etCity,city);
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(getActivity(), data);
            Log.i(getTag(), status.getStatusMessage());

        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }


    private String getRealPathFromUri(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }


        cursor.close();
        return res;
    }

    //--------------------Dialog of single choice----------------------------------
    private void dialogSingleChoice(final String[] stringsList, final String title) {
        final String[] OptionsStrings = stringsList;
        final int choiceMarked = -1;//getChoiceMarked(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        builder.setSingleChoiceItems(OptionsStrings, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //enter the choice to the field in the class
                myChoice = OptionsStrings[whichButton];
                myChoiceFlag = true;

            }
        });
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (myChoiceFlag) {
                    switch (title) {
                        case "My status": {
                            activityUser.getAboutMe().setStatus(myChoice);
                            markAsSigned(etStatus, myChoice);
//                            rgChildren.setVisibility(View.VISIBLE);
                            break;
                        }


                    }
                    myChoiceFlag = false;
                }
            }
        });
        builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myChoiceFlag = false;
//                rgChildren.setVisibility(View.INVISIBLE);
            }
        });
        builder.setOnDismissListener(new AlertDialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                myChoiceFlag = false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void markAsSigned(EditText editText, @Nullable String myChoice) {

        Drawable img = getResources().getDrawable(R.drawable.ic_done_all_white_24dp);
        int h = img.getIntrinsicHeight();
        int w = img.getIntrinsicWidth();
        img.setBounds(0, 0, w, h);

        if (myChoice != "" && myChoice != null)
            editText.setText(myChoice);
        else
            editText.setText(editText.getHint());
//        editText.setCompoundDrawables(null, null, img, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ObjectAnimator animator2 =ObjectAnimator.ofInt(img, "alpha",0,255);
//            animator2.setDuration(1000);
//            animator2.start();
            ObjectAnimator animator = ObjectAnimator.ofArgb(editText,"textColor" ,ContextCompat.getColor(getActivity().getApplicationContext(), R.color.black)
                    ,ContextCompat.getColor(getActivity().getApplicationContext(), R.color.marked_blue));
            animator.setDuration(1000);
            animator.setStartDelay(300);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setEvaluator(new ArgbEvaluator());
            animator.start();
        }else {
            editText.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.marked_blue));
        }
    }


    //--------------------Dialog of multi choice---------------------
    private void dialogMultiChoice(final String[] stringOptions, final String title) {
        final boolean[] boolOption = new boolean[stringOptions.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        final boolean[] flag = {false};
        builder.setMultiChoiceItems(stringOptions, boolOption, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                boolOption[which] = isChecked;
                String currentItem = stringOptions[which];
                myChoiceFlag = true;
            }
        });
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (myChoiceFlag) {

                    switch (title) {
                        case "My view": {
                            activityUser.getAboutMe().setView(allChoicesToArray(stringOptions, boolOption));
                            myChoiceFlag = false;
                            markAsSigned(etView,null);
                            break;
                        }
                        case "My Eda": {
                            activityUser.getAboutMe().setWitness(allChoicesToArray(stringOptions, boolOption));
                            myChoiceFlag = false;
                            markAsSigned(etWitness,null);
                            break;
                        }
                    }
                }
            }
        });
        builder.setNegativeButton("ביטול", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // ----------------- help function -----------------
    public List<String> allChoicesToArray(String[] str, boolean[] bool) {
        int arraySize = countTrues(bool);
        String[] array = new String[arraySize];
        int j = 0;
        for (int i = 0; i < str.length; i++)
            if (bool[i]) {
                array[j] = str[i];
                j++;
            }
        return Arrays.asList(array);
    }

    private int countTrues(boolean[] bool) {
        int count = 0;
        for (int i = 0; i < bool.length; i++) {
            if (bool[i]) {
                count++;
            }

        }
        return count;
    }

    //should stay on EDITPROFLE
    public void updatePictureFromActivity(Uri resultUri, String button_reqCode) {
        switch (myUCropChoiceButton){//TODO: future ucrop update: switch (button_reqCode)
            case Finals.App.PROFILE_PICTURE:{
                ibMainPicture.setImageURI(resultUri);
                uploadedJustNow = true;
                uploadPicture(resultUri,Finals.FireBase.storage.MAIN_PICTURE);
                break;
            }
            case Finals.App.SMALL_PICRTURE_1:{
                imageButton1.setImageURI(resultUri);
                uploadPicture(resultUri,Finals.FireBase.storage.SMALL_PICTURE_1);
                break;
            }
            case Finals.App.SMALL_PICRTURE_2:{
                imageButton2.setImageURI(resultUri);
                uploadPicture(resultUri,Finals.FireBase.storage.SMALL_PICTURE_2);
                break;
            }
            case Finals.App.SMALL_PICRTURE_3:{
                imageButton3.setImageURI(resultUri);
                uploadPicture(resultUri,Finals.FireBase.storage.SMALL_PICTURE_3);
                break;
            }
            case Finals.App.SMALL_PICRTURE_4:{
                imageButton4.setImageURI(resultUri);
                uploadPicture(resultUri,Finals.FireBase.storage.SMALL_PICTURE_4);
                break;
            }
            case Finals.App.SMALL_PICRTURE_5:{
                imageButton5.setImageURI(resultUri);
                uploadPicture(resultUri,Finals.FireBase.storage.SMALL_PICTURE_5);
                break;
            }
            case "": {
                Log.i("Tag", "exceptionwhile setting myUCropChoiceButton <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                break;
            }
        }

    }

    private void uploadPicture(Uri imageUri,String imageName) {
        uploadedJustNow = true;
        Storage.sendToStorage(getActivity(),imageName,activityUser,imageUri);
    }


    private void bGoToSearch() {
        updateUserDetails();
        Fragment searchFragment = new Search();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.NavigationDrawContainer, searchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public UpdateAsync UpdateAsyncCreator() {
        return new UpdateAsync(DB_users,activityUser,getActivity());
    }

    private void updateUserDetails() {
        String sDescription = etDescription.getText().toString();
        if (!sDescription.isEmpty())
            activityUser.getAboutMe().setFreeDescription(sDescription);
        Integer height = Integer.valueOf(tvHeight.getText().toString());
        if (height!=null) {
            activityUser.getAboutMe().setHeight(height);
        }
        UpdateAsync updateAsync = new UpdateAsync(DB_users,activityUser,getActivity());
        updateAsync.execute(activityUser);
    }


}