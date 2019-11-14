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

/**
 * View Model class which makes requests to fetch the cart product and
 * creates a cart summary based on that result
 */
public class CartViewModel extends ViewModel {
    private static final String TAG = "CartViewModel";
    //Live data object that holds all the object present in a user's cart
    private MutableLiveData<List<CartProductDetail>> cartProductListLiveD = new MutableLiveData<>();
    //Transformation is applied on the cart live data and the cart summary is stored in this live data
    private LiveData cartSummaryLiveD;
    //used to issue a single event to notify whether the add to cart operation was successful or not
    private MutableLiveData<Event<Boolean>> addToCartResultLiveD = new MutableLiveData<>();
    //used to issue a single event to notify whether the update cart operation was successful or not
    private MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = new MutableLiveData<>();
    //repository object to make api requests
    private Repository repository = new Repository();

    public CartViewModel() {
        if (cartProductListLiveD.getValue() == null)
            repository.getCartDetails(new FetchCartProductsCallback(cartProductListLiveD));

        cartSummaryLiveD = Transformations.map(cartProductListLiveD, cartProducts -> generateCartSummary(cartProducts));
    }

    public void getAllCartDetails(){
        repository.getCartDetails(new FetchCartProductsCallback(cartProductListLiveD));
    }

    //This method is called as part of the Transformation applied to the cart live data
    private CartSummary generateCartSummary(List<CartProductDetail> cartProducts) {
        double cartTotal = 0;
        int totalQty = 0;
        double savings = 0;
        for (CartProductDetail cartProduct : cartProducts) {
            Product product = cartProduct.getProduct();
            cartTotal += (product.getFinalPrice() * cartProduct.getQuantity());
            totalQty += cartProduct.getQuantity();
            savings += ((product.getOriginalPrice() - product.getFinalPrice()) * cartProduct.getQuantity());
        }
        return new CartSummary(cartTotal, totalQty, (int) savings);
    }

    //makes an api request to add a product to the cart
    public void addToCart(Product product, int qty) {
        CartProductDetail cartProductDetail = new CartProductDetail(product, qty);
        repository.addToCart(new AddToCartCallback(cartProductListLiveD, addToCartResultLiveD, cartProductDetail), cartProductDetail);
    }

    //makes an api request to remove a product from the cart based on its position in the list
    public void removeFromCart(int position) {
        List<CartProductDetail> cartProductDetails = cartProductListLiveD.getValue();
        if (cartProductDetails != null) {
            Product product = cartProductDetails.get(position).getProduct();
            repository.removeFromCart(new RemoveFromCart(cartProductListLiveD, position), product.getId());
        }
    }

    //makes an api request to update a product from the cart based on its position in the list
    public void updateCart(int position, int qty) {
        List<CartProductDetail> cartProductDetails = cartProductListLiveD.getValue();
        if (cartProductDetails != null) {
            Product product = cartProductDetails.get(position).getProduct();
            repository.updateCart(new UpdateCartCallback(cartProductListLiveD, position, qty), product.getId(), qty);
        }
    }

    public void updateFavoriteStatus(Product product) {
        repository.updateFavoriteStatus(new CartViewModel.UpdateFavoriteCallback(isUpdateSuccessful, product), product.getId(), product.isFavorite());
    }


    public LiveData<List<CartProductDetail>> getCartProductsLiveData() {
        return cartProductListLiveD;
    }

    public LiveData<Event<Boolean>> getAddToCartResult() {
        return addToCartResultLiveD;
    }

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

            MutableLiveData<List<CartProductDetail>> cartProductListLiveD = weakCartProductListLiveD.get();
            if (cartProductListLiveD != null) {
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
        private WeakReference<MutableLiveData<Event<UpdateOperationResult>>> weakIsUpdateSuccessful;
        private Product product;

        public UpdateFavoriteCallback(MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful,
                                      Product product) {
            this.weakIsUpdateSuccessful = new WeakReference<>(isUpdateSuccessful);
            this.product = product;

        }

        @Override
        public void onSuccess(JSONObject response){
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
                isUpdateSuccessful.setValue(new Event<>(new UpdateOperationResult(false, product)));
            }
        }
    }
}
