package com.myhalf.controller.tools;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.ImageView;

import com.myhalf.R;
import com.myhalf.model.entities.UserSeeker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class OtherTools {
    public static Bitmap imageViewToBitmap(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        return imageView.getDrawingCache();
    }

    public static Uri bitmapToUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap uriToBitmap(Activity activity,Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
    }

    public static Uri bitmapToUri(Activity activity, Bitmap bitmap, UserSeeker userSeeker) throws Exception {//here we alsoo define the path of the cropped image
        String username = userSeeker.getAboutMe().getName();
        String imageFileName = username + "_" + Calendar.getInstance().getTimeInMillis();
        File file = null;
        String root = activity.getDir("Images", Context.MODE_PRIVATE).getAbsolutePath();
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
            return Uri.fromFile(file);
        } else
            throw new Exception(">>Bitmap hasn't parsed into Uri!<<");

    }

    public static String ListToString(List<String> stringList){
        String string = new String();
        if (!stringList.isEmpty()) {
            for (String item : stringList) {
                string += bigLetterFirst(item) + ", ";
            }
            string = string.substring(0, string.length() - 2);
        }
        return string;
    }

    public static String fixLinesAndSize(@NonNull String string) {
        if (string!= null) {
            string = bigLetterFirst(string);
            string = string.replaceAll("_", " ");
        }
        return string;
    }

    public static String bigLetterFirst(String userName) {

        if (userName!= null && userName.length() >= 1)
            return userName.substring(0, 1).toUpperCase() + userName.substring(1).toLowerCase();
        else return userName;
    }

    public static void markAsSigned(Activity activity, EditText editText, @Nullable String myChoices) {

        Drawable img = activity.getResources().getDrawable(R.drawable.ic_done_all_white_24dp);
        int h = img.getIntrinsicHeight();
        int w = img.getIntrinsicWidth();
        img.setBounds(0, 0, w, h);

        if (myChoices != null)
            editText.setText(myChoices);
        else
            editText.setText(editText.getHint());
        editText.setCompoundDrawables(null, null, img, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ObjectAnimator animator = ObjectAnimator.ofArgb(editText,"textColor" , ContextCompat.getColor(activity.getApplicationContext(), R.color.black)
                    ,ContextCompat.getColor(activity.getApplicationContext(), R.color.marked_blue));
            animator.setDuration(1000);
            animator.setStartDelay(300);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setEvaluator(new ArgbEvaluator());
            animator.start();
        }else {
            editText.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.marked_blue));
        }
    }

    public static List<String> allChoicesToArray(String[] str, boolean[] boolArray) {
        int arraySize = countTrues(boolArray);
        String[] array = new String[arraySize];
        int j = 0;
        for (int i = 0; i < str.length; i++)
            if (boolArray[i]) {
                array[j] = str[i];
                j++;
            }
        return Arrays.asList(array);
    }

    public static int countTrues(boolean[] bool) {
        int count = 0;
        for (int i = 0; i < bool.length; i++) {
            if (bool[i]) {
                count++;
            }

        }
        return count;
    }
}
