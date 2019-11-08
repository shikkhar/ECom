package com.example.ecom.ui;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecom.R;
import com.example.ecom.adapters.CartAdapter;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.view_models.CartViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements CartAdapter.OnItemClickListener, QuantityPickerDialogFragment.ValueChangeCallback {
    public static final String BUNDLE_KEY_CART_TOTAL = "0";
    public static final String BUNDLE_KEY_LIST_POSITION = "1";
    public static final String TAG_QUANTITY_DIALOG_FRAGMENT = "2";

    private NavController navController;
    private CartViewModel cartViewModel;
    private CartAdapter mAdapter;
    private Animation bounceAnimation;
    private QuantityPickerDialogFragment quantityPickerDialogFragment;
    private double cartTotal;
    private Dialog overlayDialog;

    @BindView(R.id.textViewFinalAmount)
    TextView finalAmountTextView;
    @BindView(R.id.buttonCheckout)
    MaterialButton checkoutButton;
    @BindView(R.id.textViewSavings)
    TextView savingsTextView;
    @BindView(R.id.recyclerViewCart)
    RecyclerView recyclerView;

    @OnClick({R.id.label, R.id.textViewFinalAmount, R.id.textViewSavings})
    void onLabelClick() {
        recyclerView.smoothScrollToPosition(cartViewModel.getCartProductsLiveData().getValue().size());
        /*Bundle bundle = new Bundle();
        bundle.putDouble(BUNDLE_KEY_CART_TOTAL, cartTotal);
        navController.navigate(R.id.action_action_cart_to_summaryBottomSheetDialoagFragment);*/
    }

    @OnClick(R.id.buttonCheckout)
    void onProceedToCheckoutClick() {
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
        //layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bounceAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        //bounceAnimation.setRepeatCount(Animation.INFINITE);
        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);
        cartViewModel.getCartProductsLiveData().observe(getViewLifecycleOwner(), cartProductDetails -> {
            if (cartProductDetails == null) {
                finalAmountTextView.setVisibility(View.GONE);
                savingsTextView.setVisibility(View.GONE);
                checkoutButton.setEnabled(false);
                //TODO:replace this with string resource
                Toast.makeText(getContext(), "There are no items in your cart", Toast.LENGTH_LONG).show();
            }
            mAdapter.setList(cartProductDetails);
        });

        cartViewModel.getCartSummaryLiveData().observe(getViewLifecycleOwner(), cartSummaryObject -> {
            cartTotal = cartSummaryObject.getCartTotal();
            DecimalFormat df = new DecimalFormat("#.##");
            finalAmountTextView.setText(getString(R.string.Rs) + df.format(cartTotal));
            mAdapter.setCartSummary(cartTotal);
        });

    }

    /*@Override
    public void onResume() {
        super.onResume();
        cartTotalDetailsImageView.startAnimation(bounceAnimation);
    }

    @Override
    public void onPause() {
        cartTotalDetailsImageView.clearAnimation();
        super.onPause();
    }*/

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.fragment_cart).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void updateCartClick(int position, CartProductDetail cartProductDetail) {
        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG_QUANTITY_DIALOG_FRAGMENT);
        if (fragment != null)
            fm.beginTransaction().remove(fragment).commit();
        quantityPickerDialogFragment = new QuantityPickerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_LIST_POSITION, position);
        //bundle.putString("reward", reward);
        quantityPickerDialogFragment.setArguments(bundle);
        quantityPickerDialogFragment.show(fm, TAG_QUANTITY_DIALOG_FRAGMENT);
        //navController.navigate(R.id.action_action_cart_to_quantityPickerDialogFragment);
    }

    @Override
    public void onRemoveClick(int position) {
        cartViewModel.removeFromCart(position);
    }

    @Override
    public void onValueChange(int position, int value) {
        quantityPickerDialogFragment.dismiss();
        if (value == 0)
            cartViewModel.removeFromCart(position);
        else
            cartViewModel.updateCart(position, value);
    }
}
