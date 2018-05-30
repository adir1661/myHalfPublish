package com.myhalf.model.backend;

import android.content.ContentValues;

import com.myhalf.model.entities.User;

import java.util.List;

public interface DBManager {

    public boolean isExist(String email);

    User getUser(String Email);

    String addUser(ContentValues contentValues);

    public  boolean removeUser(long id);

    boolean updateUser(String id, ContentValues values);

    List<? extends User> getUsersList();//"? extends User" this is a way to interface a list that

    long getMaxId();
    //is List<userSeeker
    // or userMatch>

}
