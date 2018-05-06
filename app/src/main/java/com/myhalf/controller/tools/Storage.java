package com.myhalf.controller.tools;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhalf.model.entities.UserSeeker;
import com.squareup.okhttp.internal.http.RetryableSink;

import java.lang.ref.WeakReference;

public class Storage {

    //----------------------------------download from storage---------------------------------------------
    private static void downloadImageToView(String storageDirectory, String imageName, ImageView destinationImage, Fragment fragment) {
        Glide.with(fragment)//TODO: create more effective query, to save image on cache
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference().child(storageDirectory+imageName))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(destinationImage.getDrawable())
                .into(destinationImage);

    }

    public static void getFromStorage(Fragment fragment, String pictureString, ImageView imageView, UserSeeker userSeeker) {
        String storageDirectory = "images/"+ userSeeker.getId() +"/" ;
        String imageName = pictureString +".jpg";
        downloadImageToView(storageDirectory, imageName, imageView,fragment);
    }

    // -------------------------------------upload to storage------------------------------------------------

    public static String sendToStorage(Activity activity,String pictureString,UserSeeker userSeeker,Uri imageUri){
        String storageDirectory = "images/"+ userSeeker.getId() +"/" ;
        String imageName = pictureString +".jpg";
        new Storage.UploadPictureToFirebaseAsync(activity,storageDirectory+imageName).execute(imageUri);
        return storageDirectory+imageName;
    }
    public static class UploadPictureToFirebaseAsync extends AsyncTask<Uri, ProgressBar, Void> {
        private final ProgressDialog mDialog;
        private WeakReference<Context> mContext;
        private final StorageReference mStorageRef;
        String mFilePath;

        public UploadPictureToFirebaseAsync(Context context, String filePath) {
            this.mContext = new WeakReference<Context>(context);
            this.mDialog = new ProgressDialog(context);
            this.mDialog.setIndeterminate(true);
            this.mDialog.setMessage("Uploading Docs");
            this.mStorageRef = FirebaseStorage.getInstance().getReference();
            this.mFilePath =filePath;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final ProgressDialog progressDialog = mDialog;
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Uri... uris) {
            final StorageReference mainPicRef = mStorageRef.child(mFilePath);
            mainPicRef.putFile(uris[0])
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(mContext.get().getApplicationContext(), "Picture Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            mDialog.dismiss();
                            Toast.makeText(mContext.get().getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double precent =  100.0*(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            taskSnapshot.getTotalByteCount();
                            mDialog.setMessage(((int) precent) + "% Uploading...");
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
