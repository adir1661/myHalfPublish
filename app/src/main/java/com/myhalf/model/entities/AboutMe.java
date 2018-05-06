package com.myhalf.model.entities;


import java.io.Serializable;

public abstract class AboutMe implements Serializable {
    protected String name;
    protected Enums.Gender gender = Enums.Gender.NULL;
    protected DateBuilt birthday;
    protected String freeDescription;
//-----------------------------------------methods-----------------------------------------------------


//-----------------c-tors--------------------------------

    public AboutMe() {
        this.birthday = new DateBuilt();
    }

    public AboutMe(String name, Enums.Gender gender, DateBuilt birthday, String freeDescription) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.freeDescription = freeDescription;
    }

//-----------------Getters & Setters----------------------


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {return this.name;}

    public void setBirthday(DateBuilt birthday) {
        this.birthday = birthday;
    }
    public DateBuilt getBirthday() {
        return birthday;
    }

    public String getFreeDescription() {
        return freeDescription;
    }
    public void setFreeDescription(String freeDescription) {
        this.freeDescription = freeDescription;
    }

    public void setGender(Enums.Gender gender) {
        this.gender =gender;
    }
    public Enums.Gender getGender() {
        return gender;
    }

}