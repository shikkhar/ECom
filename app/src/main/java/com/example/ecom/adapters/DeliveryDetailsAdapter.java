package com.example.ecom.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecom.R;
import com.example.ecom.model.Address;
import com.example.ecom.model.DeliveryDetail;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliveryDetailsAdapter extends RecyclerView.Adapter<DeliveryDetailsAdapter.MyViewHolder> {

    private List<DeliveryDetail> deliveryDetailList;
    private OnButtonClick onButtonClick;

    public DeliveryDetailsAdapter() {
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_delivery_details, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DeliveryDetail deliveryDetail = deliveryDetailList.get(position);
        Address address = deliveryDetail.getAddress();
        holder.userNameTextView.setText(deliveryDetail.getUserName());
        holder.addressTextView.setText(address.getHouse() + "\n" +
                address.getLocality() + "\n" +
                address.getCity() + "\n" +
                address.getState());

        if(deliveryDetail.getSelected()){
            holder.viewGroupDeliveryButtons.setVisibility(View.VISIBLE);
            holder.radioButton.setChecked(true);
        }

        else{
            holder.viewGroupDeliveryButtons.setVisibility(View.GONE);
            holder.radioButton.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return deliveryDetailList.size();
    }

    public void setClickListener(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public void setList(List<DeliveryDetail> deliveryDetailList) {
        this.deliveryDetailList = deliveryDetailList;
        notifyDataSetChanged();
    }

    public interface OnButtonClick {
        void onDeliverHereClick(int position);

        void onEditAddressClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.radioButton)
        RadioButton radioButton;
        @BindView(R.id.textViewUserName)
        TextView userNameTextView;
        @BindView(R.id.textViewAddress)
        TextView addressTextView;
        @BindView(R.id.buttonDeliverHere)
        MaterialButton deliverHereButton;
        @BindView(R.id.buttonEditAddress)
        MaterialButton editAddressButton;
        @BindView(R.id.viewGroupDeliveryButtons)
        Group viewGroupDeliveryButtons;

        @OnClick(R.id.buttonDeliverHere)
        void onDeliverHereClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
                onButtonClick.onDeliverHereClick(position);
        }

        @OnClick(R.id.buttonEditAddress)
        void onEditAddressClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
                onButtonClick.onEditAddressClick(position);
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            /*itemView.setFocusableInTouchMode(true);
            itemView.setFocusable(true);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        viewGroupDeliveryButtons.setVisibility(View.VISIBLE);
                        radioButton.setChecked(true);
                    }
                    else{
                        viewGroupDeliveryButtons.setVisibility(View.GONE);
                        radioButton.setChecked(false);
                    }

                    notifyDataSetChanged();
                }
            });*/

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    for (int i = 0; i < getItemCount(); i++) {
                        if (i == position)
                            deliveryDetailList.get(i).setSelected(true);
                        else
                            deliveryDetailList.get(i).setSelected(false);
                    }

                    notifyDataSetChanged();
                }
            });
        }
    }
}
