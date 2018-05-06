package com.myhalf.model.datasource;

import android.content.ContentValues;

import com.myhalf.model.entities.DateBuilt;
import com.myhalf.model.entities.Enums;
import com.myhalf.model.entities.UserSeeker;

import org.junit.Test;

/**
 * Created by Adir on 12/22/2017.
 */
public class Seeker_MySqlTest {
    @Test
    public void isExist() throws Exception {
    }

    @Test
    public void addUser() throws Exception {
        UserSeeker myUser = new UserSeeker();
        myUser.getAboutMe().setName("adir");
        myUser.setPassword("123");
        myUser.setCellPhone("050");
        myUser.getAboutMe().setGender(Enums.Gender.MALE);
        myUser.getAboutMe().setBirthday(new DateBuilt("25/3/2007"));
        if (myUser.getAboutMe().getBirthday() == null )
            System.out.println("hi!");
        Seeker_MySql _new =new Seeker_MySql();
        ContentValues contentValues=Tools.userSeekerToContentValues(myUser);
        _new.addUser(contentValues);




    }

    @Test
    public void removeUser() throws Exception {
    }

    @Test
    public void getAllUsers() throws Exception {
    }

    @Test
    public void updateUser() throws Exception {
    }

    @Test
    public void getUsersList() throws Exception {
    }

}