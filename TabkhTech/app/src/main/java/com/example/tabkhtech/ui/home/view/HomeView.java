package com.example.tabkhtech.ui.home.view;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.Meal;

import java.util.List;

public interface HomeView {
    void showRandomMeal(Meal meal);
    void showError(String errorMessage);
}
