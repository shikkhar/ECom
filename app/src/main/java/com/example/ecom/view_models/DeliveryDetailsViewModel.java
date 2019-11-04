package com.example.ecom.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecom.model.DeliveryDetail;
import com.example.ecom.repository.Repository;

import java.util.List;

public class DeliveryDetailsViewModel extends ViewModel {

    private MutableLiveData<List<DeliveryDetail>> deliveryDetailLiveD = new MutableLiveData<>();
    private Repository mRepository = new Repository();

    public DeliveryDetailsViewModel() {
        deliveryDetailLiveD.setValue(mRepository.getAllDeliveryDetails());
    }

    public LiveData<List<DeliveryDetail>> getDeliveryDetailLiveData(){
        return deliveryDetailLiveD;
    }
}
