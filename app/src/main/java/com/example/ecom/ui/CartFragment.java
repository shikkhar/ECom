package com.example.ecom.ui;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecom.R;
import com.example.ecom.adapters.CartAdapter;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.utils.Event;
import com.example.ecom.utils.UpdateOperationResult;
import com.example.ecom.view_models.CartViewModel;
import com.example.ecom.view_models.MainActivityViewModel;
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
    private MainActivityViewModel mainActivityViewModel;
    private CartAdapter mAdapter;
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
    RecyclerView recyclerView;@BindView(R.id.frameProgrssBar)
    FrameLayout frameProgressBar;

    @OnClick({R.id.label, R.id.textViewFinalAmount, R.id.textViewSavings})
    void onLabelClick() {
        recyclerView.smoothScrollToPosition(cartViewModel.getCartProductsLiveData().getValue().size());
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

        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.setActionBarTitle(getResources().getString(R.string.title_action_bar_cart));

        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);
        cartViewModel.getCartProductsLiveData().observe(getViewLifecycleOwner(), cartProductDetails -> {
            if (overlayDialog != null) {
                frameProgressBar.setVisibility(View.GONE);
                overlayDialog.dismiss();
                overlayDialog = null;
            }

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
            int savings = cartSummaryObject.getSavings();
            DecimalFormat df = new DecimalFormat("#.##");
            finalAmountTextView.setText(getString(R.string.Rs) + df.format(cartTotal));
            if(savings == 0)
                savingsTextView.setVisibility(View.GONE);
            else {
                savingsTextView.setText(getString(R.string.savings, getString(R.string.Rs), String.valueOf(savings)));
                savingsTextView.setVisibility(View.VISIBLE);
            }
            mAdapter.setCartSummary(cartTotal);
        });

        cartViewModel.getUpdateOperationStatus().observe(getViewLifecycleOwner(), updateOperationResultEvent -> {
            if (overlayDialog != null) {
                frameProgressBar.setVisibility(View.GONE);
                overlayDialog.dismiss();
                overlayDialog = null;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //TODO: replace this with fragment manager's pop back stack
                navController.popBackStack();
                return true;
            case R.id.action_favorite:
                navController.navigate(R.id.action_fragment_cart_to_favoritesFragment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.fragment_cart).setVisible(false);
        //menu.findItem(R.id.action_search).setVisible(false);
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
        quantityPickerDialogFragment.setArguments(bundle);
        quantityPickerDialogFragment.show(fm, TAG_QUANTITY_DIALOG_FRAGMENT);
    }

    @Override
    public void onRemoveClick(int position) {
        showOverlayDialog();
        cartViewModel.removeFromCart(position);
    }

    @Override
    public void onValueChange(int position, int value) {
        quantityPickerDialogFragment.dismiss();
        showOverlayDialog();

        if (value == 0)
            cartViewModel.removeFromCart(position);
        else
            cartViewModel.updateCart(position, value);
    }

    private void showOverlayDialog() {
        if (overlayDialog == null) {
            overlayDialog = new Dialog(getContext());
            overlayDialog.show();
            frameProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
