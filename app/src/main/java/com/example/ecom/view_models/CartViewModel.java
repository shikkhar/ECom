package com.example.ecom.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.model.CartSummary;
import com.example.ecom.model.Product;
import com.example.ecom.repository.Repository;
import com.example.ecom.utils.Event;
import com.example.ecom.utils.UpdateOperationResult;
import com.example.ecom.utils.VolleySeverRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {
    private static final String TAG = "CartViewModel";
    private MutableLiveData<List<CartProductDetail>> cartProductListLiveD = new MutableLiveData<>();
    private LiveData cartSummaryLiveD;
    private MutableLiveData<Event<Boolean>> addToCartResultLiveD = new MutableLiveData<>();
    private MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = new MutableLiveData<>();
    private Repository mRepository = new Repository();

    public CartViewModel() {
        if (cartProductListLiveD.getValue() == null)
            mRepository.getCartDetails(new FetchCartProductsCallback(cartProductListLiveD));

        cartSummaryLiveD = Transformations.map(cartProductListLiveD, cartProducts -> generateCartSummary(cartProducts));
        //filteredListLiveD = Transformations.map(productListLiveD, products -> createFilteredList(products));
    }

    private CartSummary generateCartSummary(List<CartProductDetail> cartProducts) {
        double cartTotal = 0;
        int totalQty = 0;
        double savings = 0;
        for (CartProductDetail cartProduct : cartProducts) {
            Product product = cartProduct.getProduct();
            cartTotal += product.getFinalPrice();
            totalQty += cartProduct.getQuantity();
            savings += ((product.getOriginalPrice() - product.getFinalPrice()) * cartProduct.getQuantity());
        }

        return new CartSummary(cartTotal, totalQty, (int) savings);
    }

    public void addToCart(Product product, int qty) {
        CartProductDetail cartProductDetail = new CartProductDetail(product, qty);
        mRepository.addToCart(new AddToCartCallback(cartProductListLiveD, addToCartResultLiveD, cartProductDetail), cartProductDetail);
    }

    public void removeFromCart(int position) {
        List<CartProductDetail> cartProductDetails = cartProductListLiveD.getValue();
        if (cartProductDetails != null) {
            Product product = cartProductDetails.get(position).getProduct();
            mRepository.removeFromCart(new RemoveFromCart(cartProductListLiveD, position), product);
        }
    }

    public void updateCart(int position, int qty) {
        List<CartProductDetail> cartProductDetails = cartProductListLiveD.getValue();
        if (cartProductDetails != null) {
            Product product = cartProductDetails.get(position).getProduct();
            mRepository.updateCart(new UpdateCartCallback(cartProductListLiveD, position, qty), product.getId(), qty);
        }
        //mRepository.updateCart(new UpdateCartCallback());
    }

    public void updateFavoriteStatus(Product product) {
        mRepository.updateFavoriteStatus(new CartViewModel.UpdateFavoriteCallback(isUpdateSuccessful, product), product.getId(), product.isFavorite());
    }


    public LiveData<List<CartProductDetail>> getCartProductsLiveData() {
        return cartProductListLiveD;
    }

    public LiveData<Event<Boolean>> getAddToCartResult() {
        return addToCartResultLiveD;
    }

    /*public void setAddToCartResult(boolean value){
        addToCartResultLiveD.setValue(value);
    }*/

    public LiveData<CartSummary> getCartSummaryLiveData() {
        return cartSummaryLiveD;
    }

    public LiveData<Event<UpdateOperationResult>> getUpdateOperationStatus() {
        return isUpdateSuccessful;
    }


    private static class FetchCartProductsCallback implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<CartProductDetail>>> weakCartProductListLiveD;

        public FetchCartProductsCallback(MutableLiveData<List<CartProductDetail>> cartProductListLiveD) {
            this.weakCartProductListLiveD = new WeakReference<>(cartProductListLiveD);
        }

        @Override
        public void onSuccess(JSONObject response) throws JSONException {

            List<CartProductDetail> cartProductDetailList = new ArrayList<>();
            //List<CartProductDetail> temp = new ArrayList<>();
            JSONArray jsonElements = response.getJSONArray("data");

            if (jsonElements != null) {
                Gson gson = new Gson();

                for (int i = 0; i < jsonElements.length(); i++) {
                    CartProductDetail cartProduct = gson.fromJson(jsonElements.get(i).toString(), CartProductDetail.class);
                    cartProductDetailList.add(cartProduct);
                }
            }

            MutableLiveData<List<CartProductDetail>> cartProductListLiveD = weakCartProductListLiveD.get();
            if (cartProductListLiveD != null) {
                //cartProductListLiveD.setValue(temp);
                cartProductListLiveD.setValue(cartProductDetailList);
            }
        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail:" + error.getMessage());
        }
    }

    private static class AddToCartCallback implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<CartProductDetail>>> weakCartProductListLiveD;
        private WeakReference<MutableLiveData<Event<Boolean>>> weakAddToCartResultLiveD;
        private CartProductDetail cartProductDetail;

        public AddToCartCallback(MutableLiveData<List<CartProductDetail>> cartProductListLiveD,
                                 MutableLiveData<Event<Boolean>> addToCartResultLiveD,
                                 CartProductDetail cartProductDetail) {
            this.weakCartProductListLiveD = new WeakReference<>(cartProductListLiveD);
            this.weakAddToCartResultLiveD = new WeakReference<>(addToCartResultLiveD);
            this.cartProductDetail = cartProductDetail;
        }

        @Override
        public void onSuccess(JSONObject response) throws JSONException {

           /* String[] imagePaths = new String[]{"https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
                    "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"};

            Product product = new Product("Product 10","Short Description",
                    "Long Description",
                    Arrays.asList(imagePaths),"",
                    100,75, 25);


            CartProductDetail cartProductDetail = new CartProductDetail(product, 10);*/


            MutableLiveData<List<CartProductDetail>> cartProductListLiveD = weakCartProductListLiveD.get();
            if (cartProductListLiveD != null) {
                //cartProductListLiveD.setValue(temp);
                boolean productAlreadyExists = false;
                List<CartProductDetail> cartProductDetailList = cartProductListLiveD.getValue();
                if (cartProductDetailList != null) {
                    for (int i = 0; i < cartProductDetailList.size(); i++) {
                        Product existingProduct = cartProductDetailList.get(i).getProduct();
                        if (existingProduct.getId() == cartProductDetail.getProduct().getId()) {
                            productAlreadyExists = true;
                            int oldQty = cartProductDetailList.get(i).getQuantity();
                            int newQty = cartProductDetail.getQuantity();
                            cartProductDetailList.get(i).setQuantity(oldQty + newQty);
                            break;
                        }
                    }
                }

                if (!productAlreadyExists)
                    cartProductDetailList.add(cartProductDetail);
                cartProductListLiveD.setValue(cartProductDetailList);
            }

            MutableLiveData<Event<Boolean>> addToCartResultLiveD = weakAddToCartResultLiveD.get();
            if (addToCartResultLiveD != null) {
                //cartProductListLiveD.setValue(temp);
                addToCartResultLiveD.setValue(new Event<>(true));
            }
        }

        @Override
        public void onFail(VolleyError error) {
            MutableLiveData<Event<Boolean>> addToCartResultLiveD = weakAddToCartResultLiveD.get();
            if (addToCartResultLiveD != null) {
                //cartProductListLiveD.setValue(temp);
                addToCartResultLiveD.setValue(new Event<>(false));
            }
            Log.d(TAG, "onFail:" + error.getMessage());
        }
    }

    private static class UpdateCartCallback implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<CartProductDetail>>> weakCartProductListLiveD;
        private int position;
        private int quantity;

        public UpdateCartCallback(MutableLiveData<List<CartProductDetail>> cartProductListLiveD,
                                  int position,
                                  int quantity) {
            this.weakCartProductListLiveD = new WeakReference<>(cartProductListLiveD);
            this.position = position;
            this.quantity = quantity;
        }

        @Override
        public void onSuccess(JSONObject response) throws JSONException {
            //TODO: fire an event to tell that the operation was successful and then reload the cart items.
            MutableLiveData<List<CartProductDetail>> cartProductListLiveD = weakCartProductListLiveD.get();
            if (cartProductListLiveD != null) {
                //cartProductListLiveD.setValue(temp);
                List<CartProductDetail> cartProductDetailList = cartProductListLiveD.getValue();
                cartProductDetailList.get(position).setQuantity(quantity);
                cartProductListLiveD.setValue(cartProductDetailList);
            }
        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail:" + error.getMessage());
        }
    }

    private static class RemoveFromCart implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<CartProductDetail>>> weakCartProductListLiveD;
        private int position;

        public RemoveFromCart(MutableLiveData<List<CartProductDetail>> cartProductListLiveD,
                              int position) {
            this.weakCartProductListLiveD = new WeakReference<>(cartProductListLiveD);
            this.position = position;
        }

        @Override
        public void onSuccess(JSONObject response) throws JSONException {


            MutableLiveData<List<CartProductDetail>> cartProductListLiveD = weakCartProductListLiveD.get();
            if (cartProductListLiveD != null) {
                //cartProductListLiveD.setValue(temp);
                List<CartProductDetail> cartProductDetailList = cartProductListLiveD.getValue();

                cartProductDetailList.remove(position);
                cartProductListLiveD.setValue(cartProductDetailList);
            }
        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail:" + error.getMessage());
        }
    }

    private static class UpdateFavoriteCallback implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<Product>>> weakProductListLiveD;
        private WeakReference<MutableLiveData<Event<UpdateOperationResult>>> weakIsUpdateSuccessful;
        private Product product;

        public UpdateFavoriteCallback(MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful,
                                      Product product) {
            this.weakIsUpdateSuccessful = new WeakReference<>(isUpdateSuccessful);
            this.product = product;

        }

        @Override
        public void onSuccess(JSONObject response) throws JSONException {
            MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = weakIsUpdateSuccessful.get();

            if (isUpdateSuccessful != null) {
                isUpdateSuccessful.setValue(new Event<>(new UpdateOperationResult(true, product)));
            }

        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail: " + error.getMessage());
            MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = weakIsUpdateSuccessful.get();

            if (isUpdateSuccessful != null) {
                //product.setFavorite(!product.isFavorite());
                isUpdateSuccessful.setValue(new Event<>(new UpdateOperationResult(false, product)));
            }
        }
    }
}
