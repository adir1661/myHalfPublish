package com.myhalf.model.entities.UnOfficial;

/**
 * Created by CardUser on 28/09/2017.
 */

public class Coordinate {

    private double latitude;
    private double longitude;


    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate(Coordinate other) {
        this.latitude = other.latitude;
        this.longitude = other.longitude;
    }

    public Coordinate() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
