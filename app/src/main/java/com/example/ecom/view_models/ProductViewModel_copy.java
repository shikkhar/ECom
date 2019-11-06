package com.example.ecom.view_models;

import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.model.Product;
import com.example.ecom.repository.Repository;
import com.example.ecom.utils.VolleySeverRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ProductViewModel_copy extends ViewModel implements Filterable {
    private static final String TAG = "ProductViewModel";
    private List<Product> fullProductList = new ArrayList<>();
    private MutableLiveData<List<Product>> productListLiveD = new MutableLiveData<>();
    private MutableLiveData<List<CartProductDetail>> cartProductListLiveD = new MutableLiveData<>();
    //private LiveData filteredListLiveD;
    private LiveData cartSummaryLiveD;
    private Repository mRepository = new Repository();

    public ProductViewModel_copy() {
        if (productListLiveD.getValue() == null)
            mRepository.getAllProducts(new FetchProductListCallback(productListLiveD, fullProductList));
        //productListLiveD.setValue(fullProductList);
        if (cartProductListLiveD.getValue() == null)
            mRepository.getCartDetails(new FetchCartProductsCallback(cartProductListLiveD));

        cartSummaryLiveD = Transformations.map(cartProductListLiveD, cartProducts -> generateCartSummary(cartProducts));
        //filteredListLiveD = Transformations.map(productListLiveD, products -> createFilteredList(products));
    }

    private Double generateCartSummary(List<CartProductDetail> cartProducts) {
        double cartTotal = 0;
        for (CartProductDetail cartProduct : cartProducts) {
            Product product = cartProduct.getProduct();
            cartTotal += product.getFinalPrice();
        }

        return cartTotal;
    }

    /*private List<Product> createFilteredList(List<Product> productList ){
        List<Product> filteredList = new ArrayList<>(productList);
        return filteredList;
    }*/

    //used to fetch all the products from the database
    public void getAllProducts() {
        mRepository.getAllProducts(new FetchProductListCallback(productListLiveD, fullProductList));
    }

    public LiveData<List<Product>> getProductLiveData() {
        return productListLiveD;
    }

    public LiveData<List<CartProductDetail>> getCartProductsLiveData() {
        return cartProductListLiveD;
    }

    public LiveData<Double> getCartSummaryLiveData() {
        return cartSummaryLiveD;
    }


    private static class FetchProductListCallback implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<Product>>> weakProductListLiveD;
        private List<Product> fullProductList;

        public FetchProductListCallback(MutableLiveData<List<Product>> productListLiveD, List<Product> fullProductList) {
            this.weakProductListLiveD = new WeakReference<>(productListLiveD);
            this.fullProductList = fullProductList;

        }

        @Override
        public void onSuccess(JSONObject response) throws JSONException {
            JSONArray jsonElements = response.getJSONArray("data");

            if (jsonElements != null) {
                Gson gson = new Gson();

                for (int i = 0; i < jsonElements.length(); i++) {
                    Product product = gson.fromJson(jsonElements.get(i).toString(), Product.class);
                    fullProductList.add(product);
                }
            }

            MutableLiveData<List<Product>> productListLiveD = weakProductListLiveD.get();
            if (productListLiveD != null) {
                productListLiveD.setValue(fullProductList);
            }
        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail: " + error.getMessage());
        }
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
            Log.d(TAG, "onFail:" +error.getMessage());
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    //TODO: implement feature to search with tags
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //List<Product> fullList = productListLiveD.getValue();
            List<Product> filteredList;

            if (constraint == null || constraint.toString().trim().isEmpty())
                filteredList = fullProductList;
            else {
                filteredList = new ArrayList<>();
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Product p : fullProductList) {
                    if (p.getTitle().toLowerCase().contains(filterPattern))
                        filteredList.add(p);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                productListLiveD.setValue((List) results.values);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    };
}
