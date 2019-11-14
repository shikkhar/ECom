package com.example.ecom.view_models;

import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.ecom.model.Product;
import com.example.ecom.repository.Repository;
import com.example.ecom.utils.Event;
import com.example.ecom.utils.OperationResult;
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
 * View Model class to expose the product list to users.
 */
public class ProductViewModel extends ViewModel implements Filterable {
    private static final String TAG = "ProductViewModel";
    //list to store the result that is fetched originally
    //used to repopulate the live data when a filtered list has to be reset
    private List<Product> fullProductList = new ArrayList<>();
    //live data to expose the product list
    private MutableLiveData<List<Product>> productListLiveD = new MutableLiveData<>();
    //single event live data to notify the success/failure of the update product operation.
    //right now th favorite status of the product is being updated
    private MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = new MutableLiveData<>();
    //single event live data to notify observers about the success/failure of an operation
    private MutableLiveData<Event<OperationResult>> operationResultLiveD = new MutableLiveData<>();
    //repository object to send api requests
    private Repository repository;// = new Repository();

    public ProductViewModel() {
        /*if (productListLiveD.getValue() == null)
            repository.getAllProducts(new FetchProductListCallback(productListLiveD, fullProductList));*/

    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    //used to fetch all the products from the database
    public void getAllProducts() {
        repository.getAllProducts(new FetchProductListCallback(productListLiveD, operationResultLiveD, fullProductList));
    }

    //request to update the favorite status of the given product
    public void updateFavoriteStatus(Product product) {
        repository.updateFavoriteStatus(new UpdateFavoriteCallback(productListLiveD, isUpdateSuccessful, product, fullProductList), product.getId(), product.isFavorite());
    }

    //function to expose product live data to the observer
    public LiveData<List<Product>> getProductLiveData() {
        return productListLiveD;
    }

    //exposes the result of update operation to the observer
    public LiveData<Event<UpdateOperationResult>> getUpdateOperationStatus() {
        return isUpdateSuccessful;
    }

    //exposes the result of update operation to the observer
    public LiveData<Event<OperationResult>> getOperationResult() {
        return operationResultLiveD;
    }

    /**
     * Callback class for the fetch product list request
     */
    private static class FetchProductListCallback implements VolleySeverRequest.VolleyResponseCallback {
        // weak reference to the product live data
        private WeakReference<MutableLiveData<List<Product>>> weakProductListLiveD;
        private WeakReference<MutableLiveData<Event<OperationResult>>> weakOperationResultLiveD;
        //we store the original result of this fetch in a list to repopulate the live data when the list is filtered
        private List<Product> fullProductList;

        public FetchProductListCallback(MutableLiveData<List<Product>> productListLiveD,
                                        MutableLiveData<Event<OperationResult>> operationResultLiveD,
                                        List<Product> fullProductList) {
            this.weakProductListLiveD = new WeakReference<>(productListLiveD);
            this.weakOperationResultLiveD = new WeakReference<>(operationResultLiveD);
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

            setOperationResult(true, null);
        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail: " + error.getMessage());
            setOperationResult(false, error.getMessage());
        }

        private void setOperationResult(boolean success, String error){
            MutableLiveData<Event<OperationResult>> operationResultLiveD = weakOperationResultLiveD.get();
            if (operationResultLiveD != null)
                operationResultLiveD.setValue(new Event<>(new OperationResult(success, error)));
        }
    }

    /**
     * Callback class for update operation on the given product
     */
    private static class UpdateFavoriteCallback implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<Product>>> weakProductListLiveD;
        private WeakReference<MutableLiveData<Event<UpdateOperationResult>>> weakIsUpdateSuccessful;
        private Product product;
        private List<Product> fullProductList;

        public UpdateFavoriteCallback(MutableLiveData<List<Product>> productListLiveD,
                                      MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful,
                                      Product product,
                                      List<Product> fullProductList) {
            this.weakProductListLiveD = new WeakReference<>(productListLiveD);
            this.weakIsUpdateSuccessful = new WeakReference<>(isUpdateSuccessful);
            this.product = product;
            this.fullProductList = fullProductList;
        }

        @Override
        public void onSuccess(JSONObject response) {
            MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = weakIsUpdateSuccessful.get();

            // set the event with false to notify that the operation has succeeded
            if (isUpdateSuccessful != null) {
                isUpdateSuccessful.setValue(new Event<>(new UpdateOperationResult(true, product)));
            }
        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail: " + error.getMessage());
            MutableLiveData<Event<UpdateOperationResult>> isUpdateSuccessful = weakIsUpdateSuccessful.get();

            // set the event with false to notify that the operation has failed
            if (isUpdateSuccessful != null) {
                isUpdateSuccessful.setValue(new Event<>(new UpdateOperationResult(false, product)));
            }

            //we create a new product because if we just change the values of the original product,
            //those changes are also reflected in the list used in the recycler view adapter... so the
            //recycler view is not updated
            //one possible issue can arise when the request takes some time to fail and within that time the
            //user navigates to the product detail screen. In that case old instance of the product will be passed before
            // the list has been updated with this new product instance
            Product productToBeInserted = new Product(product);
            //since the operation has failed revert the changes made to the favorite field of that product
            for (int i = 0; i < fullProductList.size(); i++) {
                if (productToBeInserted.getId() == fullProductList.get(i).getId()) {
                    boolean oldFavValue = productToBeInserted.isFavorite();
                    productToBeInserted.setFavorite(!oldFavValue);
                    fullProductList.set(i, productToBeInserted);
                    break;
                }
            }

            //set the live data with the list having product whose favorite status has been reverted back to its original value
            //this list will be submitted to the recycler view to update the view
            MutableLiveData<List<Product>> productListLiveD = weakProductListLiveD.get();
            if (productListLiveD != null) {
                productListLiveD.setValue(fullProductList);
            }
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
            // full list will be filtered and those values will be stored here
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

        // publishes the results obtained on the main thread
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
