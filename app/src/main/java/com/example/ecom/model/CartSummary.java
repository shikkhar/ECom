package com.example.ecom.model;

public class CartSummary {
    private double cartTotal;
    private int savings;
    private int itemCount;

    public CartSummary(double cartTotal, int itemCount, int savings) {
        this.cartTotal = cartTotal;
        this.itemCount = itemCount;
        this.savings = savings;
    }

    public double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(double cartTotal) {
        this.cartTotal = cartTotal;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getSavings() {
        return savings;
    }

    public void setSavings(int savings) {
        this.savings = savings;
    }
}
