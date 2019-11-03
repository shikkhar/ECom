package com.example.ecom.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecom.R;
import com.example.ecom.adapters.ViewPagerAdapter;
import com.example.ecom.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.ecom.ui.ProductListFragment.BUNDLE_KEY_PRODUCT;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends Fragment {

    private NavController navController;
    private Product selectedProduct;
    private ViewPagerAdapter mAdapter;
    private List<String> imageUrls = new ArrayList<>();

    @BindView(R.id.viewPagerProductDetail) ViewPager viewPager;
    @BindView(R.id.tabLayoutProductImages) TabLayout tabLayout;
    @BindView(R.id.textViewProductTitle) TextView titleTextView;
    @BindView(R.id.textViewFinalPrice) TextView finalPriceTextView;
    @BindView(R.id.textViewOriginalPrice) TextView originalPriceTextView;
    @BindView(R.id.textViewDiscount) TextView discountTextView;
    @BindView(R.id.textViewDescription) TextView descriptionTextView;
    @BindView(R.id.buttonAddToCart) MaterialButton addToCartButton;
    @BindView(R.id.buttonBuyNow) MaterialButton buyNowButton;

    @OnClick(R.id.buttonAddToCart)
    void onAddToCartClick(){

    }

    @OnClick(R.id.buttonBuyNow)
    void onBuyNowClick(){

    }


    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(BUNDLE_KEY_PRODUCT)){
            selectedProduct = bundle.getParcelable(BUNDLE_KEY_PRODUCT);
        }
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

         String[] images = new String[]{
                "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
        };

        imageUrls = Arrays.asList(images);

        super.onActivityCreated(savedInstanceState);
        mAdapter = new ViewPagerAdapter(getContext(), imageUrls);
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cartFragment:
                navController.navigate(R.id.action_productDetailFragment_to_cartFragment);
                return true;
                //return NavigationUI.onNavDestinationSelected(item, navController);
                        //|| super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
