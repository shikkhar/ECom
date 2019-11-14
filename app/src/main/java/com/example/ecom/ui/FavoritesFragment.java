package com.example.ecom.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecom.R;
import com.example.ecom.adapters.ProductListAdapter;
import com.example.ecom.model.Product;
import com.example.ecom.repository.Repository;
import com.example.ecom.view_models.FavoritesViewModel;
import com.example.ecom.view_models.MainActivityViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.ecom.ui.ProductListFragment.BUNDLE_KEY_PRODUCT;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment implements ProductListAdapter.OnItemClickListener {

    private ProductListAdapter adapter;
    private FavoritesViewModel viewModel;
    private MainActivityViewModel mainActivityViewModel;
    private NavController navController;

    @BindView(R.id.recyclerViewFavorites)
    RecyclerView recyclerView;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new ProductListAdapter(getContext().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        navController = Navigation.findNavController(view);
        setupRecyclerView();

        viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        viewModel.setRepository(new Repository());
        viewModel.getFavorites();
        viewModel.getFavoritesLiveD().observe(getViewLifecycleOwner(), favoritesList -> {
            adapter.setList(favoritesList);
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.setActionBarTitle(getResources().getString(R.string.title_action_bar_favorite));

    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Product selectedProduct) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_PRODUCT, selectedProduct);
        navController.navigate(R.id.action_favoritesFragment_to_productDetailFragment, bundle);
    }

    @Override
    public void onFavIconClick(Product selectedProduct) {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popBackStack();
                return true;
            case R.id.fragment_cart:
                navController.navigate(R.id.action_favoritesFragment_to_fragment_cart);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.action_favorite).setVisible(false);
        //menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
