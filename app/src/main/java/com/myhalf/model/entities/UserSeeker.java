package com.myhalf.model.entities;


public class UserSeeker extends User{

    private SearchProperties searchProperties;
    protected AboutMeSeeker aboutMe;

    protected class AvailableTo{
        private int a_minAge;
        private int a_maxAge;

        //------------getter & setter-------------


        public int getA_minAge() {return a_minAge;}
        public void setA_minAge(int a_minAge) {this.a_minAge = a_minAge;}

        public int getA_maxAge() {return a_maxAge;}
        public void setA_maxAge(int a_maxAge) {this.a_maxAge = a_maxAge;}
    }


    //-----------------------------------------methods-----------------------------------------------------
    //-----------------c-tors-------------------------
    public UserSeeker() {
        super();
        this.aboutMe = new AboutMeSeeker();
        this.searchProperties = new SearchProperties();

    }

    public UserSeeker(UserSeeker other) {
        super(other);
        this.searchProperties = other.searchProperties;
        this.aboutMe = other.aboutMe;
    }
    //-----------------getters&setters----------------------

    public SearchProperties getSearchProperties() {
        return new SearchProperties(searchProperties);
    }
    public void setSearchProperties(SearchProperties searchProperties) {
        this.searchProperties = new SearchProperties(searchProperties);
    }

    public AboutMeSeeker getAboutMe() {
        return aboutMe;
    }
    public void setAboutMe(AboutMeSeeker aboutMe) {
        this.aboutMe = aboutMe;
    }
    }