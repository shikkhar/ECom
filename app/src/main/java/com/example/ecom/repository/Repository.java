package com.example.ecom.repository;

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
}
