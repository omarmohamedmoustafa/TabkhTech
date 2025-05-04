package com.example.tabkhtech.model.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tabkhtech.model.pojos.FavMeal;

import java.util.List;

@Dao
public interface FavMealDAO {

    @Query("SELECT * FROM fav_meals WHERE userId = :userId")
    LiveData<List<FavMeal>> getFavouriteMeals(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavouriteMeal(FavMeal meal);

    @Query("SELECT * FROM fav_meals WHERE idMeal = :id AND userId = :userId")
    LiveData<FavMeal> getFavouriteMealById(String id, String userId);

    @Delete
    void deleteFavouriteMeal(FavMeal meal);
}