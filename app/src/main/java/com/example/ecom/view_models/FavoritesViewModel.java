package com.example.ecom.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
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

/**
 * View Model class tied to the lifecycle of Favorites Fragment
 */
public class FavoritesViewModel extends ViewModel {
    private static final String TAG = "FavoritesViewModel";
    //live data to expose favorite products to the observer
    private MutableLiveData<List<Product>> productMutableLiveData = new MutableLiveData<>();
    //repository object to make api requests
    private Repository repository;

    public void setRepository(Repository repository){
        this.repository = repository;
    }

    //method to send api request to fetch all favorites
    public void getFavorites(){
        repository.getFavorites(new FetchProductListCallback(productMutableLiveData));
    }

    //exposes the live data to the observer
    public LiveData<List<Product>> getFavoritesLiveD(){
        return productMutableLiveData;
    }

    /**
     * Callback class for fetch favorites request
     */
    private static class FetchProductListCallback implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<Product>>> weakProductListLiveD;

        FetchProductListCallback(MutableLiveData<List<Product>> productListLiveD) {
            this.weakProductListLiveD = new WeakReference<>(productListLiveD);
        }

        @Override
        public void onSuccess(JSONObject response) throws JSONException {
            List<Product> favoritesProductList = new ArrayList<>();
            JSONArray jsonElements = response.getJSONArray("data");

            if (jsonElements != null) {
                Gson gson = new Gson();

                for (int i = 0; i < jsonElements.length(); i++) {
                    Product product = gson.fromJson(jsonElements.get(i).toString(), Product.class);
                    favoritesProductList.add(product);
                }
            }

            MutableLiveData<List<Product>> productListLiveD = weakProductListLiveD.get();
            if (productListLiveD != null) {
                productListLiveD.setValue(favoritesProductList);
            }
        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail: " + error.getMessage());
        }
    }

}
