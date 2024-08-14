package com.example.geofencing.model;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Polygon {
    private String name;
    private List<CustomLatLng> points;

    public Polygon() {

    }

    public Polygon(String name, List<CustomLatLng> points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CustomLatLng> getPoints() {
        return points;
    }

    public void setPoints(List<CustomLatLng> points) {
        this.points = points;
    }
}