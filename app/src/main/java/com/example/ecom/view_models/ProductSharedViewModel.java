package com.example.ecom.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecom.model.Product;
import com.example.ecom.repository.Repository;

import java.util.List;

public class ProductSharedViewModel extends ViewModel {
    private MutableLiveData<List<Product>> productListLiveData = new MutableLiveData<>();
    private Repository mRepository = new Repository();

    public ProductSharedViewModel() {
        productListLiveData.setValue(mRepository.getAllProducts());
    }

    public void getAllProducts(){
        mRepository.getAllProducts();
    }
    public LiveData<List<Product>> getProductLiveData(){
        return productListLiveData;
    }
}
