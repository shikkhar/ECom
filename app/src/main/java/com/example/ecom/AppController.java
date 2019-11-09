package com.example.ecom;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Field;

public class AppController extends Application {

    private RequestQueue mRequestQueue;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static AppController getInstance(){
        return mInstance;
    }

    public synchronized RequestQueue getVolleyRequestQueue(){
        if(mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return mRequestQueue;
    }

}
