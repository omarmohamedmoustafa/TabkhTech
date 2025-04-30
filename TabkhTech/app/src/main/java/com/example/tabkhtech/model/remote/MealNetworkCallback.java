package com.example.tabkhtech.model.remote;

import com.example.tabkhtech.model.pojos.Meal;

import java.util.List;

public interface MealNetworkCallback {
    public void onSuccess(List<Meal> meals);
    public void onFailure(String errorMessage);
}

