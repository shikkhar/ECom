package com.example.ecom.ui;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecom.QuantityCustomView;
import com.example.ecom.R;
import com.example.ecom.adapters.ViewPagerAdapter;
import com.example.ecom.model.CartSummary;
import com.example.ecom.model.Product;
import com.example.ecom.utils.UpdateOperationResult;
import com.example.ecom.view_models.CartViewModel;
import com.example.ecom.view_models.MainActivityViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.ecom.ui.ProductListFragment.BUNDLE_KEY_PRODUCT;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends BaseFragment {

    private CartViewModel cartViewModel;
    private NavController navController;
    private Product selectedProduct;
    private ViewPagerAdapter mAdapter;
    private List<String> imageUrls = new ArrayList<>();
    private CartSummary cartSummary;
    private Menu optionsMenu;
    private boolean navigateToCart = false;
    private Dialog overlayDialog;
    private MainActivityViewModel mainActivityViewModel;
    //private SearchView searchView;

    @BindView(R.id.viewPagerProductDetail)
    ViewPager viewPager;
    @BindView(R.id.tabLayoutProductImages)
    TabLayout tabLayout;
    @BindView(R.id.textViewProductTitle)
    TextView titleTextView;
    @BindView(R.id.textViewFinalPrice)
    TextView finalPriceTextView;
    @BindView(R.id.textViewOriginalPrice)
    TextView originalPriceTextView;
    @BindView(R.id.textViewDiscount)
    TextView discountTextView;
    @BindView(R.id.webViewDescription)
    WebView descriptionTextView;
    @BindView(R.id.buttonAddToCart)
    MaterialButton addToCartButton;
    @BindView(R.id.buttonBuyNow)
    MaterialButton buyNowButton;
    @BindView(R.id.customViewQuantiity)
    QuantityCustomView quantityCustomView;
    @BindView(R.id.frameProgrssBar)
    FrameLayout frameProgressBar;
    @BindView(R.id.editTextPincode)
    EditText enterPincodeEditText;
    @BindView(R.id.buttonCheckDelivery)
    Button checkDeliveryButton;
    @BindView(R.id.imageViewFavoriteGray)
    ImageView grayFavoriteImageView;
    @BindView(R.id.imageViewFavorite)
    ImageView favoriteImageView;

    @OnClick({R.id.imageViewFavoriteGray, R.id.imageViewFavorite})
    void onFavIconClick() {
        if (selectedProduct.isFavorite()) {
            Animation animZoomOut = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.zoom_out);
            favoriteImageView.startAnimation(animZoomOut);
            this.grayFavoriteImageView.setVisibility(View.VISIBLE);
            this.favoriteImageView.setVisibility(View.GONE);
        } else {
            this.favoriteImageView.setVisibility(View.VISIBLE);
            this.grayFavoriteImageView.setVisibility(View.GONE);
            Animation animZoomIn = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.zoom_in);
            favoriteImageView.startAnimation(animZoomIn);
        }
        selectedProduct.setFavorite(!selectedProduct.isFavorite());
        cartViewModel.updateFavoriteStatus(selectedProduct);
    }

    @OnClick(R.id.buttonAddToCart)
    void onAddToCartClick() {
        showOverlayDialog();
        navigateToCart = false;
        cartViewModel.addToCart(selectedProduct, quantityCustomView.getQuantity());
    }

    @OnClick(R.id.buttonBuyNow)
    void onBuyNowClick() {
        showOverlayDialog();
        navigateToCart = true;
        cartViewModel.addToCart(selectedProduct, quantityCustomView.getQuantity());
        /*if (navController != null)
            navController.navigate(R.id.action_productDetailFragment_to_cartFragment);*/
    }

    private void showOverlayDialog() {
        if (overlayDialog == null) {
            overlayDialog = new Dialog(getContext());
            overlayDialog.show();
            frameProgressBar.setVisibility(View.VISIBLE);
        }
    }


    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BUNDLE_KEY_PRODUCT)) {
            selectedProduct = bundle.getParcelable(BUNDLE_KEY_PRODUCT);
        }

        if (selectedProduct != null)
            imageUrls = selectedProduct.getImagePaths();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail_collapsible, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        navController = Navigation.findNavController(view);
        tabLayout.setupWithViewPager(viewPager);
        bindView();
        //navController.navigate(R.id.action_productListFragment_to_productDetailFragment);
    }

    private void bindView() {
        DecimalFormat df = new DecimalFormat("#.##");
        String finalPrice = df.format(selectedProduct.getFinalPrice());
        String originalPrice = df.format(selectedProduct.getOriginalPrice());
        String rupeeSymbol = getString(R.string.Rs);


        titleTextView.setText(selectedProduct.getTitle());
        descriptionTextView.setBackgroundColor(Color.TRANSPARENT);
        descriptionTextView.loadUrl("file:///android_asset/sample.html");

        finalPriceTextView.setText(rupeeSymbol + finalPrice);
        originalPriceTextView.setText(rupeeSymbol + originalPrice);
        //TODO: replace with a string resource
        discountTextView.setText(selectedProduct.getDiscount() + "% Off");
        if (selectedProduct.isFavorite()) {
            this.favoriteImageView.setVisibility(View.VISIBLE);
            this.grayFavoriteImageView.setVisibility(View.GONE);
            //product.setFavorite(!product.isFavorite());
        } else {
            this.favoriteImageView.setVisibility(View.GONE);
            this.grayFavoriteImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.setActionBarTitle(getResources().getString(R.string.title_action_bar_product_detail));

        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);

        cartViewModel.getCartSummaryLiveData().observe(getViewLifecycleOwner(), cartSummaryObject -> {
            cartSummary = cartSummaryObject;
            if (optionsMenu != null)
                setCartCount(getContext().getApplicationContext(), optionsMenu, String.valueOf(cartSummary.getItemCount()));
        });

        cartViewModel.getAddToCartResult().observe(getViewLifecycleOwner(), event -> {

            if (overlayDialog != null) {
                frameProgressBar.setVisibility(View.GONE);
                overlayDialog.dismiss();
                overlayDialog = null;
            }

            if (!event.isHasBeenHandled() && event.getContentIfNotHandled() && navController != null) {
                //cartViewModel.setAddToCartResult(false);
                if (navigateToCart) {
                    navigateToCart = false;
                    navController.navigate(R.id.action_productDetailFragment_to_cartFragment);
                } else {
                    if (getView() != null)
                        Snackbar.make(getView(), "Add to cart successful", Snackbar.LENGTH_LONG).setAction("See Cart", v -> {
                            navigateToCart = false;
                            navController.navigate(R.id.action_productDetailFragment_to_cartFragment);
                        }).show();
                }
            }

        });

        cartViewModel.getUpdateOperationStatus().observe(this.getViewLifecycleOwner(), booleanEvent -> {
            if (!booleanEvent.isHasBeenHandled()) {
                UpdateOperationResult updateOperationResult = booleanEvent.getContentIfNotHandled();
                Product product = updateOperationResult.getProduct();
                if (updateOperationResult.isSuccessful()) {
                    if (product.isFavorite())
                        Toast.makeText(getContext(), "Added to favorites successfully", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Removed from favorites successfully", Toast.LENGTH_SHORT).show();
                } else {
                    product.setFavorite(!product.isFavorite());
                    Toast.makeText(getContext(), "Favorites could not be updated", Toast.LENGTH_SHORT).show();
                    if (!product.isFavorite()) {
                        this.favoriteImageView.setVisibility(View.GONE);
                        this.grayFavoriteImageView.setVisibility(View.VISIBLE);
                    } else {
                        this.favoriteImageView.setVisibility(View.VISIBLE);
                        this.grayFavoriteImageView.setVisibility(View.GONE);
                    }
                }
            }
        });

        mAdapter = new ViewPagerAdapter(getContext().getApplicationContext(), imageUrls);
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popBackStack();
                return true;
            case R.id.fragment_cart:
                navController.navigate(R.id.action_productDetailFragment_to_cartFragment);
                return true;

            case R.id.action_favorite:
                navController.navigate(R.id.action_productDetailFragment_to_favoritesFragment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        /*MenuItem searchItem = menu.findItem(R.id.action_search);
        *//*SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        //searchView.setIconified(true);
        searchView.onActionViewCollapsed();*//*

        *//*if (!searchView.isIconified()) {
            searchView.setIconified(true);
           // searchView.
        }*//*
        searchItem.setVisible(false);
*/
        if (cartSummary == null) {
            setCartCount(getContext().getApplicationContext(), menu, getString(R.string.zero));
        } else {
            setCartCount(getContext().getApplicationContext(), menu, String.valueOf(cartSummary.getItemCount()));
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        /*super.onPrepareOptionsMenu(menu);
        optionsMenu = menu;
        MenuItem searchItem = menu.findItem(R.id.action_search);
        *//*SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        //searchView.setIconified(true);
        searchView.onActionViewCollapsed();*//*

        *//*if (!searchView.isIconified()) {
            searchView.setIconified(true);
           // searchView.
        }*//*
        searchItem.setVisible(false);*/

        if (cartSummary == null) {
            setCartCount(getContext().getApplicationContext(), menu, getString(R.string.zero));
        } else {
            setCartCount(getContext().getApplicationContext(), menu, String.valueOf(cartSummary.getItemCount()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
