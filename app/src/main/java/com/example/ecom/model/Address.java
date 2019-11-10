package com.example.ecom.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Address implements Parcelable {
    private String house;
    private String locality;
    private String city;
    private String state;
    private String pincode;
    private String landmark;

    public Address(String house, String locality, String city, String state, String pincode, String landmark) {
        this.house = house;
        this.locality = locality;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    protected Address(Parcel in) {
        house = in.readString();
        locality = in.readString();
        city = in.readString();
        state = in.readString();
        pincode = in.readString();
        landmark = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(house);
        dest.writeString(locality);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(pincode);
        dest.writeString(landmark);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
