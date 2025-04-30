package com.example.tabkhtech.model.repository;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;
import com.example.tabkhtech.model.remote.CategoryNetworkCallback;
import com.example.tabkhtech.model.remote.CountryNetworkCallback;
import com.example.tabkhtech.model.remote.IngredientNetworkCallback;
import com.example.tabkhtech.model.remote.MealNetworkCallback;
import com.example.tabkhtech.model.remote.SingleMealNetworkCallback;

import java.util.List;

public interface Repository {

    LiveData<List<FavMeal>> getAllFavMeals();
    LiveData<List<SchedMeal>> getAllSchedMeals();
    LiveData<List<RecentMeal>> getAllRecentMeals(int limit);

    void insertFavMeal(FavMeal meal);
    void insertSchedMeal(SchedMeal meal);
    void insertRecentMeal(RecentMeal meal);

    void deleteFavMeal(FavMeal meal);
    void deleteSchedMeal(SchedMeal meal);
    void deleteRecentMeal(RecentMeal meal);

    LiveData<FavMeal> getFavMealById(String id);
    LiveData<SchedMeal> getSchedMealById(String id);
    LiveData<RecentMeal> getRecentMealById(String id);

    void getRandomMeal(SingleMealNetworkCallback callback);
    void getMealsByCategory(String category, MealNetworkCallback callback);
    void getMealsByIngredient(String ingredient, MealNetworkCallback callback);
    void getMealsByCountry(String country, MealNetworkCallback callback);
    void getMealById(String id, SingleMealNetworkCallback callback);

    void getAllCategories(CategoryNetworkCallback callback);
    void getAllCountries(CountryNetworkCallback callback);
    void getAllIngredients(IngredientNetworkCallback callback);

}
