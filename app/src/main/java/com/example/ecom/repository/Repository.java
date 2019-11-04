package com.example.ecom.repository;

import com.example.ecom.model.Address;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.model.DeliveryDetail;
import com.example.ecom.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    public List<Product> getAllProducts(){
        List<Product> productList = new ArrayList<>();

        for (int i = 1; i < 25 ; i++) {
            productList.add(new Product("Product " + i,"Short Description",
                    "Long Description",
                    "https://banner2.cleanpng.com/20180514/eiw/kisspng-surgical-mask-activated-carbon-air-pollution-dust-5af9ef971a0dd1.9567113515263292391067.jpg",
                    100,75, 25));
        }


        return productList;
    }

    public List<DeliveryDetail> getAllDeliveryDetails(){

        List<DeliveryDetail> deliveryDetailList = new ArrayList<>();

        for (int i = 0; i <5 ; i++) {
            Address address = new Address("B521A", "Sushant Lok", "Gurugram", "Haryana", "");
            DeliveryDetail deliveryDetail = new DeliveryDetail("Shikhar Pahwa " + i, address);
            deliveryDetailList.add(deliveryDetail);
        }

        return deliveryDetailList;
    }

    public List<CartProductDetail> getCartDetails(){
        List<CartProductDetail> cartProductDetailList = new ArrayList<>();
        for (int i = 1; i < 3 ; i++) {

            Product product = new Product("Product " + i,"Short Description",
                    "Long Description",
                    "https://banner2.cleanpng.com/20180514/eiw/kisspng-surgical-mask-activated-carbon-air-pollution-dust-5af9ef971a0dd1.9567113515263292391067.jpg",
                    100,75, 25);

            CartProductDetail cartProductDetail = new CartProductDetail(product, i);
            cartProductDetailList.add(cartProductDetail);
        }

        return cartProductDetailList;
    }
}
