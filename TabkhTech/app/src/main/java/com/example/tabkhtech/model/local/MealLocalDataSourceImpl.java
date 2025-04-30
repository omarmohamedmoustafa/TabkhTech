package com.example.tabkhtech.model.local;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;

import java.util.List;

public class MealLocalDataSourceImpl implements MealLocalDataSource{
    private FavMealDAO faveDAO;
    private SchedMealDAO schedDAO;
    private RecentMealDAO recentDAO;

    private LiveData<List<FavMeal>> favouriteMeals;
    private LiveData<List<RecentMeal>> recentMeals;
    private LiveData<List<SchedMeal>> scheduledMeals;


    private static MealLocalDataSourceImpl instance = null;

    private MealLocalDataSourceImpl(Context context) {
        MealDatabase mealDatabase = MealDatabase.getInstance(context);
        faveDAO = mealDatabase.favMealDAO();
        schedDAO = mealDatabase.schedMealDAO();
        recentDAO = mealDatabase.recentMealDAO();
        favouriteMeals = faveDAO.getFavouriteMeals();
        recentMeals = recentDAO.getRecentMeals(5);
        scheduledMeals = schedDAO.getScheduledMeals();
    }

    public static MealLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MealLocalDataSourceImpl(context);
        }
        return instance;
    }


    @Override
    public LiveData<List<FavMeal>> getAllFavMeals() {
        return faveDAO.getFavouriteMeals();
    }

    @Override
    public LiveData<List<SchedMeal>> getAllSchedMeals() {
        return schedDAO.getScheduledMeals();
    }

    @Override
    public LiveData<List<RecentMeal>> getAllRecentMeals(int limit) {
        return recentDAO.getRecentMeals(limit);
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
    public LiveData<FavMeal> getFavMealById(String id) {
        return faveDAO.getFavouriteMealById(id);
    }

    @Override
    public LiveData<SchedMeal> getSchedMealById(String id) {
        return schedDAO.getScheduledMealById(id);
    }

    @Override
    public LiveData<RecentMeal> getRecentMealById(String id) {
        return recentDAO.getRecentMealById(id);
    }
}