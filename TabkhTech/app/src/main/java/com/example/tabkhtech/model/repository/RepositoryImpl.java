package com.example.tabkhtech.model.repository;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.local.MealLocalDataSource;
import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;
//import com.example.tabkhtech.model.pojos.User;
//import com.example.tabkhtech.model.pojos.User;
import com.example.tabkhtech.model.remote.retrofit.MealRemoteDataSource;
import com.example.tabkhtech.model.remote.retrofit.MealNetworkCallback;
import com.example.tabkhtech.model.remote.retrofit.CategoryNetworkCallback;
import com.example.tabkhtech.model.remote.retrofit.IngredientNetworkCallback;
import com.example.tabkhtech.model.remote.retrofit.CountryNetworkCallback;
import com.example.tabkhtech.model.remote.retrofit.SingleMealNetworkCallback;


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
    public LiveData<List<FavMeal>> getAllFavMeals(String userId) {
        return localDataSource.getAllFavMeals(userId);
    }

    @Override
    public LiveData<List<SchedMeal>> getAllSchedMeals(String date, String userId) {
        return localDataSource.getAllSchedMeals(date, userId);
    }

    @Override
    public LiveData<List<RecentMeal>> getAllRecentMeals(String userId, int limit) {
        return localDataSource.getAllRecentMeals(userId,limit);
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

    @Override
    public LiveData<FavMeal> getFavMealById(String mealId, String userId) {
        return localDataSource.getFavMealById(mealId,userId);
    }

    @Override
    public LiveData<SchedMeal> getSchedMealById(String mealId, String userId) {
        return localDataSource.getSchedMealById(mealId,userId);
    }

    @Override
    public LiveData<RecentMeal> getRecentMealById(String mealId, String userId) {
        return localDataSource.getRecentMealById(mealId,userId);
    }

//    @Override
//    public void insertUser(User user) {
//        new Thread(() -> localDataSource.insertUser(user)).start();
//    }
//
//    @Override
//    public LiveData<User> getUserById(String userId) {
//        return localDataSource.getUserById(userId);
//    }
//
//    @Override
//    public void updateUser(User user) {
//        new Thread(() -> localDataSource.updateUser(user)).start();
//    }
//
//    @Override
//    public void deleteUser(String userId) {
//        new Thread(() -> localDataSource.deleteUser(userId)).start();
//    }

}

