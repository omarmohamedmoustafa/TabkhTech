package com.example.tabkhtech.random_meal.view;

import com.example.tabkhtech.model.pojos.Meal;

import java.util.List;

public interface RandomMealView {
    void showRandomMeal(Meal meal);
    void showError(String errorMessage);
}
