package com.example.ecom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
import butterknife.OnClick;

public class ProductListAdapter extends ListAdapter<Product, ProductListAdapter.MyViewHolder>{

    private Context appContext;
    private List<Product> productList;
    private OnItemClickListener onItemClickListener;

    public ProductListAdapter(Context appContext) {
        super(DIFF_CALLBACK);
        this.appContext = appContext;
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
                    oldItem.getImagePaths().equals(newItem.getImagePaths()) &&
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
        String rupeeSymbol = appContext.getString(R.string.Rs);

        //TODO: replace image path with thumbnail image
        if (appContext != null)
            Glide.with(appContext)
                    .load(product.getImagePaths().get(0))
                    .placeholder(R.drawable.ic_insert_photo_dark_24dp)
                    .into(holder.productImageView);

        holder.titleTextView.setText(product.getTitle());
        holder.shortDescrTextView.setText(product.getShortDescription());
        holder.finalPriceTextView.setText(rupeeSymbol + finalPrice);
        holder.originalPriceTextView.setText(rupeeSymbol + originalPrice);
        //TODO: replace with a string resource
        holder.discountTextView.setText(product.getDiscount() + "% Off");

        if(product.isFavorite()){
            holder.favoriteImageView.setVisibility(View.VISIBLE);
            holder.grayFavoriteImageView.setVisibility(View.GONE);
        }
        else{
            holder.favoriteImageView.setVisibility(View.GONE);
            holder.grayFavoriteImageView.setVisibility(View.VISIBLE);
        }
    }

    public void setList(List<Product> productList){
        this.productList = productList;
        submitList(productList);
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Product selectedProduct);
        void onFavIconClick(int position, Product selectedProduct);
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
        @BindView(R.id.imageViewFavoriteGray)
        ImageView grayFavoriteImageView;
        @BindView(R.id.imageViewFavorite)
        ImageView favoriteImageView;

        @OnClick({R.id.imageViewFavoriteGray, R.id.imageViewFavorite})
        void onFavIconClick(){
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = getItem(position);
                    if(product.isFavorite()){
                        this.favoriteImageView.setVisibility(View.GONE);
                        this.grayFavoriteImageView.setVisibility(View.VISIBLE);
                        //product.setFavorite(!product.isFavorite());
                    }
                    else{
                        this.favoriteImageView.setVisibility(View.VISIBLE);
                        this.grayFavoriteImageView.setVisibility(View.GONE);
                    }
                    productList.get(position).setFavorite(!product.isFavorite());
                    submitList(productList);
                    onItemClickListener.onFavIconClick(position, product);
                }
            }
        }

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }


}
