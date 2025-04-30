package com.example.tabkhtech.ui.home.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.RecentMeal;

import java.util.List;

public interface HomePresenter {
    void getRandomMeal();
    LiveData<List<RecentMeal>> getRecentlyViewedMeals(int limit);
}
