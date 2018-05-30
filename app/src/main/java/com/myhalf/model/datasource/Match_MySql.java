package com.myhalf.model.datasource;

import android.content.ContentValues;

import com.myhalf.model.backend.DBManager;
import com.myhalf.model.entities.User;

import java.util.List;

public class Match_MySql implements DBManager {

    private final String username="abargil";
    private final String webURL= "http://"+username+".vlab.ct.ac.il/abargil_/";
    @Override
    public boolean isExist(String email) {
        return false;
    }

    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public String addUser(User contentValues) {
        return "0";
    }

    @Override
    public boolean removeUser(long id) {
        return false;
    }

    @Override
    public boolean updateUser(String id, User values) {
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
