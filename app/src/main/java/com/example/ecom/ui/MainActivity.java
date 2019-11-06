package com.example.ecom.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.TaskStackBuilder;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ecom.R;
import com.example.ecom.utils.CountDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
        /*witch (item.getItemId()) {
            case R.id.cartFragment:
                //return super.onOptionsItemSelected(item);
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                return NavigationUI.onNavDestinationSelected(item, navController)
                        || super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //setCartCount(this,menu, "2");
        return true;
    }

    /*public void setCartCount(Context context,Menu menu, String count) {
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCartCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_count, badge);
    }*/
}
