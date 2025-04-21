package com.example.tabkhtech.model.local;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.Meal;

import java.util.List;

public class MealLocalDataSourceImpl implements MealLocalDataSource{
    private MealDAO mealDAO;

    private LiveData<List<Meal>> allMeals;

    private static MealLocalDataSourceImpl instance = null;

    private MealLocalDataSourceImpl(Context context) {
        MealDatabase mealDatabase = MealDatabase.getInstance(context);
        mealDAO = mealDatabase.MealDAO();
        allMeals = mealDAO.getAllMeals();
    }
    public static MealLocalDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MealLocalDataSourceImpl(context);
        }
        return instance;
    }

    @Override
    public void insertMeal(Meal meal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDAO.insertMeal(meal);
            }
        }).start();
    }

    @Override
    public void deleteMeal(Meal meal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDAO.deleteMeal(meal);
            }
        }).start();
    }

    @Override
    public LiveData<List<Meal>> getAllMeals() {
        return mealDAO.getAllMeals();
    }
}
