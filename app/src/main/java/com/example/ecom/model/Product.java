package com.example.ecom.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private long id;
    private String title;
    private String shortDescription;
    private String longDescription;
    private String imagePath;
    private double originalPrice;
    private double finalPrice;
    private int discount;

    public Product(String title, String shortDescription, String longDescription, String imagePath, double originalPrice, double finalPrice, int discount) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.imagePath = imagePath;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.discount = discount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    protected Product(Parcel in) {
        id = in.readLong();
        title = in.readString();
        shortDescription = in.readString();
        longDescription = in.readString();
        imagePath = in.readString();
        originalPrice = in.readDouble();
        finalPrice = in.readDouble();
        discount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(shortDescription);
        dest.writeString(longDescription);
        dest.writeString(imagePath);
        dest.writeDouble(originalPrice);
        dest.writeDouble(finalPrice);
        dest.writeInt(discount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
