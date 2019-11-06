package com.example.ecom.model;

public class CartSummary {
    private double cartTotal;
    private int itemCount;

    public CartSummary(double cartTotal, int itemCount) {
        this.cartTotal = cartTotal;
        this.itemCount = itemCount;
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
}
