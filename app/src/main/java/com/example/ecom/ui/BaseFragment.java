package com.example.ecom.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.example.ecom.R;
import com.example.ecom.utils.CountDrawable;

public class BaseFragment extends Fragment {

    boolean checkConnectivity(Context appContext) {
        boolean isConnected = false;
        if(appContext != null) {
            ConnectivityManager cm =
                    (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }

        return isConnected;
    }

    void setCartCount(Context context, Menu menu, String count) {
        MenuItem menuItem = menu.findItem(R.id.fragment_cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_count, badge);
    }
}
