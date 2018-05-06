package com.myhalf.model.datasource;

import android.content.ContentValues;

import com.myhalf.model.backend.Finals;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Adir on 12/25/2017.
 */
public class DatasourceToolsTest {
    @Test
    public void contentValuesToSeeker() throws Exception {

    }

    @Test
    public void seekerToContentValues() throws Exception {
        //        userSeeker myUser = new userSeeker();
//        myUser.setUserName("adir");
//        myUser.setPassword("123");
//        myUser.setCellPhone("050");
//        myUser.getAboutMe().setGender(Gender.MAN);
//        myUser.getAboutMe().setBirthday("25/3/2007");
//        ContentValues contentValues = Tools.SeekerToContentValues(myUser);
//        String str = contentValues.getAsString(Finals.User.NAME);
//        System.out.println(str);
//        assertTrue((myUser.getUserName()).equals(contentValues.getAsString(Finals.User.NAME)));
        ContentValues contentValues = new ContentValues();
        contentValues.put(Finals.DB.User.NAME,"adir");
        String str = contentValues.getAsString(Finals.DB.User.NAME);
        assertTrue(str.equals("adir"));
    }
}