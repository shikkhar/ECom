package com.example.ecom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class QuantityCustomView extends ConstraintLayout {
    private ImageView decreaseQty, increaseQty;
    private TextView quantity;

    public QuantityCustomView(Context context) {
        super(context);
        init(null, context);
    }

    public QuantityCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);

    }

    public QuantityCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }



    private void init(@Nullable AttributeSet attrs, Context context) {
        inflate(context, R.layout.custom_view, this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QuantityCustomView);
        decreaseQty = findViewById(R.id.imageViewDecreaseQty);
        increaseQty = findViewById(R.id.imageViewIncreaseQty);
        quantity = findViewById(R.id.textViewQty);
        decreaseQty.setImageDrawable(ta.getDrawable(R.styleable.QuantityCustomView_decreaseQtyImageSrc));
        increaseQty.setImageDrawable(ta.getDrawable(R.styleable.QuantityCustomView_increaseQtyImageSrc));
        quantity.setTextColor(ta.getColor(R.styleable.QuantityCustomView_textColor, Color.WHITE));

        ta.recycle();

        decreaseQty.setOnClickListener(v -> {
            int qty = Integer.valueOf(quantity.getText().toString());
            if(qty == 1)
                return;
            quantity.setText(String.valueOf(qty - 1));
        });

        increaseQty.setOnClickListener(v -> {
            int qty = Integer.valueOf(quantity.getText().toString());
            if(qty == 10)
                return;
            quantity.setText(String.valueOf(qty + 1));
        });
    }

    public int getQuantity(){
        return Integer.valueOf(quantity.getText().toString());
    }

    public void setQuantity(int qty){
        quantity.setText(String.valueOf(qty));
    }
}
