package com.example.tabkhtech.model.remote;

import com.example.tabkhtech.model.pojos.Meal;


public interface SingleMealNetworkCallback {
    public void onSingleMealSuccess(Meal meal);
    public void onFailure(String errorMessage);
}

