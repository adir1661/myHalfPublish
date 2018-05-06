package com.myhalf.model.entities.UnOfficial;

/**
 * Created by CardUser on 28/09/2017.
 */

public class City {
    private String name;
    private Coordinate coordinate;

    public City() {
    }

    public City(City other) {
        this.name = other.name;
        this.coordinate = new Coordinate(other.coordinate);
    }

    public City(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = new Coordinate(coordinate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getCoordinate() {
        return new Coordinate(coordinate);
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = new Coordinate(coordinate);
    }
}
