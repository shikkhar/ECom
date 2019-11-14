package com.example.ecom.ui;


import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.Group;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.ecom.R;
import com.example.ecom.adapters.DealsAdapter;
import com.example.ecom.adapters.ProductListAdapter;
import com.example.ecom.adapters.ViewPagerAdapter;
import com.example.ecom.model.CartSummary;
import com.example.ecom.model.Product;
import com.example.ecom.repository.Repository;
import com.example.ecom.utils.OperationResult;
import com.example.ecom.utils.UpdateOperationResult;
import com.example.ecom.view_models.CartViewModel;
import com.example.ecom.view_models.MainActivityViewModel;
import com.example.ecom.view_models.ProductViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends BaseFragment implements ProductListAdapter.OnItemClickListener {
    public static final String BUNDLE_KEY_PRODUCT = "product_key";
    private NavController navController;
    private ProductViewModel productViewModel;
    private CartViewModel cartViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private ProductListAdapter productListAdapter;
    private DealsAdapter dealsAdapter;
    private ProductAdapterChangesListener adapterChangesListener;
    private CartSummary cartSummary;
    private Menu optionsMenu;
    private Handler handler;
    private AutoScrollRunnable runnable;
    private SearchView searchView;
    private int delay = 2500; //milliseconds
    private int page = 0;

    @BindView(R.id.viewPagerBanner)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.recyclerViewProductList)
    RecyclerView productListRecyclerView;
    @BindView(R.id.recyclerViewDeals)
    RecyclerView dealsRecyclerView;
    @BindView(R.id.searchGroup)
    Group searchGroup;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefresh;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        productListAdapter = new ProductListAdapter(this.getContext().getApplicationContext());
        dealsAdapter = new DealsAdapter();
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.setRepository(new Repository());
        productViewModel.getAllProducts();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecyclerView();

        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.setActionBarTitle(getResources().getString(R.string.title_action_bar_home));

        cartViewModel = ViewModelProviders.of(getActivity()).get(CartViewModel.class);

        //productViewModel.setRepository(new Repository());
        //productViewModel.getAllProducts();

        productViewModel.getProductLiveData().observe(getViewLifecycleOwner(), products ->
            productListAdapter.setList(products));

        cartViewModel.getCartSummaryLiveData().observe(getViewLifecycleOwner(), cartSummaryObject -> {
            cartSummary = cartSummaryObject;
            if (optionsMenu != null)
                setCartCount(getContext().getApplicationContext(), optionsMenu, String.valueOf(cartSummary.getItemCount()));
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
                    Toast.makeText(getContext(), "Favorites could not be updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        productViewModel.getOperationResult().observe(getViewLifecycleOwner(), operationResultEvent -> {
            if(!operationResultEvent.isHasBeenHandled()){
                OperationResult operationResult = operationResultEvent.getContentIfNotHandled();
                if(operationResult != null){
                    swipeRefresh.setRefreshing(false);
                }
            }
        });

        swipeRefresh.setOnRefreshListener(() -> {
            productViewModel.getAllProducts();
            cartViewModel.getAllCartDetails();
        });
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
        hideSearchView();

        navController.navigate(R.id.action_productListFragment_to_productDetailFragment, bundle);
    }

    private void hideSearchView() {
        //searchView.onActionViewCollapsed();
       /* if(!searchView.isIconified())
            searchView.setIconified(true);*/
    }

    @Override
    public void onFavIconClick(Product selectedProduct) {
        productViewModel.updateFavoriteStatus(selectedProduct);
    }

    private class ProductAdapterChangesListener extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            nestedScrollView.scrollTo(0, 0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            //nestedScrollView.scrollTo(0,0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            // nestedScrollView.scrollTo(0,0);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            nestedScrollView.scrollTo(0, 0);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            nestedScrollView.scrollTo(0, 0);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            nestedScrollView.scrollTo(0, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_cart:
                hideSearchView();
                navController.navigate(R.id.action_productListFragment_to_cartFragment);
                return true;

            case R.id.action_favorite:
                hideSearchView();
                navController.navigate(R.id.action_productListFragment_to_favoritesFragment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        optionsMenu = menu;

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
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
