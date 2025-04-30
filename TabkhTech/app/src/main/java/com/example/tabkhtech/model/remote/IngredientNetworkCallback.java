package com.example.tabkhtech.model.remote;

import com.example.tabkhtech.model.pojos.Ingredient;

import java.util.List;

public interface IngredientNetworkCallback {
    public void onIngredientSuccess(List<Ingredient> ingredients);
    public void onFailure(String errorMessage);
}

