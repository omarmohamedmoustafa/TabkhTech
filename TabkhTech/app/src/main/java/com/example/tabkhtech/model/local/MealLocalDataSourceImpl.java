package com.example.tabkhtech.model.local;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;
//import com.example.tabkhtech.model.pojos.User;

import java.util.List;

public class MealLocalDataSourceImpl implements MealLocalDataSource {
    private FavMealDAO faveDAO;
    private SchedMealDAO schedDAO;
    private RecentMealDAO recentDAO;
//    private UserDAO userDAO;
    private static MealLocalDataSourceImpl instance = null;

    private MealLocalDataSourceImpl(Context context) {
        TabkhTeckDatabase mealDatabase = TabkhTeckDatabase.getInstance(context);
        faveDAO = mealDatabase.favMealDAO();
        schedDAO = mealDatabase.schedMealDAO();
        recentDAO = mealDatabase.recentMealDAO();
//        userDAO = mealDatabase.userDAO();
    }

    public static MealLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MealLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public LiveData<List<FavMeal>> getAllFavMeals(String userId) {
        return faveDAO.getFavouriteMeals(userId);
    }

    @Override
    public LiveData<List<SchedMeal>> getAllSchedMeals(String date, String userId) {
        return schedDAO.getScheduledMeals(date, userId);
    }

    @Override
    public LiveData<List<RecentMeal>> getAllRecentMeals(String userId, int limit) {
        return recentDAO.getRecentMeals(userId, limit);
    }

    @Override
    public void insertFavMeal(FavMeal meal) {
        faveDAO.insertFavouriteMeal(meal);
    }

    @Override
    public void insertSchedMeal(SchedMeal meal) {
        schedDAO.insertScheduledMeal(meal);
    }

    @Override
    public void insertRecentMeal(RecentMeal meal) {
        recentDAO.insertRecentMeal(meal);
    }

    @Override
    public void deleteFavMeal(FavMeal meal) {
        faveDAO.deleteFavouriteMeal(meal);
    }

    @Override
    public void deleteSchedMeal(SchedMeal meal) {
        schedDAO.deleteScheduledMeal(meal);
    }

    @Override
    public void deleteRecentMeal(RecentMeal meal) {
        recentDAO.deleteRecentMeal(meal);
    }

    @Override
    public LiveData<FavMeal> getFavMealById(String id, String userId) {
        return faveDAO.getFavouriteMealById(id, userId);
    }

    @Override
    public LiveData<SchedMeal> getSchedMealById(String id, String userId) {
        return schedDAO.getScheduledMealById(id, userId);
    }

    @Override
    public LiveData<RecentMeal> getRecentMealById(String id, String userId) {
        return recentDAO.getRecentMealById(id, userId);
    }

//    @Override
//    public void insertUser(User user) {
//        userDAO.insertUser(user);
//    }
//
//    @Override
//    public LiveData<User> getUserById(String userId) {
//        return userDAO.getUserById(userId);
//    }
//
//    @Override
//    public void updateUser(User user) {
//        userDAO.updateUser(user);
//    }
//
//    @Override
//    public void deleteUser(String userId) {
//        userDAO.deleteUser(userId);
//    }
}