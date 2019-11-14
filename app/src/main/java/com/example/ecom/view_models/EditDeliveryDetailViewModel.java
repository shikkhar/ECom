package com.example.ecom.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.ecom.model.DeliveryDetail;
import com.example.ecom.repository.Repository;
import com.example.ecom.utils.Event;
import com.example.ecom.utils.VolleySeverRequest;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * This class is used to notify the concerned component when the update address request is complete
 * and whether it was successful or not
 */
public class EditDeliveryDetailViewModel extends ViewModel {
    private Repository repository;
    private MutableLiveData<Event<Boolean>> isOperationSuccessfulLiveD = new MutableLiveData<>();

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public void updateDeliveryDetails(DeliveryDetail newDeliveryDetails) {
        repository.updateDeliveryDetail(new DeliveryDetailOperationCallback(isOperationSuccessfulLiveD), newDeliveryDetails);
    }

    public void deleteDeliveryDetail(DeliveryDetail deliveryDetails) {
        repository.deleteDeliveryDetail(new DeliveryDetailOperationCallback(isOperationSuccessfulLiveD), deliveryDetails.getId());
    }

    public LiveData<Event<Boolean>> getOperationSuccessLiveD() {
        return isOperationSuccessfulLiveD;
    }

    private static class DeliveryDetailOperationCallback implements VolleySeverRequest.VolleyResponseCallback {
        private static final String TAG = "DeliveryDetailOperation";
        private WeakReference<MutableLiveData<Event<Boolean>>> weakIsOperationSuccessLiveD;

        DeliveryDetailOperationCallback(MutableLiveData<Event<Boolean>> isOperationSuccessLiveD) {
            this.weakIsOperationSuccessLiveD = new WeakReference<>(isOperationSuccessLiveD);
        }

        @Override
        public void onSuccess(JSONObject response) {
            MutableLiveData<Event<Boolean>> isOperationSuccessLiveD = weakIsOperationSuccessLiveD.get();
            if (isOperationSuccessLiveD != null)
                isOperationSuccessLiveD.setValue(new Event<>(true));
        }

        @Override
        public void onFail(VolleyError error) {
            MutableLiveData<Event<Boolean>> isOperationSuccessLiveD = weakIsOperationSuccessLiveD.get();
            if (isOperationSuccessLiveD != null)
                isOperationSuccessLiveD.setValue(new Event<>(false));
            Log.d(TAG, "onFail:" + error.getMessage());
        }
    }
}
