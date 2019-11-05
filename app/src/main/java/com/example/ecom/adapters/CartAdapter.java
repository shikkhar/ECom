package com.example.ecom.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecom.QuantityCustomView;
import com.example.ecom.R;
import com.example.ecom.model.CartProductDetail;
import com.example.ecom.model.Product;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemViewHolder> {
    /*private static final int VIEW_TYPE_PRODUCT = 0;
    private static final int VIEW_TYPE_SUMMARY = 1;*/

    private List<CartProductDetail> cartProductDetailList;
    private double cartSummary;
    private Fragment containerFragment;

    public CartAdapter(Fragment containerFragment) {
        this.containerFragment = containerFragment;
    }

    @NonNull
    @Override
    public CartAdapter.CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //if (viewType == VIEW_TYPE_PRODUCT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
            return new CartItemViewHolder(view);
       /* } else if (viewType == VIEW_TYPE_SUMMARY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_order_summary, parent, false);
            return new SummaryViewHolder(view);
        }*/

        //return null;
    }


    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartItemViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.##");
        //if (getItemViewType(position) == VIEW_TYPE_PRODUCT) {
            CartItemViewHolder viewHolder = (CartItemViewHolder) holder;

            CartProductDetail cartProductDetail = cartProductDetailList.get(position);
            Product product = cartProductDetail.getProduct();


            String finalPrice = df.format(product.getFinalPrice());
            String originalPrice = df.format(product.getOriginalPrice());
            String rupeeSymbol = containerFragment.getString(R.string.Rs);

            if (containerFragment != null)
                Glide.with(containerFragment)
                        .load(product.getImagePath())
                        .centerCrop()
                        .placeholder(R.drawable.ic_search_dark_24dp)
                        .into(viewHolder.productImageView);

            viewHolder.titleTextView.setText(product.getTitle());
            viewHolder.finalPriceTextView.setText(rupeeSymbol + finalPrice);
            viewHolder.originalPriceTextView.setText(rupeeSymbol + originalPrice);
            viewHolder.quantityCustomView.setQuantity(cartProductDetail.getQuantity());
        /*} else if (getItemViewType(position) == VIEW_TYPE_SUMMARY) {
            SummaryViewHolder viewHolder = (SummaryViewHolder) holder;

            viewHolder.totalMrpTextView.setText(df.format(cartSummary));
            //TODO: Don't hardcode this
            viewHolder.shippingChargesTextView.setText("FREE");
            viewHolder.payableAmountTextView.setText(df.format(cartSummary));
        }*/

    }

    @Override
    public int getItemCount() {
        return cartProductDetailList.size();
    }

    /*@Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return VIEW_TYPE_SUMMARY;
        else
            return VIEW_TYPE_PRODUCT;
    }*/

    public void setList(List<CartProductDetail> cartProductDetailList) {
        this.cartProductDetailList = cartProductDetailList;
        notifyDataSetChanged();
    }

    public void setCartSummary(double cartSummary) {
        this.cartSummary = cartSummary;
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
        @BindView(R.id.customViewQuantity)
        QuantityCustomView quantityCustomView;


        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
