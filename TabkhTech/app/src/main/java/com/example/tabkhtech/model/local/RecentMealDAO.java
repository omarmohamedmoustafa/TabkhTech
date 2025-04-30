package com.example.tabkhtech.model.local;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tabkhtech.model.pojos.RecentMeal;

import java.util.List;

@Dao
public interface RecentMealDAO {
    @Query("SELECT * FROM recent_meals ORDER BY lastOpened DESC LIMIT :limit")
    LiveData<List<RecentMeal>> getRecentMeals(int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecentMeal(RecentMeal meal);

    @Query("SELECT * FROM recent_meals WHERE idMeal = :id")
    LiveData<RecentMeal> getRecentMealById(String id);

    @Delete
    void deleteRecentMeal(RecentMeal meal);
}