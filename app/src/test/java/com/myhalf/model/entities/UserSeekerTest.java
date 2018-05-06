package com.myhalf.model.entities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Adir on 12/7/2017.
 */
public class UserSeekerTest {
    @Test
    public void getAboutMeName() throws Exception {
        UserSeeker user = new UserSeeker();
        user.getAboutMe().setName("adir");
        System.out.println(user.getAboutMe().getName());
    }

}