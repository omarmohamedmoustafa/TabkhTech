package com.example.tabkhtech.ui.home.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.RecentMeal;
//import com.example.tabkhtech.model.pojos.User;

import java.util.List;

public interface HomePresenter {
    void getMealOfTheDay();
    LiveData<List<RecentMeal>> getAllRecentMeals(String userId, int limit);
}
