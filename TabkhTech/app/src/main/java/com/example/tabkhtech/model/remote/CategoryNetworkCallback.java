package com.example.tabkhtech.model.remote;

import com.example.tabkhtech.model.pojos.Category;

import java.util.List;

public interface CategoryNetworkCallback {
    public void onCategorySuccess(List<Category> categories);
    public void onFailure(String errorMessage);
}

