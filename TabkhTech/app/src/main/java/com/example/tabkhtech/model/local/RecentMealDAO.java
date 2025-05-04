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
    @Query("SELECT * FROM recent_meals WHERE userId = :userId ORDER BY lastOpened DESC LIMIT :limit")
    LiveData<List<RecentMeal>> getRecentMeals(String userId, int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecentMeal(RecentMeal meal);

    @Query("SELECT * FROM recent_meals WHERE idMeal = :id AND userId = :userId")
    LiveData<RecentMeal> getRecentMealById(String id, String userId);

    @Delete
    void deleteRecentMeal(RecentMeal meal);
}