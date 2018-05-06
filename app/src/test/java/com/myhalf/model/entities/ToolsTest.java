package com.myhalf.model.entities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Adir on 12/5/2017.
 */
public class ToolsTest {
    @Test
    public void clarifyDateString() throws Exception {
        String myDate = "1/4/24";
        myDate = DateBuilt.Tools.clarifyDateString(myDate);
        System.out.println(myDate);
        myDate = "25/3/17";
        myDate = DateBuilt.Tools.clarifyDateString(myDate);
        System.out.println(myDate);
    }

}