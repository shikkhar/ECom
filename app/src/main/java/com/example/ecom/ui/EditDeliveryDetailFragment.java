package com.example.ecom.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecom.R;
import com.example.ecom.model.Address;
import com.example.ecom.model.DeliveryDetail;
import com.example.ecom.repository.Repository;
import com.example.ecom.view_models.EditDeliveryDetailViewModel;
import com.example.ecom.view_models.MainActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.ecom.ui.DeliveryDetailListFragment.BUNDLE_KEY_DELIVERY_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDeliveryDetailFragment extends Fragment {

    private DeliveryDetail deliveryDetail;
    private EditDeliveryDetailViewModel editDeliveryDetailViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private NavController navController;

    public EditDeliveryDetailFragment() {
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
    @BindView(R.id.buttonSaveAddress)
    Button saveAddressButton;
    @BindView(R.id.buttonDeleteAddress)
    Button deleteAddressButton;

    @OnClick(R.id.buttonSaveAddress)
    void onSaveClick() {
        getUpdatedValues();
        editDeliveryDetailViewModel.updateDeliveryDetails(getUpdatedValues());
    }

    private DeliveryDetail getUpdatedValues() {
        Address address = new Address(houseNumberEditText.getText().toString(),
                localityEditText.getText().toString(),
                cityEditText.getText().toString(),
                stateEditText.getText().toString(),
                pincodeEditText.getText().toString(),
                landmarkEditText.getText().toString());

        return new DeliveryDetail(deliveryDetail.getId(),
                fullNameEditText.getText().toString(),
                address,
                mobileNumberEditText.getText().toString());

    }

    @OnClick(R.id.buttonDeleteAddress)
    void onDeleteClick() {
        editDeliveryDetailViewModel.deleteDeliveryDetail(deliveryDetail);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BUNDLE_KEY_DELIVERY_DETAIL)) {
            this.deliveryDetail = bundle.getParcelable(BUNDLE_KEY_DELIVERY_DETAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_delivery_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Address address = deliveryDetail.getAddress();
        fullNameEditText.setText(deliveryDetail.getUserName());
        mobileNumberEditText.setText(deliveryDetail.getContactNumber());
        houseNumberEditText.setText(address.getHouse());
        localityEditText.setText(address.getLocality());
        cityEditText.setText(address.getCity());
        stateEditText.setText(address.getState());
        pincodeEditText.setText(address.getPincode());
        landmarkEditText.setText(address.getLandmark());

        navController = Navigation.findNavController(view);

        editDeliveryDetailViewModel = ViewModelProviders.of(this).get(EditDeliveryDetailViewModel.class);
        editDeliveryDetailViewModel.setRepository(new Repository());
        editDeliveryDetailViewModel.getOperationSuccessLiveD().observe(getViewLifecycleOwner(), booleanEvent -> {
            Boolean isSuccessful = booleanEvent.getContentIfNotHandled();
            if(isSuccessful != null && navController != null){
                navController.navigate(R.id.action_editDeliveryDetailFragment_to_deliveryDetailListFragment);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.setActionBarTitle(getResources().getString(R.string.title_action_bar_edit_delivery_details));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                navController.popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.fragment_cart).setVisible(false);
        //menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_favorite).setVisible(false);
    }

}
