package com.example.ecom.model;

public class Address {
    private String house;
    private String locality;
    private String city;
    private String state;
    private String landmark;

    public Address(String house, String locality, String city, String state, String landmark) {
        this.house = house;
        this.locality = locality;
        this.city = city;
        this.state = state;
        this.landmark = landmark;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
