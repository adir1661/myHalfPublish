package com.myhalf.controller.activities;


import com.myhalf.model.entities.UserMatch;
import com.myhalf.model.entities.UserSeeker;

public class MyUser {

    public static UserSeeker userSeeker = null;//new userSeeker();
    public static UserMatch userMatch = null;// new UserMatch();

    //c-tor
    public static UserSeeker getUserSeeker(){
        if (userSeeker == null)
            userSeeker = new UserSeeker();
        return userSeeker;
    }

    public static void setUserSeeker( UserSeeker userSeeker1){
        userSeeker = userSeeker1;
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
