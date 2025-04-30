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

    @Query("SELECT * FROM sched_meals")
    LiveData<List<SchedMeal>> getScheduledMeals();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertScheduledMeal(SchedMeal meal);

    @Query("SELECT * FROM sched_meals WHERE idMeal = :id")
    LiveData<SchedMeal> getScheduledMealById(String id);

    @Delete
    void deleteScheduledMeal(SchedMeal meal);
}
