package com.example.ecom.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecom.R;
import com.example.ecom.model.Address;
import com.example.ecom.model.DeliveryDetail;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliveryDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_DELIVERY_DETAIL = 0;
    public static final int VIEW_TYPE_ADD_ADDRESS = 1;
    private List<DeliveryDetail> deliveryDetailList;
    private OnClickListener onClick;

    public DeliveryDetailsAdapter() {
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_ADD_ADDRESS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_add_address, parent, false);
            return new AddAddressViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_delivery_details, parent, false);
            return new DeliveryDetailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_DELIVERY_DETAIL) {
            try {
                DeliveryDetailViewHolder viewHolder = (DeliveryDetailViewHolder) holder;
                DeliveryDetail deliveryDetail = deliveryDetailList.get(position);
                Address address = deliveryDetail.getAddress();
                viewHolder.userNameTextView.setText(deliveryDetail.getUserName());
                viewHolder.addressTextView.setText(address.getHouse() + "\n" +
                        address.getLocality() + "\n" +
                        address.getCity() + "\n" +
                        address.getState());

                if (deliveryDetail.getSelected()) {
                    if (viewHolder.cardView.getStrokeWidth() == 0)
                        viewHolder.cardView.setStrokeWidth(8);
                    viewHolder.viewGroupDeliveryButtons.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.cardView.setStrokeWidth(0);
                    viewHolder.viewGroupDeliveryButtons.setVisibility(View.GONE);
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return deliveryDetailList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return VIEW_TYPE_ADD_ADDRESS;

        return VIEW_TYPE_DELIVERY_DETAIL;
    }

    public void setClickListener(OnClickListener onClick) {
        this.onClick = onClick;
    }

    public void setList(List<DeliveryDetail> deliveryDetailList) {
        this.deliveryDetailList = deliveryDetailList;
        notifyDataSetChanged();
    }


    public class DeliveryDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardViewDeliveryDetail)
        MaterialCardView cardView;
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
                onClick.onDeliverHereClick(deliveryDetailList.get(position));
        }

        @OnClick(R.id.buttonEditAddress)
        void onEditAddressClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
                onClick.onEditAddressClick(deliveryDetailList.get(position));
        }

        public DeliveryDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> formatRecyclerView());

        }

        private void formatRecyclerView() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                for (int i = 0; i < getItemCount()-1; i++) {
                    if (i == position)
                        deliveryDetailList.get(i).setSelected(true);
                    else
                        deliveryDetailList.get(i).setSelected(false);
                }

                notifyDataSetChanged();
            }
        }
    }

    public class AddAddressViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.textViewAddAddress) TextView addAddressTextView;

        @OnClick(R.id.textViewAddAddress)
        void onAddAddressClick(){
            if(onClick != null)
                onClick.onAddAddressClick();
        }

        public AddAddressViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnClickListener{
        void onAddAddressClick();
        void onDeliverHereClick(DeliveryDetail deliveryDetail);
        void onEditAddressClick(DeliveryDetail deliveryDetail);
    }
}
