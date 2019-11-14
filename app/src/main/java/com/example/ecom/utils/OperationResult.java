package com.example.ecom.utils;

import com.example.ecom.model.Product;

public class OperationResult {
    private boolean isSuccessful;
    private String error;

    public OperationResult(boolean isSuccessful, String error) {
        this.isSuccessful = isSuccessful;
        this.error = error;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
