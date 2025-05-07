package com.example.tabkhtech.ui.detailed_meal.view;

import com.example.tabkhtech.model.pojos.Meal;

public interface SingleMealView {
    public void showMeal(Meal meal);
    void showError(String errorMessage);
}
