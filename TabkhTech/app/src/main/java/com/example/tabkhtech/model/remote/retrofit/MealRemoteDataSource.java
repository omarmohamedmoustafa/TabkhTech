package com.example.tabkhtech.model.remote.retrofit;

public interface MealRemoteDataSource {
    void getRandomMeal(SingleMealNetworkCallback callback);
    void getMealsByCategory(String category, MealNetworkCallback callback);
    void getMealsByIngredient(String ingredient, MealNetworkCallback callback);
    void getMealsByCountry(String country, MealNetworkCallback callback);
    void getMealById(String id, SingleMealNetworkCallback callback);
    void getAllCategories(CategoryNetworkCallback callback);
    void getAllCountries(CountryNetworkCallback callback);
    void getAllIngredients(IngredientNetworkCallback callback);
}
