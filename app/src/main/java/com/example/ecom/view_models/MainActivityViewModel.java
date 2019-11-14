package com.example.ecom.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<String> actionBarLiveD = new MutableLiveData<>();

    public void setActionBarTitle(String title){
        actionBarLiveD.setValue(title);
    }

    public LiveData<String> getActionBarLiveD(){
        return actionBarLiveD;
    }
}
