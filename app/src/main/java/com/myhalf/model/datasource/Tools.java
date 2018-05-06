package com.myhalf.model.datasource;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.AboutMe;
import com.myhalf.model.entities.AboutMeSeeker;
import com.myhalf.model.entities.DateBuilt;
import com.myhalf.model.entities.Enums;
import com.myhalf.model.entities.UserMatch;
import com.myhalf.model.entities.UserSeeker;

import java.util.Arrays;
import java.util.List;

public class Tools {

//--------------------------------Seeker tools------------------------------------

    public static UserSeeker ContentValuesToUserSeeker(ContentValues contentValues) {
        UserSeeker user = new UserSeeker();
        user.setId(contentValues.getAsString(Finals.DB.User.ID));
        user.setPassword(contentValues.getAsString(Finals.DB.User.PASSWORD));
        user.setEmailAdress(contentValues.getAsString(Finals.DB.User.EMAIL));
        user.setCellPhone(contentValues.getAsString(Finals.DB.User.CELLPHONE));
        user.setAboutMe(ContentValuesToAboutMeSeeker(contentValues));
        user.getAboutMe().setName(contentValues.getAsString(Finals.DB.User.NAME));
        user.setFirebaseToken(contentValues.getAsString(Finals.DB.User.FIREBASE_TOKEN));
        return user;
    }

    public static AboutMeSeeker ContentValuesToAboutMeSeeker(ContentValues contentValues) {
        AboutMeSeeker aboutMe = new AboutMeSeeker();//todo: if there is an incorrect account on the database there is and error in this methud...
        if (contentValues.containsKey(Finals.DB.AboutMe.GENDER))
            aboutMe.setGender(Enums.Gender.valueOf(contentValues.getAsString(Finals.DB.AboutMe.GENDER)));
        if (contentValues.containsKey(Finals.DB.AboutMe.BIRTHDAY))
            aboutMe.setBirthday(new DateBuilt(contentValues.getAsString(Finals.DB.AboutMe.BIRTHDAY)));
        if (contentValues.containsKey(Finals.DB.AboutMe.FREE_DESCRIPTION))
            aboutMe.setFreeDescription(contentValues.getAsString(Finals.DB.AboutMe.FREE_DESCRIPTION));
        if (contentValues.containsKey(Finals.DB.AboutMe.HEIGHT))
            aboutMe.setHeight(contentValues.getAsInteger(Finals.DB.AboutMe.HEIGHT));
        if (contentValues.containsKey(Finals.DB.AboutMe.VIEW )&& contentValues.getAsString(Finals.DB.AboutMe.VIEW)!= null)
            aboutMe.setView(stringToArray(contentValues.getAsString(Finals.DB.AboutMe.VIEW)));
        if (contentValues.containsKey(Finals.DB.AboutMe.WHAT_I_DO))
            aboutMe.setWhatIDo(stringToArray(contentValues.getAsString(Finals.DB.AboutMe.WHAT_I_DO)));
        if (contentValues.containsKey(Finals.DB.AboutMe.EDA))
            aboutMe.setWitness(stringToArray(contentValues.getAsString(Finals.DB.AboutMe.EDA)));
        if (contentValues.containsKey(Finals.DB.AboutMe.STATUS))
            aboutMe.setStatus(contentValues.getAsString(Finals.DB.AboutMe.STATUS));
        if (contentValues.containsKey(Finals.DB.AboutMe.CITY))
            aboutMe.setCity(contentValues.getAsString(Finals.DB.AboutMe.CITY));
        return aboutMe;
    }

    public static ContentValues userSeekerToContentValues(UserSeeker user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Finals.DB.User.ID, user.getId());
        contentValues.put(Finals.DB.User.PASSWORD, user.getPassword());
        contentValues.put(Finals.DB.User.EMAIL, user.getEmailAdress());
        contentValues.put(Finals.DB.User.CELLPHONE, user.getCellPhone());
        contentValues.putAll(AboutMeSeekerToContentValues(user.getAboutMe()));
        contentValues.put(Finals.DB.User.NAME, user.getAboutMe().getName());
        contentValues.put(Finals.DB.User.FIREBASE_TOKEN, user.getFirebaseToken());


        return contentValues;
    }

    public static ContentValues AboutMeSeekerToContentValues(AboutMeSeeker aboutMe) {
        ContentValues contentValues = new ContentValues();
        if (aboutMe.getGender() != null)
            contentValues.put(Finals.DB.AboutMe.GENDER, aboutMe.getGender().toString());
        if (aboutMe.getStatus() != null)
            contentValues.put(Finals.DB.AboutMe.STATUS, aboutMe.getStatus().toString());
        if (aboutMe.getBirthday() != null)
            contentValues.put(Finals.DB.AboutMe.BIRTHDAY, aboutMe.getBirthday().toString());
        if (aboutMe.getFreeDescription() != null)
            contentValues.put(Finals.DB.AboutMe.FREE_DESCRIPTION, aboutMe.getFreeDescription());
        if (aboutMe.getHeight() != 0)
            contentValues.put(Finals.DB.AboutMe.HEIGHT, aboutMe.getHeight());
        if (aboutMe.getView().isEmpty())
            contentValues.put(Finals.DB.AboutMe.VIEW, arrayToString(aboutMe.getView()));
        if (aboutMe.getWhatIDo() != null)
            contentValues.put(Finals.DB.AboutMe.WHAT_I_DO, arrayToString(aboutMe.getWhatIDo()));
        if (aboutMe.getWitness() != null)
            contentValues.put(Finals.DB.AboutMe.EDA, aboutMe.getWitness().toString());
            contentValues.put(Finals.DB.AboutMe.CITY, aboutMe.getCity());
        return contentValues;
    }

    public static Cursor SeekerListToCursor(List<UserSeeker> userlist) {
        String[] columns = new String[]
                {
                        Finals.DB.User.ID,
                        Finals.DB.User.NAME,
                        Finals.DB.User.PASSWORD,
                        Finals.DB.User.EMAIL,
                        Finals.DB.User.CELLPHONE,
                        Finals.DB.AboutMe.GENDER,
                        Finals.DB.AboutMe.BIRTHDAY,
                        Finals.DB.AboutMe.FREE_DESCRIPTION,
                        Finals.DB.AboutMe.HEIGHT

                };

        MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (UserSeeker item : userlist) {
            matrixCursor.addRow(new Object[]{item.getId(), item.getAboutMe().getName(), item.getPassword(), item.getEmailAdress(), item.getCellPhone(),
                    item.getAboutMe().getGender(), item.getAboutMe().getBirthday(), item.getAboutMe().getFreeDescription()});
        }

        return matrixCursor;
    }

    //-----------------------------------------private tools--------------------------------------------------

    public static String arrayToString(List<String> enums) {
        String tmp = null;
        for (int i = 0; i < enums.size(); i++) {
            tmp += enums.get(i) + "~";
        }
        return tmp;
    }

    public static List<String> stringToArray(String str) {
        if (str != null) {
            String[] enums = str.split("~");
            return Arrays.asList(enums);
        }else
            return null;
    }

    //-------------------------------matchmaker tools----------------------------------
    public static UserMatch contentValuesToUserMatch(ContentValues contentValues) {
        UserMatch user = new UserMatch();
        //-----------------------------------------UserMatch Values----------------------------

        user.setId(contentValues.getAsString(Finals.DB.User.ID));
        user.getAboutMe().setName(contentValues.getAsString(Finals.DB.User.NAME));
        user.setPassword(contentValues.getAsString(Finals.DB.User.PASSWORD));
        user.setEmailAdress(contentValues.getAsString(Finals.DB.User.EMAIL));
        user.setCellPhone(contentValues.getAsString(Finals.DB.User.CELLPHONE));
        user.setFirebaseToken(contentValues.getAsString(Finals.DB.User.FIREBASE_TOKEN));


        //-----------------------------------------UserMatch.AboutMe Values----------------------------

        user.getAboutMe().setGender(Enums.Gender.valueOf(contentValues.getAsString(Finals.DB.AboutMe.GENDER)));
        user.getAboutMe().setBirthday(new DateBuilt(contentValues.getAsString(Finals.DB.AboutMe.BIRTHDAY)));
        user.getAboutMe().setFreeDescription(contentValues.getAsString(Finals.DB.AboutMe.FREE_DESCRIPTION));

        return user;
    }

    public static ContentValues userMatchToContentValues(UserMatch user) {

        ContentValues contentValues = new ContentValues();

        //-----------------------------------------UserMatch Values----------------------------

        contentValues.put(Finals.DB.User.ID, user.getId());
        contentValues.put(Finals.DB.User.NAME, user.getAboutMe().getName());
        contentValues.put(Finals.DB.User.PASSWORD, user.getPassword());
        contentValues.put(Finals.DB.User.EMAIL, user.getEmailAdress());
        contentValues.put(Finals.DB.User.CELLPHONE, user.getCellPhone());
        contentValues.put(Finals.DB.User.FIREBASE_TOKEN, user.getFirebaseToken());


        //-----------------------------------------UserMatch.AboutMe Values----------------------------

        contentValues.put(Finals.DB.AboutMe.GENDER, user.getAboutMe().getGender().toString());
        contentValues.put(Finals.DB.AboutMe.BIRTHDAY, user.getAboutMe().getBirthday().toString());
        contentValues.put(Finals.DB.AboutMe.FREE_DESCRIPTION, user.getAboutMe().getFreeDescription());

        return contentValues;
    }

    public ContentValues aboutMeSeekerToContentValues(AboutMe aboutMe) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Finals.DB.AboutMe.STATUS, aboutMe.getGender().toString());
        contentValues.put(Finals.DB.AboutMe.BIRTHDAY, aboutMe.getBirthday().toString());
        contentValues.put(Finals.DB.AboutMe.FREE_DESCRIPTION, aboutMe.getFreeDescription());
        return contentValues;
    }

    public static Cursor MatchmakerListToCursor(List<UserMatch> matchmakerList) {
        String[] columns = new String[]
                {
                        Finals.DB.User.ID,
                        Finals.DB.User.NAME,
                        Finals.DB.User.PASSWORD,
                        Finals.DB.User.EMAIL,
                        Finals.DB.User.CELLPHONE,
                        Finals.DB.AboutMe.GENDER,
                        Finals.DB.AboutMe.BIRTHDAY,
                        Finals.DB.AboutMe.FREE_DESCRIPTION

                };

        MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (UserMatch item : matchmakerList) {
            matrixCursor.addRow(new Object[]{item.getId(), item.getAboutMe().getName(), item.getPassword(), item.getEmailAdress(), item.getCellPhone(),
                    item.getAboutMe().getGender(), item.getAboutMe().getBirthday(), item.getAboutMe().getFreeDescription()});
        }
        return matrixCursor;
    }
}
