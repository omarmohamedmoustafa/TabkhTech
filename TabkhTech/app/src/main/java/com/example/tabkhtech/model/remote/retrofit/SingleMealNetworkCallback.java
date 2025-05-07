package com.example.tabkhtech.model.remote.retrofit;

import com.example.tabkhtech.model.pojos.Meal;


public interface SingleMealNetworkCallback {
    public void onSingleMealSuccess(Meal meal);
    public void onFailure(String errorMessage);
}

