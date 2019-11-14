package com.example.ecom.repository;

import androidx.lifecycle.ViewModelProviders;

import com.example.ecom.model.Address;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.model.DeliveryDetail;
import com.example.ecom.model.Product;
import com.example.ecom.utils.VolleySeverRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.ecom.utils.CONSTANTS.*;

public class Repository {
    public void getAllProducts(VolleySeverRequest.VolleyResponseCallback responseCallback) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.FETCH_PRODUCT_LIST;
        serverRequest.makeGetRequest(url, NetworkRequestTags.FETCH_PRODUCT_LIST);

        //dummy objects
        /*List<Product> productList = new ArrayList<>();

        for (int i = 1; i < 25; i++) {
            String[] imagePaths = new String[]{
                    "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"};

            productList.add(new Product((Long.valueOf(i)), "Product " + i, "Short Description",
                    "Long Description",
                    Arrays.asList(imagePaths), imagePaths[0],
                    100, 75, 25, false));
        }

        String json = new Gson().toJson(productList);*/
        //return productList;
    }

    public void getFavorites(VolleySeverRequest.VolleyResponseCallback responseCallback) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.FETCH_FAVORITES;
        serverRequest.makeGetRequest(url, NetworkRequestTags.FETCH_FAVORITES);
    }

    public void getAllDeliveryDetails(VolleySeverRequest.VolleyResponseCallback responseCallback) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.FETCH_DELIVERY_DETAIL_LIST;
        serverRequest.makeGetRequest(url, NetworkRequestTags.FETCH_DELIVERY_DETAIL_LIST);


        List<DeliveryDetail> deliveryDetailList = new ArrayList<>();

        /*for (int i = 0; i <2 ; i++) {
            Address address = new Address("B521A", "Sushant Lok", "Gurugram", "Haryana", "122002", "Near vyapar Kendra");
            DeliveryDetail deliveryDetail = new DeliveryDetail(i, "Shikhar Pahwa " + i, address, "9540555666");
            deliveryDetailList.add(deliveryDetail);
        }

        String json = new Gson().toJson(deliveryDetailList);*/
        //return deliveryDetailList;
    }

    public void updateFavoriteStatus(VolleySeverRequest.VolleyResponseCallback responseCallback,
                                     long productId,
                                     boolean isFavorite){
        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.UPDATE_FAVORITE_STATUS;
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", null);
        params.put("productId", String.valueOf(productId));
        params.put("isFavorite", String.valueOf(isFavorite));
        serverRequest.makePostRequest(url, params, NetworkRequestTags.UPDATE_FAVORITE_STATUS);

    }

    public void getCartDetails(VolleySeverRequest.VolleyResponseCallback responseCallback) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.FETCH_CART_PRODUCTS;
        serverRequest.makeGetRequest(url, NetworkRequestTags.FETCH_CART_PRODUCTS);

        /*List<CartProductDetail> cartProductDetailList = new ArrayList<>();
        for (int i = 1; i < 7 ; i++) {

            String[] imagePaths = new String[]{"https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"};

            Product product = new Product(i, "Product " + i, "Brand", "Long Description", Arrays.asList(imagePaths), imagePaths[0],
                    100,75,25, false);

            CartProductDetail cartProductDetail = new CartProductDetail(product, i);


            cartProductDetailList.add(cartProductDetail);
        }

        String json = new Gson().toJson(cartProductDetailList);*/

        //return cartProductDetailList;
    }

    public void addToCart(VolleySeverRequest.VolleyResponseCallback responseCallback, CartProductDetail cartProductDetail) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.ADD_TO_CART;
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", null);
        params.put("productId", String.valueOf(cartProductDetail.getProduct().getId()));
        params.put("qty", String.valueOf(cartProductDetail.getQuantity()));
        serverRequest.makePostRequest(url, params, NetworkRequestTags.ADD_TO_CART);

    }

    public void updateCart(VolleySeverRequest.VolleyResponseCallback responseCallback, long productId, int quantity) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.UPDATE_CART;
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", null);
        params.put("productId", String.valueOf(productId));
        params.put("qty", String.valueOf(quantity));
        serverRequest.makePostRequest(url, params, NetworkRequestTags.UPDATE_CART);

    }

    public void removeFromCart(VolleySeverRequest.VolleyResponseCallback responseCallback, long productId) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.ADD_TO_CART;
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", null);
        params.put("productId", String.valueOf(productId));
        serverRequest.makePostRequest(url, params, NetworkRequestTags.REMOVE_FROM_CART);

    }

    public void addDeliveryDetail(VolleySeverRequest.VolleyResponseCallback responseCallback,DeliveryDetail deliveryDetail) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.ADD_DELIVERY_DETAIL;
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", null);
        params.put("deliveryDetail", null);
        serverRequest.makePostRequest(url, params, NetworkRequestTags.ADD_DELIVERY_DETAIL);
    }

    public void updateDeliveryDetail(VolleySeverRequest.VolleyResponseCallback responseCallback,DeliveryDetail newValues) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.UPDATE_DELIVERY_DETAIL;
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", null);
        /*params.put("detailId", String.valueOf(detailId));
        params.put("detailId", String.valueOf(detailId));*/
        serverRequest.makePostRequest(url, params, NetworkRequestTags.UPDATE_DELIVERY_DETAIL);
    }

    public void deleteDeliveryDetail(VolleySeverRequest.VolleyResponseCallback responseCallback,int detailId) {

        VolleySeverRequest serverRequest = new VolleySeverRequest(responseCallback);
        String url = NetworkRequestUrls.DELETE_DELIVERY_DETAIL;
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", null);
        params.put("detailId", String.valueOf(detailId));
        serverRequest.makePostRequest(url, params, NetworkRequestTags.DELETE_DELIVERY_DETAIL);
    }
}
