package com.example.tabkhtech.model.local;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;

import java.util.List;

public interface MealLocalDataSource {
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
}
