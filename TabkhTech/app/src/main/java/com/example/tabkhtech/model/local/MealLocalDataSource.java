package com.example.tabkhtech.model.local;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.Meal;

import java.util.List;

public interface MealLocalDataSource {
    void insertMeal(Meal meal);
    void deleteMeal(Meal meal);
    LiveData<List<Meal>> getAllMeals();

}
