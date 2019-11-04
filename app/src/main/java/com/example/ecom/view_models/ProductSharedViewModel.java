package com.example.ecom.view_models;

import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.ecom.model.CartProductDetail;
import com.example.ecom.model.Product;
import com.example.ecom.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class ProductSharedViewModel extends ViewModel implements Filterable {
    private List<Product>fullProductList;
    private MutableLiveData<List<Product>> productListLiveD = new MutableLiveData<>();
    private MutableLiveData<List<CartProductDetail>> cartProductListLiveD = new MutableLiveData<>();
    //private LiveData filteredListLiveD;
    private LiveData cartSummaryLiveD;
    private Repository mRepository = new Repository();

    public ProductSharedViewModel() {
        fullProductList = mRepository.getAllProducts();
        productListLiveD.setValue(fullProductList);
        cartProductListLiveD.setValue(mRepository.getCartDetails());
        cartSummaryLiveD = Transformations.map(cartProductListLiveD, cartProducts -> generateCartSummary(cartProducts));
        //filteredListLiveD = Transformations.map(productListLiveD, products -> createFilteredList(products));
    }

    private Double generateCartSummary(List<CartProductDetail> cartProducts) {
        double cartTotal = 0;
        for (CartProductDetail cartProduct: cartProducts) {
            Product product = cartProduct.getProduct();
            cartTotal+= product.getFinalPrice();
        }

        return cartTotal;
    }

    /*private List<Product> createFilteredList(List<Product> productList ){
        List<Product> filteredList = new ArrayList<>(productList);
        return filteredList;
    }*/

    public void getAllProducts(){
        mRepository.getAllProducts();
    }

    public LiveData<List<Product>> getProductLiveData(){
        return productListLiveD;
    }

    public LiveData<List<CartProductDetail>> getCartProductsLiveData(){
        return cartProductListLiveD;
    }

    public LiveData<Double> getCartSummaryLiveData(){
        return cartSummaryLiveD;
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

            if(constraint == null || constraint.toString().trim().isEmpty())
                filteredList = fullProductList;
            else{
                filteredList = new ArrayList<>();
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Product p: fullProductList) {
                    if(p.getTitle().toLowerCase().contains(filterPattern))
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
            } catch (ClassCastException e){
                e.printStackTrace();
            }
        }
    };
}
