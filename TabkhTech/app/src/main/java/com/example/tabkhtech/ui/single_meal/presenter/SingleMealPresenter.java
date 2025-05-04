package com.example.tabkhtech.ui.single_meal.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;
/*
*
    LiveData<List<FavMeal>> getAllFavMeals(String userId);
    LiveData<List<SchedMeal>> getAllSchedMeals(String date, String userId);
    LiveData<List<RecentMeal>> getAllRecentMeals(String userId, int limit);

    void insertFavMeal(FavMeal meal);
    void insertSchedMeal(SchedMeal meal);
    void insertRecentMeal(RecentMeal meal);

    void deleteFavMeal(FavMeal meal);
    void deleteSchedMeal(SchedMeal meal);
    void deleteRecentMeal(RecentMeal meal);

    void getRandomMeal(SingleMealNetworkCallback callback);
    void getMealsByCategory(String category, MealNetworkCallback callback);
    void getMealsByIngredient(String ingredient, MealNetworkCallback callback);
    void getMealsByCountry(String country, MealNetworkCallback callback);
    void getMealById(String id, SingleMealNetworkCallback callback);

    void getAllCategories(CategoryNetworkCallback callback);
    void getAllCountries(CountryNetworkCallback callback);
    void getAllIngredients(IngredientNetworkCallback callback);

    LiveData<FavMeal> getFavMealById(String mealId, String userId);
    LiveData<SchedMeal> getSchedMealById(String mealId, String userId);
    LiveData<RecentMeal> getRecentMealById(String mealId, String userId);

    void insertUser(User user);
    LiveData<User> getUserById(String userId);
    void updateUser(User user);
    void deleteUser(String userId);
*
* */
public interface SingleMealPresenter {

    void insertFavMeal(FavMeal meal);
    boolean isMealFavorite(String mealId);
    void deleteFavMeal(FavMeal meal);
    LiveData<FavMeal> getFavMealById(String mealId, String userId);

    void insertRecentMeal(RecentMeal meal);
    void insertSchedMeal(SchedMeal meal);
}
