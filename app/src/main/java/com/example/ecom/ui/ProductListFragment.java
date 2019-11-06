package com.example.ecom.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
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
import android.view.inputmethod.EditorInfo;

import com.example.ecom.MyRecyclerView;
import com.example.ecom.R;
import com.example.ecom.adapters.ProductListAdapter;
import com.example.ecom.model.CartSummary;
import com.example.ecom.model.Product;
import com.example.ecom.view_models.CartViewModel;
import com.example.ecom.view_models.ProductViewModel;

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
    private ProductListAdapter mAdapter;
    private ProductAdapterChangesListener adapterChangesListener;
    private CartSummary cartSummary;
    private Menu optionsMenu;

    //    @BindView(R.id.editTextProductFilter)
    //    EditText filterEditText;
    //    @BindView(R.id.textInputLayoutProductFilter)
    //    TextInputLayout textInputLayout;
    @BindView(R.id.recyclerViewProductList)
    MyRecyclerView recyclerViewProductList;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new ProductListAdapter(this.getContext().getApplicationContext());

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
            mAdapter.setList(products);
        });

        cartViewModel.getCartSummaryLiveData().observe(getViewLifecycleOwner(), new Observer<CartSummary>() {
            @Override
            public void onChanged(CartSummary cartSummaryObject) {
                cartSummary = cartSummaryObject;
                if (optionsMenu != null)
                    setCartCount(getContext().getApplicationContext(), optionsMenu, String.valueOf(cartSummary.getItemCount()));
            }
        });
    }

    private void setupRecyclerView() {
        adapterChangesListener = new ProductAdapterChangesListener();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewProductList.setLayoutManager(layoutManager);
        mAdapter.setClickListener(this);
        mAdapter.registerAdapterDataObserver(adapterChangesListener);
        recyclerViewProductList.setAdapter(mAdapter);
    }

    @Override
    public void OnItemClick(Product selectedProduct) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_PRODUCT, selectedProduct);
        navController.navigate(R.id.action_productListFragment_to_productDetailFragment, bundle);
    }

    private class ProductAdapterChangesListener extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            recyclerViewProductList.scrollToPosition(0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            recyclerViewProductList.scrollToPosition(0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            recyclerViewProductList.scrollToPosition(0);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            recyclerViewProductList.scrollToPosition(0);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            recyclerViewProductList.scrollToPosition(0);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            recyclerViewProductList.scrollToPosition(0);
        }
    }

    /*private class SearchTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (productViewModel != null)
                productViewModel.getFilter().filter(s.toString());
        }
    }
*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
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


    /*
    public void setCartCount(Context context, Menu menu, String count) {
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCartCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_count, badge);
    }*/

    @Override
    public void onDestroyView() {
        mAdapter.unregisterAdapterDataObserver(adapterChangesListener);
        mAdapter.setClickListener(null);
        super.onDestroyView();
    }
}
