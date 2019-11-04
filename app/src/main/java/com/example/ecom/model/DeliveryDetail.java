package com.example.ecom.model;

public class DeliveryDetail {
    private int id;
    private String userName;
    private Address address;
    private boolean isSelected;

    public DeliveryDetail(String userName, Address address) {
        this.userName = userName;
        this.address = address;
        this.isSelected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
