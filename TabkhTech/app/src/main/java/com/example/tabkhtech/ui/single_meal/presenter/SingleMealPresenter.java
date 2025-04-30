package com.example.tabkhtech.ui.single_meal.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.RecentMeal;

public interface SingleMealPresenter {
    void addToFavorites(FavMeal meal);
    boolean isMealFavorite(String mealId);
    void removeFromFavorites(FavMeal meal);
    LiveData<FavMeal> getFavMealById(String mealId);
    void insertRecentMeal(RecentMeal meal);
}
