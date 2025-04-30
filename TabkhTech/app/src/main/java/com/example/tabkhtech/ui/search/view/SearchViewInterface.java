package com.example.tabkhtech.ui.search.view;

import com.example.tabkhtech.model.pojos.Category;
import com.example.tabkhtech.model.pojos.Country;
import com.example.tabkhtech.model.pojos.Ingredient;
import com.example.tabkhtech.model.pojos.Meal;

import java.util.List;

public interface SearchViewInterface {
    void showCountries(List<Country> countries);
    void showIngredients(List<Ingredient> ingredients);
    void showCategories(List<Category> categories);
    void showMeals(List<Meal> meals);
    void showMealDetails(Meal meal);
    void showError(String errorMessage);
}
