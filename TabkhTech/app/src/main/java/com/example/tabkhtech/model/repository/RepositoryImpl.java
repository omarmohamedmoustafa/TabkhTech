package com.example.tabkhtech.model.repository;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.local.MealLocalDataSource;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.remote.MealRemoteDataSource;
import com.example.tabkhtech.model.remote.NetworkCallback;

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
    public LiveData<List<Meal>> getLocalMeals() {
        return localDataSource.getAllMeals();
    }

    @Override
    public void insertMeal(Meal meal) {
        localDataSource.insertMeal(meal);

    }

    @Override
    public void getRandomMeal(NetworkCallback callback) {
        remoteDataSource.getRandomMeal(callback);
    }

    @Override
    public void getMealsByCategory(String category, NetworkCallback callback) {
        remoteDataSource.getMealsByCategory(category, callback);
    }

    @Override
    public void getMealsByIngredient(String ingredient, NetworkCallback callback) {
        remoteDataSource.getMealsByIngredient(ingredient, callback);
    }

    @Override
    public void getMealsByCountry(String country, NetworkCallback callback) {
        remoteDataSource.getMealsByCountry(country, callback);
    }

    @Override
    public void getMealById(String id, NetworkCallback callback) {
        remoteDataSource.getMealById(id, callback);
    }


    @Override
    public void deleteMeal(Meal meal) {
        localDataSource.deleteMeal(meal);
    }
}

