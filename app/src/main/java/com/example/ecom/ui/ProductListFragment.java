package com.example.ecom.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.ecom.MyRecyclerView;
import com.example.ecom.R;
import com.example.ecom.adapters.DealsAdapter;
import com.example.ecom.adapters.ProductListAdapter;
import com.example.ecom.adapters.ViewPagerAdapter;
import com.example.ecom.model.CartSummary;
import com.example.ecom.model.Product;
import com.example.ecom.utils.UpdateOperationResult;
import com.example.ecom.view_models.CartViewModel;
import com.example.ecom.view_models.ProductViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends BaseCartFragment implements ProductListAdapter.OnItemClickListener {
    public static final String BUNDLE_KEY_PRODUCT = "product_key";
    private NavController navController;
    private ProductViewModel productViewModel;
    private CartViewModel cartViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private ProductListAdapter productListAdapter;
    private DealsAdapter dealsAdapter;
    private ProductAdapterChangesListener adapterChangesListener;
    private CartSummary cartSummary;
    private Menu optionsMenu;
    private Handler handler;
    private AutoScrollRunnable runnable;
    private int delay = 2500; //milliseconds
    private int page = 0;

    @BindView(R.id.viewPagerBanner)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.recyclerViewProductList)
    MyRecyclerView productListRecyclerView;
    @BindView(R.id.recyclerViewDeals)
    RecyclerView dealsRecyclerView;
    @BindView(R.id.searchGroup)
    Group searchGroup;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        productListAdapter = new ProductListAdapter(this.getContext().getApplicationContext());
        dealsAdapter = new DealsAdapter();
        handler = new Handler();
        runnable = new AutoScrollRunnable();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        navController = Navigation.findNavController(view);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //filterEditText.addTextChangedListener(new SearchTextWatcher());
        //navController.navigate(R.id.action_productListFragment_to_productDetailFragment);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecyclerView();
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);

        productViewModel.getProductLiveData().observe(getViewLifecycleOwner(), products -> {
            //productListRecyclerView.setVisibility(View.VISIBLE);
            productListAdapter.setList(products);
        });

        cartViewModel.getCartSummaryLiveData().observe(getViewLifecycleOwner(), new Observer<CartSummary>() {
            @Override
            public void onChanged(CartSummary cartSummaryObject) {
                cartSummary = cartSummaryObject;
                if (optionsMenu != null)
                    setCartCount(getContext().getApplicationContext(), optionsMenu, String.valueOf(cartSummary.getItemCount()));
            }
        });

        productViewModel.getUpdateOperationStatus().observe(this.getViewLifecycleOwner(), booleanEvent -> {
            if (!booleanEvent.isHasBeenHandled()) {
                UpdateOperationResult updateOperationResult = booleanEvent.getContentIfNotHandled();
                Product product = updateOperationResult.getProduct();
                if (updateOperationResult.isSuccessful()) {
                    if (product.isFavorite())
                        Toast.makeText(getContext(), "Added to favorites successfully", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Removed from favorites successfully", Toast.LENGTH_SHORT).show();
                } else {
                    /*List<Product> temp = productViewModel.getProductLiveData().getValue();
                    List<Product> temp1 = new ArrayList<>(temp);*/
                    //product.setFavorite(!product.isFavorite());
                    //productListAdapter.setList(productViewModel.getProductLiveData().getValue());
                    //TODO: call adapter method to revert icon change
                    Toast.makeText(getContext(), "Favorites could not be updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String[] images = new String[]{
                "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
                "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
        };

        viewPagerAdapter = new ViewPagerAdapter(getContext().getApplicationContext(), Arrays.asList(images));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void setupRecyclerView() {
        adapterChangesListener = new ProductAdapterChangesListener();
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        productListRecyclerView.setLayoutManager(gridLayoutManager);
        productListAdapter.setClickListener(this);
        productListAdapter.registerAdapterDataObserver(adapterChangesListener);
        productListRecyclerView.setAdapter(productListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        dealsRecyclerView.setLayoutManager(linearLayoutManager);
        dealsRecyclerView.setAdapter(dealsAdapter);
    }

    @Override
    public void onItemClick(Product selectedProduct) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_PRODUCT, selectedProduct);
        navController.navigate(R.id.action_productListFragment_to_productDetailFragment, bundle);
    }

    @Override
    public void onFavIconClick(int position, Product selectedProduct) {
        productViewModel.updateFavoriteStatus(position, selectedProduct);
    }

    private class ProductAdapterChangesListener extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            productListRecyclerView.scrollToPosition(0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            productListRecyclerView.scrollToPosition(0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            productListRecyclerView.scrollToPosition(0);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            productListRecyclerView.scrollToPosition(0);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            productListRecyclerView.scrollToPosition(0);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            productListRecyclerView.scrollToPosition(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_cart:
                navController.navigate(R.id.action_productListFragment_to_cartFragment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        optionsMenu = menu;

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.trim().isEmpty()) {
                    searchGroup.setVisibility(View.GONE);
                    //productListRecyclerView.setVisibility(View.GONE);
                } else
                    searchGroup.setVisibility(View.VISIBLE);

                if (productViewModel != null)
                    productViewModel.getFilter().filter(newText);

                return false;
            }
        });

        if (cartSummary == null)
            setCartCount(getContext().getApplicationContext(), menu, "0");
        else
            setCartCount(getContext().getApplicationContext(), menu, String.valueOf(cartSummary.getItemCount()));

        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onDestroyView() {
        productListAdapter.unregisterAdapterDataObserver(adapterChangesListener);
        productListAdapter.setClickListener(null);
        super.onDestroyView();
    }

    private class AutoScrollRunnable implements Runnable {
        @Override
        public void run() {
            if (viewPagerAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }

            viewPager.setCurrentItem(page);
            handler.postDelayed(this, delay);
        }
    }

}
