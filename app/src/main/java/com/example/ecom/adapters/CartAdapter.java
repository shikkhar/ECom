package com.example.ecom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecom.R;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PRODUCT = 0;
    private static final int VIEW_TYPE_SUMMARY = 1;

    private List<CartProductDetail> cartProductDetailList = new ArrayList<>();
    private OnItemClickListener onItemClickListenerListener;
    private double cartSummary;
    private Context appContext;

    public CartAdapter(Context appContext) {
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (appContext == null)
            appContext = parent.getContext().getApplicationContext();

        if (viewType == VIEW_TYPE_PRODUCT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
            return new CartItemViewHolder(view);
        } else /*if (viewType == VIEW_TYPE_SUMMARY)*/ {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_order_summary, parent, false);
            return new SummaryViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.##");
        String rupeeSymbol = appContext.getString(R.string.Rs);
        if (getItemViewType(position) == VIEW_TYPE_PRODUCT) {

            try {
                //if (getItemViewType(position) == VIEW_TYPE_PRODUCT) {
                CartItemViewHolder viewHolder = (CartItemViewHolder) holder;

                CartProductDetail cartProductDetail = cartProductDetailList.get(position);
                Product product = cartProductDetail.getProduct();


                String finalPrice = df.format(product.getFinalPrice());
                String originalPrice = df.format(product.getOriginalPrice());
                String quantity = appContext.getString(R.string.quantity, String.valueOf(cartProductDetail.getQuantity()));

                Glide.with(appContext)
                        .load(product.getImagePaths().get(0))
                        .placeholder(R.drawable.ic_insert_photo_24dp)
                        .into(viewHolder.productImageView);

                viewHolder.titleTextView.setText(product.getTitle());
                viewHolder.finalPriceTextView.setText(rupeeSymbol + finalPrice);
                viewHolder.originalPriceTextView.setText(rupeeSymbol + originalPrice);
                viewHolder.updateQuantityButton.setText(quantity);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            //viewHolder.discountLabel.setText(discount);
        } else if (getItemViewType(position) == VIEW_TYPE_SUMMARY) {
            SummaryViewHolder viewHolder = (SummaryViewHolder) holder;

            viewHolder.totalMrpTextView.setText(rupeeSymbol+df.format(cartSummary));
            //TODO: Don't hardcode this
            viewHolder.shippingChargesTextView.setText("FREE");
            viewHolder.payableAmountTextView.setText(rupeeSymbol+df.format(cartSummary));
        }

    }

    @Override
    public int getItemCount() {
        if (cartProductDetailList == null || cartProductDetailList.size() == 0)
            return 0;
        return cartProductDetailList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return VIEW_TYPE_SUMMARY;
        else
            return VIEW_TYPE_PRODUCT;
    }

    public void setList(List<CartProductDetail> cartProductDetailList) {
        this.cartProductDetailList = cartProductDetailList;
        notifyDataSetChanged();
    }

    public void setClickListener(OnItemClickListener onItemClickListenerListener) {
        this.onItemClickListenerListener = onItemClickListenerListener;
    }

    public void setCartSummary(double cartSummary) {
        this.cartSummary = cartSummary;
    }

    public interface OnItemClickListener {
        void updateCartClick(int position, CartProductDetail cartProductDetail);

        //TODO: pass the selected product and remove that instead of passing position
        void onRemoveClick(int position);
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewProduct)
        ImageView productImageView;
        @BindView(R.id.textViewProductTitle)
        TextView titleTextView;
        @BindView(R.id.textViewFinalPrice)
        TextView finalPriceTextView;
        @BindView(R.id.textViewOriginalPrice)
        TextView originalPriceTextView;
        @BindView(R.id.buttonUpdateQuantity)
        Button updateQuantityButton;
        @BindView(R.id.buttonRemoveItem)
        Button removeButton;
        /*@BindView(R.id.labelDiscount)
        Button discountLabel;*/

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            updateQuantityButton.setOnClickListener(v -> {
                int position = getAdapterPosition();

                if (onItemClickListenerListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListenerListener.updateCartClick(position, cartProductDetailList.get(position));
                }
            });

            removeButton.setOnClickListener(v -> {
                int position = getAdapterPosition();

                if (onItemClickListenerListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListenerListener.onRemoveClick(position);
                }
            });
        }
    }

    class SummaryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewTotalMrp)
        TextView totalMrpTextView;
        @BindView(R.id.textViewShippingCharges)
        TextView shippingChargesTextView;
        @BindView(R.id.textViewPayableAmount)
        TextView payableAmountTextView;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
