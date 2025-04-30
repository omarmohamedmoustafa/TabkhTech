package com.example.tabkhtech.model.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.example.tabkhtech.model.pojos.FavMeal;

@Dao
public interface FavMealDAO {

    @Query("SELECT * FROM fav_meals")
    LiveData<List<FavMeal>> getFavouriteMeals();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavouriteMeal(FavMeal meal);


    @Query("SELECT * FROM fav_meals WHERE idMeal = :id")
    LiveData<FavMeal> getFavouriteMealById(String id);

    @Delete
    void deleteFavouriteMeal(FavMeal meal);
}
