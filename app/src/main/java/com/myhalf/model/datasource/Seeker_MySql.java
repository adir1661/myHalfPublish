package com.myhalf.model.datasource;

import android.content.ContentValues;

import com.myhalf.model.backend.DBManager;
import com.myhalf.model.backend.Finals;
import com.myhalf.model.entities.User;
import com.myhalf.model.entities.UserSeeker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Seeker_MySql implements DBManager {
    private final String username = "abargil";
    private final String webURL = "http://" + username + ".vlab.jct.ac.il/";

    @Override
    public boolean isExist(String email) {
        return false;
    }

    @Override
    public User getUser(String id) {
        UserSeeker userSeeker = new UserSeeker();
        // insert code to get user seeker from DB.
        return userSeeker;
    }

    @Override
    public String addUser(User user) {
        String str ="";
        try {
            str = PhpTools.POST(webURL + "insertUser.php", Tools.userSeekerToContentValues((UserSeeker) user));
            str = PhpTools.removeSpaces(str);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return str;
    }

    @Override
    public boolean removeUser(long id) {

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Finals.DB.User.ID, id);
            PhpTools.POST(webURL + "removeUser.php", contentValues);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updateUser(String id, User values) {
        try {
            PhpTools.POST(webURL + "updateUser.php",Tools.userSeekerToContentValues((UserSeeker) values));
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<UserSeeker> getUsersList() {
        List<UserSeeker> allUsers = new ArrayList<>();
        try {
            String usersJson = PhpTools.GET(webURL + "getAllUsers.php");
            JSONArray usersJsonArray = new JSONObject(usersJson).getJSONArray("users");
            List<ContentValues> ABList = new ArrayList<>();
            String ABJsonString = PhpTools.GET(webURL + "getAllAboutMe.php");
            JSONArray amJsonArray = new JSONObject(ABJsonString).getJSONArray("aboutMe");
            for (int i = 0; i < amJsonArray.length(); i++) {
                JSONObject AMjsonObject = amJsonArray.getJSONObject(i);

                ContentValues ABContentValues = PhpTools.JsonToContentValues(AMjsonObject);
                ABList.add(ABContentValues);
            }

            for (int i = 0; i < usersJsonArray.length(); i++) {
                JSONObject usersJsonObject = usersJsonArray.getJSONObject(i);
                ContentValues usersCV = PhpTools.JsonToContentValues(usersJsonObject);
                UserSeeker userSeeker = new UserSeeker();
                long _id = usersCV.getAsLong(Finals.DB.User.ID);
                try {
                    boolean flag = false;
                    for (ContentValues ABitem : ABList) {
                        if (ABitem.getAsLong(Finals.DB.AboutMe.ID) == _id) {
                            flag = true;
//                            ABitem.put(Finals.AboutMe.GENDER,userSeeker.getAboutMe().getGender().toString());
//                            ABitem.put(Finals.AboutMe.HEIGHT,userSeeker.getAboutMe().getHeight());
//                            ABitem.put(Finals.AboutMe.BIRTHDAY,userSeeker.getAboutMe().getBirthday().toString());
//TODO: oginize the database where aboutMe is aboutMe and user its user
                            usersCV.putAll(ABitem);
                            userSeeker=Tools.ContentValuesToUserSeeker(usersCV);
                            ABList.remove(ABitem);
                            break;
                        }
                        else {
                            userSeeker = Tools.ContentValuesToUserSeeker(usersCV);
                        }
                        if (!flag)
                            throw new Exception("aboutMe ID isn't found!");
                    }
                } catch (Exception e) {
                    StackTraceElement[] stack = e.getStackTrace();
                    String str = "";
                    str += "<-------------------------------------------------------->\n";
                    for (StackTraceElement item : stack) {
                        str += "Source: " + item;
                        str += "\n";
                        str += "\t Class: " + item.getClassName() + "\n";
                        str += "\t Method: " + item.getMethodName() + "\n";
                        str += "\t Line: " + item.getLineNumber() + "\n";
                    }
                    str += "\n<-------------------------------------------------------->\n";
                    str += "Message: " + e.getMessage() + "\n";
//                Log.d(TAG, str);
                    System.out.println(str);
                    return null;
                }
                allUsers.add(userSeeker);
            }


            return allUsers;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getMaxId() {
        try {
            String idString = PhpTools.GET(webURL+"chechIfNewMember.php");
            long id = Long.parseLong(PhpTools.removeSpaces(idString));
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
