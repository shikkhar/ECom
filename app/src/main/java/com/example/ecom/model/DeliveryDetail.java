package com.example.ecom.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryDetail implements Parcelable {
    private int id;
    private String userName;
    private Address address;
    private String contactNumber;
    private transient boolean isSelected;


    public DeliveryDetail(String userName, Address address, String contactNumber) {
        this.userName = userName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.isSelected = false;
    }

    public DeliveryDetail(int id, String userName, Address address, String contactNumber) {
        this(userName, address, contactNumber);
        this.id = id;
    }


    public DeliveryDetail(DeliveryDetail deliveryDetail){
        this(deliveryDetail.getId(),
                deliveryDetail.getUserName(),
                deliveryDetail.getAddress(),
                deliveryDetail.getContactNumber());
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    protected DeliveryDetail(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        address = (Address) in.readValue(Address.class.getClassLoader());
        contactNumber = in.readString();
        isSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userName);
        dest.writeValue(address);
        dest.writeString(contactNumber);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeliveryDetail> CREATOR = new Parcelable.Creator<DeliveryDetail>() {
        @Override
        public DeliveryDetail createFromParcel(Parcel in) {
            return new DeliveryDetail(in);
        }

        @Override
        public DeliveryDetail[] newArray(int size) {
            return new DeliveryDetail[size];
        }
    };
}
