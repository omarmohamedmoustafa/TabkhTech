package com.example.tabkhtech.random_meal.presenter;

import com.example.tabkhtech.model.pojos.Meal;

public interface RandomMealPresenter {
    void getRandomMeal();
    void addToFavorites(Meal meal);
}
