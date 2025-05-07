package com.example.tabkhtech.ui.home.presenter;


import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.RecentMeal;
//import com.example.tabkhtech.model.pojos.User;
import com.example.tabkhtech.model.remote.retrofit.MealNetworkCallback;
import com.example.tabkhtech.model.remote.retrofit.SingleMealNetworkCallback;
import com.example.tabkhtech.ui.home.view.HomeView;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.repository.Repository;

import java.util.List;

public class HomePresenterImpl implements HomePresenter, MealNetworkCallback, SingleMealNetworkCallback {
    Repository repository;
    HomeView view;

    public HomePresenterImpl(Repository repository, HomeView view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void getMealOfTheDay() {
        repository.getRandomMeal(this);
    }

    @Override
    public LiveData<List<RecentMeal>> getAllRecentMeals(String userId, int limit) {
        return repository.getAllRecentMeals(userId, limit);
    }


    @Override
    public void onSuccess(List<Meal> meals) {
    }

    @Override
    public void onSingleMealSuccess(Meal meal) {
        if (meal != null) {
            view.showRandomMeal(meal);
        } else {
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        view.showError(errorMessage);
    }
}
