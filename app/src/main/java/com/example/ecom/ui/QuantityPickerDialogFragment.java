package com.example.ecom.ui;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.ecom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.ecom.ui.CartFragment.BUNDLE_KEY_LIST_POSITION;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuantityPickerDialogFragment extends DialogFragment implements View.OnClickListener {
    private ValueChangeCallback valueChangeCallback;
    private int position;

    @BindView(R.id.textViewNumber1) TextView textView1;
    @BindView(R.id.textViewNumber2) TextView textView2;
    @BindView(R.id.textViewNumber3) TextView textView3;
    @BindView(R.id.textViewNumber4) TextView textView4;
    @BindView(R.id.textViewNumber5) TextView textView5;
    @BindView(R.id.textViewNumber6) TextView textView6;
    @BindView(R.id.textViewNumber7) TextView textView7;
    @BindView(R.id.textViewNumber8) TextView textView8;
    @BindView(R.id.textViewNumber9) TextView textView9;
    @BindView(R.id.textViewNumber10) TextView textView10;

    public QuantityPickerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(BUNDLE_KEY_LIST_POSITION)) {
            position = bundle.getInt(BUNDLE_KEY_LIST_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quanity_picker_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachToParentFragment(getParentFragment());
        ButterKnife.bind(this, view);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);
        textView7.setOnClickListener(this);
        textView8.setOnClickListener(this);
        textView9.setOnClickListener(this);
        textView10.setOnClickListener(this);
    }



    /* @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        attachToParentFragment(getParentFragment());

        final NumberPicker numberPicker = new NumberPicker(getActivity());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> valueChangeCallback.onValueChange(position, newVal));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        *//*builder.setTitle("Choose Value");
        builder.setMessage("Choose a number :");
*//*
     *//*builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });
*//*
        builder.setView(numberPicker);
        return builder.create();
    }*/

    private void attachToParentFragment(Fragment parentFragment) {
        try {
            valueChangeCallback = (CartFragment) parentFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    parentFragment.toString() + " must implement QuantityPickerDialogFragment.ValueChangeCallback");
        }
    }

    interface ValueChangeCallback {
        void onValueChange(int position, int value);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewNumber1:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView1.getText().toString()));
                break;
            case R.id.textViewNumber2:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView2.getText().toString()));
                break;
            case R.id.textViewNumber3:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView3.getText().toString()));
                break;
            case R.id.textViewNumber4:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView4.getText().toString()));
                break;
            case R.id.textViewNumber5:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView5.getText().toString()));
                break;
            case R.id.textViewNumber6:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView6.getText().toString()));
                break;
            case R.id.textViewNumber7:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView7.getText().toString()));
                break;
            case R.id.textViewNumber8:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView8.getText().toString()));
                break;
            case R.id.textViewNumber9:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView9.getText().toString()));
                break;
            case R.id.textViewNumber10:
                valueChangeCallback.onValueChange(position, Integer.valueOf(textView10.getText().toString()));
                break;
        }
    }
}
