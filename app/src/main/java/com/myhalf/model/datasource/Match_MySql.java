package com.myhalf.model.datasource;

import android.content.ContentValues;
import android.database.Cursor;

import com.myhalf.model.backend.DBManager;
import com.myhalf.model.entities.User;

import java.util.List;

public class Match_MySql implements DBManager {

    private final String username="abargil";
    private final String webURL= "http://"+username+".vlab.ct.ac.il/abargil_/";
    @Override
    public boolean isExist(String email, String password) {
        return false;
    }

    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public String addUser(ContentValues contentValues) {
        return "0";
    }

    @Override
    public boolean removeUser(long id) {
        return false;
    }

    @Override
    public boolean updateUser(String id, ContentValues values) {
        return false;
    }

    @Override
    public List<User> getUsersList() {
        return null;
    }

    @Override
    public long getMaxId() {
        return 0;
    }
}