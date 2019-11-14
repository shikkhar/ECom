package com.example.ecom.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.ecom.model.DeliveryDetail;
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
 * View Model class attached to the lifecycle of Delivery Details List Fragment
 * Fetches a list of all the saved addresses and exposes them through a live data
 */
public class DeliveryDetailsViewModel extends ViewModel {
    private static final String TAG = "DeliveryDetailsViewMode";

    private MutableLiveData<List<DeliveryDetail>> deliveryDetailLiveD = new MutableLiveData<>();
    private Repository repository;

    public void setRepository(Repository repository){
        this.repository = repository;
    }

    public void getAllDeliveryDetails(){
        repository.getAllDeliveryDetails(new FetchDeliveryDetailsListCallback(deliveryDetailLiveD));
    }

    public LiveData<List<DeliveryDetail>> getDeliveryDetailLiveData(){
        return deliveryDetailLiveD;
    }

    private static class FetchDeliveryDetailsListCallback implements VolleySeverRequest.VolleyResponseCallback {
        private WeakReference<MutableLiveData<List<DeliveryDetail>>> weakDeliveryListLiveD;

        public FetchDeliveryDetailsListCallback(MutableLiveData<List<DeliveryDetail>> deliveryListLiveD) {
            this.weakDeliveryListLiveD = new WeakReference<>(deliveryListLiveD);
        }

        @Override
        public void onSuccess(JSONObject response) throws JSONException {
            JSONArray jsonElements = response.getJSONArray("data");

            List<DeliveryDetail> deliveryDetailList = new ArrayList<>();

            if (jsonElements != null) {
                Gson gson = new Gson();

                for (int i = 0; i < jsonElements.length(); i++) {
                    DeliveryDetail deliveryDetail = gson.fromJson(jsonElements.get(i).toString(), DeliveryDetail.class);
                    deliveryDetailList.add(deliveryDetail);
                }
            }

            MutableLiveData<List<DeliveryDetail>> deliveryListLiveD = weakDeliveryListLiveD.get();
            if (deliveryListLiveD != null) {
                deliveryListLiveD.setValue(deliveryDetailList);
            }
        }

        @Override
        public void onFail(VolleyError error) {
            Log.d(TAG, "onFail: " + error.getMessage());
        }
    }
}
