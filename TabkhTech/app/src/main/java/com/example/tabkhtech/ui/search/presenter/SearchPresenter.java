package com.example.tabkhtech.ui.search.presenter;

import com.example.tabkhtech.model.pojos.RecentMeal;

public interface SearchPresenter {
    void getAllIngredients();
    void getAllCountries();
    void getAllCategories();
    void getMealsByIngredient(String ingredient);
    void getMealsByCountry(String country);
    void getMealsByCategory(String category);
    void getMealById(String id);
    void insertRecentMeal(RecentMeal meal);
}
