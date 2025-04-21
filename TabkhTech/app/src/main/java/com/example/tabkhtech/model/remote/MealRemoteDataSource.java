package com.example.tabkhtech.model.remote;

public interface MealRemoteDataSource {
    void getRandomMeal(NetworkCallback callback);
    void getMealsByCategory(String category, NetworkCallback callback);
    void getMealsByIngredient(String ingredient, NetworkCallback callback);
    void getMealsByCountry(String country, NetworkCallback callback);
    void getMealById(String id, NetworkCallback callback);
}
