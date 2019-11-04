package com.example.ecom.utils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ecom.AppController;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Class to make Volley Requests
 */
public class VolleySeverRequest {
    //callback for volley responses
    private VolleyResponseCallback volleyResponseCallback;

    public VolleySeverRequest(){

    }

    public VolleySeverRequest(VolleyResponseCallback volleyResponseCallback) {
        this.volleyResponseCallback = volleyResponseCallback;
    }

    public void makePostRequest(String url, HashMap params, String requestTag){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseCallback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyResponseCallback.onFail(error);
                    }
                });

        jsonObjectRequest.setTag(requestTag);
        AppController.getInstance().getVolleyRequestQueue().add(jsonObjectRequest);
    }


    //make a json get request using url
    public void makeGetRequest(String url, String requestTag) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyResponseCallback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyResponseCallback.onFail(error);
                    }
                });

        jsonObjectRequest.setTag(requestTag);
        AppController.getInstance().getVolleyRequestQueue().add(jsonObjectRequest);
    }

    //function to cancel all volley requests with the given tag
    public void cancelRequest(Object tag) {
        AppController.getInstance().getVolleyRequestQueue().cancelAll(tag);
    }

    /*
    Volley response Callback Class
     */
    public interface VolleyResponseCallback {
        void onSuccess(JSONObject response);

        void onFail(VolleyError error);
    }
}
