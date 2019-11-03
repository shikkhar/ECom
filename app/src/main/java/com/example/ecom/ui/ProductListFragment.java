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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ecom.R;
import com.example.ecom.adapters.ProductListAdapter;
import com.example.ecom.model.Product;
import com.example.ecom.view_models.ProductSharedViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment implements ProductListAdapter.OnItemClickListener {
    public static final String BUNDLE_KEY_PRODUCT = "product_key";
    private NavController navController;
    private ProductSharedViewModel productSharedViewModel;
    private ProductListAdapter mAdapter;

    @BindView(R.id.editTextProductFilter)
    EditText filterEditText;
//    @BindView(R.id.textInputLayoutProductFilter)
//    TextInputLayout textInputLayout;
    @BindView(R.id.recyclerViewProductList)
    RecyclerView recyclerViewProductList;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new ProductListAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.collapsible_toolbar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        navController = Navigation.findNavController(view);
        //navController.navigate(R.id.action_productListFragment_to_productDetailFragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecyclerView();
        productSharedViewModel = ViewModelProviders.of(getActivity()).get(ProductSharedViewModel.class);
        productSharedViewModel.getProductLiveData().observe(getViewLifecycleOwner(), products -> {
            mAdapter.submitList(products);
        });
    }

    private void setupRecyclerView() {
        recyclerViewProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setClickListener(this);
        recyclerViewProductList.setAdapter(mAdapter);
    }

    @Override
    public void OnItemClick(Product selectedProduct) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_PRODUCT, selectedProduct);
        navController.navigate(R.id.action_productListFragment_to_productDetailFragment, bundle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cartFragment:

                navController.navigate(R.id.action_productListFragment_to_cartFragment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
