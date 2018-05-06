package com.myhalf.model.entities;

import java.io.Serializable;

public class SearchProperties implements Serializable{

    private int minAge,
                maxAge,
                minHeight,
                maxHeight;
    private int distanceFromMyCity;

    private String[] status;
    private String[] view;
    private String[] witness;


    public SearchProperties() {
    }


    public SearchProperties(int minAge, int maxAge, int minHeight, int maxHeight, int distanceFromMyCity, String[] status, String[] view, String[] witness) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.distanceFromMyCity = distanceFromMyCity;
        this.status = status;
        this.view = view;
        this.witness = witness;
    }

    public SearchProperties(SearchProperties other) {
        this.minAge = other.minAge;
        this.maxAge = other.maxAge;
        this.minHeight = other.minHeight;
        this.maxHeight = other.maxHeight;
        this.distanceFromMyCity = other.distanceFromMyCity;
        this.status = other.status;
        this.view = other.view;
        this.witness = other.witness;
    }



    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public String[] getWitness() {
        return witness;
    }

    public int getDistanceFromMyCity() {
        return distanceFromMyCity;
    }

    public void setDistanceFromMyCity(int distanceFromMyCity) {
        this.distanceFromMyCity = distanceFromMyCity;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public String[] getView() {
        return view;
    }

    public void setView(String[] view) {
        this.view = view;
    }

    public void setWitness(String[] witness) {
        this.witness = witness;
    }
}
