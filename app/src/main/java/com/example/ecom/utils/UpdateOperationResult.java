package com.example.ecom.utils;

import com.example.ecom.model.Product;

public class UpdateOperationResult {
    private boolean isSuccessful;
    private Product product;
    private int position;

    public UpdateOperationResult(boolean isSuccessful, Product product, int position) {
        this.isSuccessful = isSuccessful;
        this.product = product;
        this.position =  position;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
