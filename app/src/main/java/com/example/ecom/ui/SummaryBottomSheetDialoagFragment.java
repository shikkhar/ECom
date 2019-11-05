package com.example.ecom.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecom.R;
import com.example.ecom.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.ecom.ui.CartFragment.BUNDLE_KEY_CART_TOTAL;

public class SummaryBottomSheetDialoagFragment extends BottomSheetDialogFragment {
    private double cartTotal;

    @BindView(R.id.textViewTotalMrp) TextView totalMrpTextView;
    @BindView(R.id.textViewShippingCharges) TextView shippingChargesTextView;
    @BindView(R.id.textViewPayableAmount) TextView payableAmountTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(BUNDLE_KEY_CART_TOTAL)){
            cartTotal = bundle.getFloat(BUNDLE_KEY_CART_TOTAL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        totalMrpTextView.setText(getString(R.string.Rs) + Utils.formatDouble(cartTotal));
        shippingChargesTextView.setText("FREE");
        payableAmountTextView.setText(getString(R.string.Rs) + Utils.formatDouble(cartTotal));
    }
}
