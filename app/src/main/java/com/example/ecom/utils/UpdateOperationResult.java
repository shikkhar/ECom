package com.example.ecom.utils;

import com.example.ecom.model.Product;

public class UpdateOperationResult {
    private boolean isSuccessful;
    private Product product;

    public UpdateOperationResult(boolean isSuccessful, Product product) {
        this.isSuccessful = isSuccessful;
        this.product = product;
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

}
