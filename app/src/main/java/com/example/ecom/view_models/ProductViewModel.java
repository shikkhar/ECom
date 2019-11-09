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

public class ProductViewModel extends ViewModel implements Filterable {
    private static final String TAG = "ProductViewModel";
    private List<Product> fullProductList = new ArrayList<>();
    private MutableLiveData<List<Product>> productListLiveD = new MutableLiveData<>();
    private MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = new MutableLiveData<>();
    private Repository mRepository = new Repository();

    public ProductViewModel() {
        if (productListLiveD.getValue() == null)
            mRepository.getAllProducts(new FetchProductListCallback(productListLiveD, fullProductList));
        //productListLiveD.setValue(fullProductList);
        //filteredListLiveD = Transformations.map(productListLiveD, products -> createFilteredList(products));
    }


    /*private List<Product> createFilteredList(List<Product> productList ){
        List<Product> filteredList = new ArrayList<>(productList);
        return filteredList;
    }*/

    //used to fetch all the products from the database
    public void getAllProducts() {
        mRepository.getAllProducts(new FetchProductListCallback(productListLiveD, fullProductList));
    }

    public void updateFavoriteStatus(int position, Product product) {
        mRepository.updateFavoriteStatus(new UpdateFavoriteCallback(productListLiveD, isUpdateSuccessful, product, position, fullProductList), product.getId(), !product.isFavorite());
    }

    public LiveData<List<Product>> getProductLiveData() {
        return productListLiveD;
    }
    public LiveData<Event<UpdateOperationResult>> getUpdateOperationStatus() {
        return isUpdateSuccessful;
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

    private static class UpdateFavoriteCallback implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<Product>>> weakProductListLiveD;
        private WeakReference<MutableLiveData<Event<UpdateOperationResult>>> weakIsUpdateSuccessful;
        private int position;
        private Product product;
        private List<Product> fullProductList;

        public UpdateFavoriteCallback(MutableLiveData<List<Product>> productListLiveD,
                                      MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful,
                                     Product product,
                                     int position,
                                      List<Product> fullProductList) {
            this.weakProductListLiveD = new WeakReference<>(productListLiveD);
            this.weakIsUpdateSuccessful = new WeakReference<>(isUpdateSuccessful);
            this.product = product;
            this.position = position;
            this.fullProductList = fullProductList;
           /* this.position = position;
            this.product = product;
            this.fullProductList = fullProductList;*/

        }

        @Override
        public void onSuccess(JSONObject response) throws JSONException {
            //TODO: issue a Toast Message that update was successful
            MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = weakIsUpdateSuccessful.get();

            if(isUpdateSuccessful != null){
                isUpdateSuccessful.setValue(new Event<>(new UpdateOperationResult(true, product, position)));
            }

            /*MutableLiveData<List<Product>> productListLiveD = weakProductListLiveD.get();
            if (productListLiveD != null) {
                List<Product> currentList = productListLiveD.getValue();
                if (currentList != null){
                    if( product.getId() == currentList.get(position).getId()){
                        currentList = new ArrayList<>(fullProductList);
                    }
                }
                    productListLiveD.setValue(currentList);
            }*/
        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail: " + error.getMessage());
            MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = weakIsUpdateSuccessful.get();

            if(isUpdateSuccessful != null){
                //product.setFavorite(!product.isFavorite());
                isUpdateSuccessful.setValue(new Event<>(new UpdateOperationResult(false, product, position)));
            }

            /*MutableLiveData<List<Product>> productListLiveD = weakProductListLiveD.get();
            if (productListLiveD != null) {
                List<Product> currentList = productListLiveD.getValue();
                if (currentList != null){
                    if( product.getId() == currentList.get(position).getId()){
                        currentList = new ArrayList<>(fullProductList);
                    }
                }
                productListLiveD.setValue(currentList);
            }*/
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
