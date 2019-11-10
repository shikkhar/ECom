package com.example.ecom.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecom.R;
import com.example.ecom.model.Address;
import com.example.ecom.model.DeliveryDetail;
import com.example.ecom.repository.Repository;
import com.example.ecom.utils.Event;
import com.example.ecom.view_models.AddDeliveryDetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.ecom.ui.DeliveryDetailListFragment.BUNDLE_KEY_DELIVERY_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDeliveryDetailFragment extends Fragment {

    private AddDeliveryDetailViewModel viewModel;

    public AddDeliveryDetailFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.editTextFullName)
    EditText fullNameEditText;
    @BindView(R.id.editTextMobileNumber)
    EditText mobileNumberEditText;
    @BindView(R.id.editTextHouseNo)
    EditText houseNumberEditText;
    @BindView(R.id.editTextLocality)
    EditText localityEditText;
    @BindView(R.id.editTextCity)
    EditText cityEditText;
    @BindView(R.id.editTextState)
    EditText stateEditText;
    @BindView(R.id.editTextPincode)
    EditText pincodeEditText;
    @BindView(R.id.editTextLandmark)
    EditText landmarkEditText;
    @BindView(R.id.buttonAddAddress)
    Button addAddressButton;

    @OnClick(R.id.buttonAddAddress)
    void onAddClick() {
        viewModel.addDeliveryDetail(getNewValues());
    }

    private DeliveryDetail getNewValues() {
        Address address = new Address(houseNumberEditText.getText().toString(),
                localityEditText.getText().toString(),
                cityEditText.getText().toString(),
                stateEditText.getText().toString(),
                pincodeEditText.getText().toString(),
                landmarkEditText.getText().toString());

        return new DeliveryDetail(fullNameEditText.getText().toString(), address,
                mobileNumberEditText.getText().toString());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_delivery_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        NavController navController = Navigation.findNavController(view);

        viewModel = ViewModelProviders.of(this).get(AddDeliveryDetailViewModel.class);
        viewModel.setRepository(new Repository());
        viewModel.getOperationSuccessLiveD().observe(getViewLifecycleOwner(), booleanEvent -> {
            Boolean isSuccessful = booleanEvent.getContentIfNotHandled();
            if (isSuccessful != null && navController != null) {
                if (isSuccessful)
                    navController.navigate(R.id.action_addDeliveryDetailFragment_to_deliveryDetailListFragment);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
