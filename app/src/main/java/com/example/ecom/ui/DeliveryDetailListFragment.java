package com.example.ecom.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecom.R;
import com.example.ecom.adapters.DeliveryDetailsAdapter;
import com.example.ecom.model.DeliveryDetail;
import com.example.ecom.repository.Repository;
import com.example.ecom.view_models.DeliveryDetailsViewModel;
import com.example.ecom.view_models.MainActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryDetailListFragment extends Fragment implements DeliveryDetailsAdapter.OnClickListener {
    static final String BUNDLE_KEY_DELIVERY_DETAIL = "delivery_detail";
    private DeliveryDetailsAdapter mAdapter;
    private NavController navController;

    @BindView(R.id.recyclerViewAddress)
    RecyclerView recyclerView;

    public DeliveryDetailListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new DeliveryDetailsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_detail_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        DeliveryDetailsViewModel deliveryDetailsViewModel = ViewModelProviders.of(this).get(DeliveryDetailsViewModel.class);
        deliveryDetailsViewModel.setRepository(new Repository());
        deliveryDetailsViewModel.getAllDeliveryDetails();
        deliveryDetailsViewModel.getDeliveryDetailLiveData().observe(getViewLifecycleOwner(), deliveryDetails ->
                mAdapter.setList(deliveryDetails));
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.setActionBarTitle(getResources().getString(R.string.title_action_bar_delivery_details));
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAddAddressClick() {
        navController.navigate(R.id.action_deliveryDetailListFragment_to_addDeliveryDetailFragment);
    }

    @Override
    public void onEditAddressClick(DeliveryDetail deliveryDetail) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_DELIVERY_DETAIL, new DeliveryDetail(deliveryDetail));
        navController.navigate(R.id.action_deliveryDetailListFragment_to_editDeliveryDetailFragment, bundle);
    }

    @Override
    public void onDeliverHereClick(DeliveryDetail deliveryDetail) {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                navController.popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.fragment_cart).setVisible(false);
        menu.findItem(R.id.action_favorite).setVisible(false);
    }
}
