package com.example.chien.location.model;

public class NodeGis {
    private float Lat;
    private float Lng;

    public NodeGis() {
    }

    public NodeGis(float lat, float lng) {
        this.Lat = lat;
        this.Lng = lng;
    }

    public float getLat() {
        return Lat;
    }

    public void setLat(float lat) {
        this.Lat = lat;
    }

    public float getLng() {
        return Lng;
    }

    public void setLng(float lng) {
        this.Lng = lng;
    }

    @Override
    public String toString() {
        return getLat()+","+getLng();
    }
}
