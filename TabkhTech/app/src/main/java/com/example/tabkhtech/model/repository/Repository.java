package com.example.tabkhtech.model.repository;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.remote.NetworkCallback;

import java.util.List;

public interface Repository {
    LiveData<List<Meal>> getLocalMeals();

    void insertMeal(Meal meal);

//    void getAllMealsFromRemoteSource(NetworkCallback callback);

    void getRandomMeal(NetworkCallback callback);
    void getMealsByCategory(String category, NetworkCallback callback);
    void getMealsByIngredient(String ingredient, NetworkCallback callback);
    void getMealsByCountry(String country, NetworkCallback callback);
    void getMealById(String id, NetworkCallback callback);

    void deleteMeal(Meal meal);
}
