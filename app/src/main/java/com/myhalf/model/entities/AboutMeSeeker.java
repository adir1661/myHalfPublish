package com.myhalf.model.entities;


import java.util.ArrayList;
import java.util.List;

public class AboutMeSeeker extends AboutMe {
    protected int height;// seeker only
    protected List<String> whatIDo;//seeker only
    protected List<String> view;
    protected List<String> languages;// seeker only -
    protected List<String> witness;// seeker only - enum

    protected String status;// seeker only
    protected String shortSentence;
    protected String livingArea = null;
    protected String city = null;


//-----------------C-tors--------------------------------

    public AboutMeSeeker() {
        super();
        this.height = 0;
        whatIDo = new ArrayList<>();
        view = new ArrayList<>();
        languages = new ArrayList<>();
        witness =  new ArrayList<>();
    }

//-----------------Getters & Setters----------------------


    public int getHeight() {return height;}
    public void setHeight(int height) {this.height = height;}

    public List<String> getWhatIDo() {return whatIDo;}
    public void setWhatIDo(List<String> whatIDo) {
        this.whatIDo = whatIDo;}

    public List<String> getLanguages() {return languages;}
    public void setLanguages(List<String> languages) {this.languages = languages;}

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getWitness() {
        return witness;
    }
    public void setWitness(List<String> witness) {
        this.witness = witness;
    }

    public String getShortSentence() { return shortSentence; }
    public void setShortSentence(String shortSentence) {
        this.shortSentence = shortSentence;
    }

    public List<String> getView() {return view;}
    public void setView(List<String> view) {this.view = view;}

    public String getLivingArea() {
        return livingArea;
    }

    public void setLivingArea(String livingArea) {
        this.livingArea = livingArea;
    }
}
