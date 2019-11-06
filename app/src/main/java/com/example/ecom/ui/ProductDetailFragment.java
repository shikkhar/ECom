package com.example.ecom.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecom.QuantityCustomView;
import com.example.ecom.R;
import com.example.ecom.adapters.ViewPagerAdapter;
import com.example.ecom.model.CartSummary;
import com.example.ecom.model.Product;
import com.example.ecom.view_models.CartViewModel;
import com.google.android.material.button.MaterialButton;
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
public class ProductDetailFragment extends BaseCartFragment {

    public static final String REQUEST_ADD_TO_CART = "0";
    public static final String REQUEST_BUY_NOW = "1";

    private CartViewModel cartViewModel;
    private NavController navController;
    private Product selectedProduct;
    private ViewPagerAdapter mAdapter;
    private List<String> imageUrls = new ArrayList<>();
    private CartSummary cartSummary;
    private Menu optionsMenu;
    private boolean navigateToCart = false;

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
    @BindView(R.id.textViewDescription)
    TextView descriptionTextView;
    @BindView(R.id.buttonAddToCart)
    MaterialButton addToCartButton;
    @BindView(R.id.buttonBuyNow)
    MaterialButton buyNowButton;
    @BindView(R.id.customViewQuantiity)
    QuantityCustomView quantityCustomView;

    @OnClick(R.id.buttonAddToCart)
    void onAddToCartClick() {
        navigateToCart = false;
        cartViewModel.addToCart(selectedProduct, quantityCustomView.getQuantity());
    }

    @OnClick(R.id.buttonBuyNow)
    void onBuyNowClick() {
        navigateToCart = true;
        cartViewModel.addToCart(selectedProduct, quantityCustomView.getQuantity());
        /*if (navController != null)
            navController.navigate(R.id.action_productDetailFragment_to_cartFragment);*/
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
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        navController = Navigation.findNavController(view);
        tabLayout.setupWithViewPager(viewPager);
        DecimalFormat df = new DecimalFormat("#.##");

        String finalPrice = df.format(selectedProduct.getFinalPrice());
        String originalPrice = df.format(selectedProduct.getOriginalPrice());
        String rupeeSymbol = getString(R.string.Rs);


        titleTextView.setText(selectedProduct.getTitle());
        descriptionTextView.setText(selectedProduct.getShortDescription());
        finalPriceTextView.setText(rupeeSymbol + finalPrice);
        originalPriceTextView.setText(rupeeSymbol + originalPrice);
        //TODO: replace with a string resource
        discountTextView.setText(selectedProduct.getDiscount() + "% Off");

        //navController.navigate(R.id.action_productListFragment_to_productDetailFragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

         /*String[] images = new String[]{
                "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
        };*/


        super.onActivityCreated(savedInstanceState);

        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);

        cartViewModel.getCartSummaryLiveData().observe(getViewLifecycleOwner(), new Observer<CartSummary>() {
            @Override
            public void onChanged(CartSummary cartSummaryObject) {
                cartSummary = cartSummaryObject;
                if (optionsMenu != null)
                    setCartCount(getContext().getApplicationContext(), optionsMenu, String.valueOf(cartSummary.getItemCount()));
            }
        });

        cartViewModel.getAddToCartResult().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean && navigateToCart && navController != null) {
                cartViewModel.setAddToCartResult(false);
                navController.navigate(R.id.action_productDetailFragment_to_cartFragment);
            }

        });

        mAdapter = new ViewPagerAdapter(getContext(), imageUrls);
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                navController.navigate(R.id.action_productDetailFragment_to_cartFragment);
                return true;
            //return NavigationUI.onNavDestinationSelected(item, navController);
            //|| super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        optionsMenu = menu;
        menu.findItem(R.id.action_search).setVisible(false);

        if (cartSummary == null)
            setCartCount(getContext().getApplicationContext(), menu, getString(R.string.zero));
        else
            setCartCount(getContext().getApplicationContext(), menu, String.valueOf(cartSummary.getItemCount()));

        super.onPrepareOptionsMenu(menu);
    }

}
