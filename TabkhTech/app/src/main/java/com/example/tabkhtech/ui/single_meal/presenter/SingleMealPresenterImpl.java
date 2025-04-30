package com.example.tabkhtech.ui.single_meal.presenter;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.remote.MealNetworkCallback;
import com.example.tabkhtech.model.remote.SingleMealNetworkCallback;
import com.example.tabkhtech.model.repository.Repository;
import com.example.tabkhtech.ui.single_meal.view.SingleMealView;

import java.util.List;

public class SingleMealPresenterImpl implements SingleMealPresenter, SingleMealNetworkCallback {
    Repository repository;
    SingleMealView view;
    public SingleMealPresenterImpl(Repository repository, SingleMealView view) {
        this.repository = repository;
        this.view = view;
    }


    @Override
    public void addToFavorites(FavMeal meal) {
        repository.insertFavMeal(meal);
    }

    @Override
    public boolean isMealFavorite(String mealId) {
        LiveData<FavMeal> favMealLiveData = repository.getFavMealById(mealId);
        return favMealLiveData.getValue() != null;
    }

    @Override
    public void removeFromFavorites(FavMeal meal) {
        repository.deleteFavMeal(meal);
    }

    @Override
    public LiveData<FavMeal> getFavMealById(String mealId) {
        return repository.getFavMealById(mealId);
    }



    @Override
    public void onSingleMealSuccess(Meal meal) {
        view.showMeal(meal);
    }

    @Override
    public void onFailure(String errorMessage) {
        view.showError(errorMessage);
    }

    @Override
    public void insertRecentMeal(RecentMeal meal) {
        new Thread(() -> repository.insertRecentMeal(meal)).start();
    }
}
