package com.example.ecom.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.ecom.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context appContext;
    private List<String> imageUrls;

    public ViewPagerAdapter(Context appContext, List<String> imageUrls) {
        this.appContext = appContext;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(appContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setBackgroundResource(R.color.backgroundLight);
        Glide.with(appContext)
                .load(imageUrls.get(position))
                .placeholder(R.drawable.ic_insert_photo_24dp)
                .into(imageView);

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
