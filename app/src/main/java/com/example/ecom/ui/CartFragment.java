package com.example.ecom.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecom.R;
import com.example.ecom.adapters.CartAdapter;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.view_models.CartViewModel;
import com.example.ecom.view_models.ProductViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements CartAdapter.OnItemClickListener {
    public static final String BUNDLE_KEY_CART_TOTAL = "0";
    private NavController navController;
    private CartViewModel cartViewModel;
    private CartAdapter mAdapter;
    private Animation bounceAnimation;
    private double cartTotal;

    @BindView(R.id.textViewFinalAmount) TextView finalAmountTextView;
    @BindView(R.id.buttonCheckout) MaterialButton checkoutButton;
    @BindView(R.id.imageViewCartTotalDetail) ImageView cartTotalDetailsImageView;
    @BindView(R.id.recyclerViewCart) RecyclerView recyclerView;

    @OnClick({R.id.label,R.id.textViewFinalAmount,R.id.imageViewCartTotalDetail})
    void onLabelClick(){
        Bundle bundle = new Bundle();
        bundle.putDouble(BUNDLE_KEY_CART_TOTAL, cartTotal);
        navController.navigate(R.id.action_action_cart_to_summaryBottomSheetDialoagFragment);
    }

    @OnClick(R.id.buttonCheckout)
    void onProceedToCheckoutClick(){
        navController.navigate(R.id.action_cartFragment_to_deliveryDetailsListFragment);
    }

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new CartAdapter(getContext().getApplicationContext());
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
        bounceAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        //bounceAnimation.setRepeatCount(Animation.INFINITE);
        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);
        cartViewModel.getCartProductsLiveData().observe(getViewLifecycleOwner(), cartProductDetails -> {
            mAdapter.setList(cartProductDetails);
        });

        cartViewModel.getCartSummaryLiveData().observe(getViewLifecycleOwner(), cartSummaryObject -> {
            cartTotal = cartSummaryObject.getCartTotal();
            DecimalFormat df = new DecimalFormat("#.##");
            finalAmountTextView.setText(getString(R.string.Rs) + df.format(cartTotal));
            mAdapter.setCartSummary(cartTotal);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        cartTotalDetailsImageView.startAnimation(bounceAnimation);
    }

    @Override
    public void onPause() {
        cartTotalDetailsImageView.clearAnimation();
        super.onPause();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.action_cart).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void updateCartClick(CartProductDetail cartProductDetail) {

    }

    @Override
    public void onRemoveClick(int position) {
        cartViewModel.removeFromCart(position);
    }
}
