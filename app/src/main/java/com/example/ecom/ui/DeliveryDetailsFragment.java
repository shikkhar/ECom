package com.example.ecom.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecom.R;
import com.example.ecom.adapters.DeliveryDetailsAdapter;
import com.example.ecom.model.DeliveryDetail;
import com.example.ecom.view_models.DeliveryDetailsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryDetailsFragment extends Fragment implements DeliveryDetailsAdapter.OnButtonClick {

    private DeliveryDetailsViewModel deliveryDetailsViewModel;
    private DeliveryDetailsAdapter mAdapter;
    private NavController navController;

    @BindView(R.id.recyclerViewAddress) RecyclerView recyclerView;

    public DeliveryDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new DeliveryDetailsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        deliveryDetailsViewModel = ViewModelProviders.of(this).get(DeliveryDetailsViewModel.class);
        deliveryDetailsViewModel.getDeliveryDetailLiveData().observe(getViewLifecycleOwner(), deliveryDetails ->
            mAdapter.setList(deliveryDetails));
        navController = Navigation.findNavController(view);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDeliverHereClick(int position) {

    }

    @Override
    public void onEditAddressClick(int position) {

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.action_cart).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
