package com.example.tabkhtech.random_meal.presenter;

import android.util.Log;

import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.remote.NetworkCallback;
import com.example.tabkhtech.model.repository.Repository;
import com.example.tabkhtech.random_meal.view.RandomMealView;

import java.util.List;

public class RandomMealPresenterImpl implements RandomMealPresenter , NetworkCallback {
    Repository repository;
    RandomMealView view;

    public RandomMealPresenterImpl(Repository repository, RandomMealView view) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void getRandomMeal() {
        repository.getRandomMeal(this);
    }

    @Override
    public void addToFavorites(Meal meal) {
        repository.insertMeal(meal);
    }

    @Override
    public void onSuccess(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            view.showRandomMeal(meals.get(0));
        } else {
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        Log.e("RandomMealPresenter", "Error fetching random meal: " + errorMessage);
        view.showError(errorMessage);
    }
}
