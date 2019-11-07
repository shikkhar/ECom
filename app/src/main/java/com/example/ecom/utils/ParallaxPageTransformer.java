package com.example.ecom.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

public class ParallaxPageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {

        int pageWidth = view.getWidth();
        ImageView imageView = view.findViewById(0);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(1);

        } else if (position <= 1) { // [-1,1]

            imageView.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(1);
        }


    }
}