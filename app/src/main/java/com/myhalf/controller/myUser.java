package com.myhalf.controller;


import com.myhalf.model.entities.UserMatch;
import com.myhalf.model.entities.UserSeeker;

public class myUser {

    public static UserSeeker userSeeker = null;//new userSeeker();
    public static UserMatch userMatch = null;// new UserMatch();

    //c-tor
    public static UserSeeker getUserSeeker(){
        if (userSeeker == null)
            userSeeker = new UserSeeker();
        return userSeeker;
    }

    public static  UserMatch getUserMatch(){
        if (userMatch == null)
            userMatch = new UserMatch();
        return userMatch;
    }

    public static void dismissUserSeeker(){
        userSeeker = null;
    }
}
