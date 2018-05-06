package com.myhalf.model.entities.UnOfficial;

/**
 * Created by User on 09/10/2017.
 */

public class Card {
    private String name;
    private String age;
    private String migzar;
    private String status;
    private String city;
    private String aboutMe;
    private String imgURL;
    public boolean isLiked=false;
    public boolean isLoved=false;

    public Card(String name, String age, String migzar, String status, String city, String aboutMe, String imgURL) {
        this.name = name;
        this.age = age;
        this.migzar = migzar;
        this.status = status;
        this.city = city;
        this.aboutMe = aboutMe;
        this.imgURL = imgURL;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getMigzar() {
        return migzar;
    }
    public void setMigzar(String migzar) {
        this.migzar = migzar;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getAboutMe() {
        return aboutMe;
    }
    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getImgURL() {
        return imgURL;
    }
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
