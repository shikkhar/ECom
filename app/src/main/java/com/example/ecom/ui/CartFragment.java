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
import android.view.View;
import android.view.ViewGroup;

import com.example.ecom.R;
import com.example.ecom.adapters.CartAdapter;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.view_models.ProductSharedViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private NavController navController;
    private ProductSharedViewModel productSharedViewModel;
    private CartAdapter mAdapter;
    @BindView(R.id.buttonCheckout) MaterialButton checkoutButton;
    @BindView(R.id.recyclerViewCart) RecyclerView recyclerView;


    @OnClick(R.id.buttonCheckout)
    void onProceedToCheckoutClick(){
        navController.navigate(R.id.action_cartFragment_to_deliveryDetailsFragment);
    }

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CartAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        navController = Navigation.findNavController(view);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        productSharedViewModel = ViewModelProviders.of(getActivity()).get(ProductSharedViewModel.class);
        productSharedViewModel.getCartProductsLiveData().observe(getViewLifecycleOwner(), cartProductDetails -> {
            mAdapter.setList(cartProductDetails);
        });

        productSharedViewModel.getCartSummaryLiveData().observe(getViewLifecycleOwner(), value -> {
            mAdapter.setCartSummary(value);
        });

    }
}
