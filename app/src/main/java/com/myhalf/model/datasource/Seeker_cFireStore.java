package com.myhalf.model.datasource;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.User;
import com.myhalf.model.entities.UserSeeker;

import java.util.ArrayList;
import java.util.List;

public class Seeker_cFireStore implements DBManager {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private String TAG = "Seeker Firestore";


    @Override
    public boolean isExist(String email, String password) {
        return false;
    }

    @Override
    public User getUser(String email) {
        final UserSeeker[] userSeeker = new UserSeeker[1];
        Task<QuerySnapshot> is = db.collection(Finals.FireBase.FirestoreCloud.MAIN_COLLECTION).
                whereEqualTo(Finals.FireBase.FirestoreCloud.EMAIL,email).
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                userSeeker[0] = documentSnapshots.toObjects(UserSeeker.class).toArray(new UserSeeker[0])[0];

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        int counter = 0;
        while (counter <= 60) {
            if (is.isComplete())
                return userSeeker[0];
            else
                try {
                    Thread.sleep(250);
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    @Override
    public String addUser(ContentValues contentValues) {
        final UserSeeker userSeeker = Tools.ContentValuesToUserSeeker(contentValues);
        final DocumentReference docRef = db.collection(Finals.FireBase.FirestoreCloud.MAIN_COLLECTION).document(userSeeker.getEmailAdress());
        String id = docRef.getId();
        contentValues.put(Finals.DB.User.ID,id);

        docRef.set(userSeeker)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + docRef.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });
        return id;
    }

    @Override
    public boolean removeUser(long id) {
        return false;
    }

    @Override
    public boolean updateUser(String id, ContentValues values) {
        UserSeeker userSeeker = Tools.ContentValuesToUserSeeker(values);
        DocumentReference washingtonRef = db.collection(Finals.FireBase.FirestoreCloud.MAIN_COLLECTION).document(id);

// Set the "isCapital" field of the city 'DC'
        washingtonRef
                .set(userSeeker)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        return false;
    }

    @Override
    public List<? extends User> getUsersList() {
        final List<UserSeeker> userSeekerList = new ArrayList<>();
        Task<QuerySnapshot> is = db.collection(Finals.FireBase.FirestoreCloud.MAIN_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()){
                                for (DocumentSnapshot snapshot:task.getResult())
                                    userSeekerList.add(snapshot.toObject(UserSeeker.class));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        int counter = 0;
        while (counter <= 60) {
            if (is.isComplete())
                return userSeekerList;
            else
                try {
                    Thread.sleep(250);
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    @Override
    public long getMaxId() {
        return 0;
    }
}
