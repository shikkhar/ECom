package com.example.ecom.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecom.R;
import com.example.ecom.model.Product;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListAdapter extends ListAdapter<Product, ProductListAdapter.MyViewHolder>{

    private Fragment containerFragment;
    private List<Product> productList;
    private OnItemClickListener onItemClickListener;

    public ProductListAdapter(Fragment containerFragment) {
        super(DIFF_CALLBACK);
        this.containerFragment = containerFragment;
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId() &&
                    oldItem.getDiscount() == newItem.getDiscount() &&
                    oldItem.getFinalPrice() == newItem.getFinalPrice() &&
                    oldItem.getImagePath().equals(newItem.getImagePath()) &&
                    oldItem.getLongDescription().equals(newItem.getLongDescription()) &&
                    oldItem.getOriginalPrice() == newItem.getOriginalPrice() &&
                    oldItem.getShortDescription().equals(newItem.getShortDescription()) &&
                    oldItem.getTitle().equals(newItem.getTitle());
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_products, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = getItem(position);
        DecimalFormat df = new DecimalFormat("#.##");

        String finalPrice = df.format(product.getFinalPrice());
        String originalPrice = df.format(product.getOriginalPrice());
        String rupeeSymbol = containerFragment.getString(R.string.Rs);

        if (containerFragment != null)
            Glide.with(containerFragment)
                    .load(product.getImagePath())
                    .centerCrop()
                    .placeholder(R.drawable.ic_search_dark_24dp)
                    .into(holder.productImageView);

        holder.titleTextView.setText(product.getTitle());
        holder.shortDescrTextView.setText(product.getShortDescription());
        holder.finalPriceTextView.setText(rupeeSymbol + finalPrice);
        holder.originalPriceTextView.setText(rupeeSymbol + originalPrice);
        //TODO: replace with a string resource
        holder.discountTextView.setText(product.getDiscount() + "% Off");
    }

    public void setList(List<Product> productList){
        this.productList = productList;
        submitList(productList);
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(Product selectedProduct);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewProduct)
        ImageView productImageView;
        @BindView(R.id.textViewProductTitle)
        TextView titleTextView;
        @BindView(R.id.textViewShortDescription)
        TextView shortDescrTextView;
        @BindView(R.id.textViewFinalPrice)
        TextView finalPriceTextView;
        @BindView(R.id.textViewOriginalPrice)
        TextView originalPriceTextView;
        @BindView(R.id.textViewDiscount)
        TextView discountTextView;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.OnItemClick(getItem(position));
                    }
                }
            });
        }
    }

}
