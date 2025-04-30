package com.example.tabkhtech.model.repository;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.local.MealLocalDataSource;
import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;
import com.example.tabkhtech.model.remote.MealRemoteDataSource;
import com.example.tabkhtech.model.remote.MealNetworkCallback;
import com.example.tabkhtech.model.remote.CategoryNetworkCallback;
import com.example.tabkhtech.model.remote.IngredientNetworkCallback;
import com.example.tabkhtech.model.remote.CountryNetworkCallback;
import com.example.tabkhtech.model.remote.SingleMealNetworkCallback;


import java.util.List;

public class RepositoryImpl implements Repository{

    private final MealLocalDataSource localDataSource;
    private final MealRemoteDataSource remoteDataSource;

    private static Repository instance = null;

    private RepositoryImpl(MealLocalDataSource localDataSource,
                            MealRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }
    public static  Repository getInstance(MealLocalDataSource localDataSource,
                                                      MealRemoteDataSource remoteDataSource) {
        if (instance == null) {
            instance = new RepositoryImpl(localDataSource, remoteDataSource);
        }
        return instance;
    }

    @Override
    public LiveData<List<FavMeal>> getAllFavMeals() {
        return localDataSource.getAllFavMeals();
    }

    @Override
    public LiveData<List<SchedMeal>> getAllSchedMeals() {
        return localDataSource.getAllSchedMeals();
    }

    @Override
    public LiveData<List<RecentMeal>> getAllRecentMeals(int limit) {
        return localDataSource.getAllRecentMeals(limit);
    }

    @Override
    public void insertRecentMeal(RecentMeal meal) {
        new Thread(() -> localDataSource.insertRecentMeal(meal)).start();
    }

    @Override
    public void deleteFavMeal(FavMeal meal) {
        new Thread(() -> localDataSource.deleteFavMeal(meal)).start();
    }

    @Override
    public void deleteSchedMeal(SchedMeal meal) {
        new Thread(() -> localDataSource.deleteSchedMeal(meal)).start();
    }

    @Override
    public void insertFavMeal(FavMeal meal) {
        new Thread(() -> localDataSource.insertFavMeal(meal)).start();
    }

    @Override
    public void insertSchedMeal(SchedMeal meal) {
        new Thread(() -> localDataSource.insertSchedMeal(meal)).start();
    }

    @Override
    public void deleteRecentMeal(RecentMeal meal) {
        new Thread(() -> localDataSource.deleteRecentMeal(meal)).start();
    }

    @Override
    public LiveData<FavMeal> getFavMealById(String id) {
        return localDataSource.getFavMealById(id);
    }

    @Override
    public LiveData<SchedMeal> getSchedMealById(String id) {
        return localDataSource.getSchedMealById(id);
    }

    @Override
    public LiveData<RecentMeal> getRecentMealById(String id) {
        return localDataSource.getRecentMealById(id);
    }

    @Override
    public void getRandomMeal(SingleMealNetworkCallback callback) {
        remoteDataSource.getRandomMeal(callback);
    }

    @Override
    public void getMealsByCategory(String category, MealNetworkCallback callback) {
        remoteDataSource.getMealsByCategory(category, callback);
    }

    @Override
    public void getMealsByIngredient(String ingredient, MealNetworkCallback callback) {
        remoteDataSource.getMealsByIngredient(ingredient, callback);
    }

    @Override
    public void getMealsByCountry(String country, MealNetworkCallback callback) {
        remoteDataSource.getMealsByCountry(country, callback);
    }

    @Override
    public void getMealById(String id, SingleMealNetworkCallback callback) {
        remoteDataSource.getMealById(id, callback);
    }

    @Override
    public void getAllCategories(CategoryNetworkCallback callback) {
        remoteDataSource.getAllCategories(callback);
    }

    @Override
    public void getAllCountries(CountryNetworkCallback callback) {
        remoteDataSource.getAllCountries(callback);
    }

    @Override
    public void getAllIngredients(IngredientNetworkCallback callback) {
        remoteDataSource.getAllIngredients(callback);
    }

}

