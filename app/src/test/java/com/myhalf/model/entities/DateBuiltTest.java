package com.myhalf.model.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateBuiltTest {
    @Test
    public void getAge() throws Exception {
        DateBuilt date = new DateBuilt("1/1/88");
        int age = date.findAge();
        assertEquals(30, age);
    }
}