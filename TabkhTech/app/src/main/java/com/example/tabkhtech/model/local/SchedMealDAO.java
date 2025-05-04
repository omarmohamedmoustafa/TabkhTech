package com.example.tabkhtech.model.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tabkhtech.model.pojos.SchedMeal;

import java.util.List;

@Dao
public interface SchedMealDAO {

    @Query("SELECT * FROM sched_meals WHERE scheduledDate = :date AND userId = :userId")
    LiveData<List<SchedMeal>> getScheduledMeals(String date, String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertScheduledMeal(SchedMeal meal);

    @Query("SELECT * FROM sched_meals WHERE idMeal = :id AND userId = :userId")
    LiveData<SchedMeal> getScheduledMealById(String id, String userId);

    @Delete
    void deleteScheduledMeal(SchedMeal meal);
}