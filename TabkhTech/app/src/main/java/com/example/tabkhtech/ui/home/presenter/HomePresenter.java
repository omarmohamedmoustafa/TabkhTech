package com.example.tabkhtech.ui.home.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.RecentMeal;

import java.util.List;

/*
*  LiveData<List<FavMeal>> getAllFavMeals(String userId);
    LiveData<List<SchedMeal>> getAllSchedMeals(String date, String userId);
    LiveData<List<RecentMeal>> getAllRecentMeals(String userId, int limit);

    void insertFavMeal(FavMeal meal);
    void insertSchedMeal(SchedMeal meal);
    void insertRecentMeal(RecentMeal meal);

    void deleteFavMeal(FavMeal meal);
    void deleteSchedMeal(SchedMeal meal);
    void deleteRecentMeal(RecentMeal meal);
* */
public interface HomePresenter {
    void getRandomMeal();
    LiveData<List<RecentMeal>> getAllRecentMeals(String userId, int limit);
}
