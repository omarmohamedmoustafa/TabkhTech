package com.example.tabkhtech.model.local;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;
//import com.example.tabkhtech.model.pojos.User;
//import com.example.tabkhtech.model.pojos.User;

import java.util.List;
/*
*  @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(String userId);

    @Query("DELETE FROM users WHERE id = :userId")
    void deleteUser(String userId);
*
* */
public interface MealLocalDataSource {
    LiveData<List<FavMeal>> getAllFavMeals(String userId);
    LiveData<List<SchedMeal>> getAllSchedMeals(String date, String userId);
    LiveData<List<RecentMeal>> getAllRecentMeals(String userId, int limit);
    void insertFavMeal(FavMeal meal);
    void insertSchedMeal(SchedMeal meal);
    void insertRecentMeal(RecentMeal meal);
    void deleteFavMeal(FavMeal meal);
    void deleteSchedMeal(SchedMeal meal);
    void deleteRecentMeal(RecentMeal meal);
    LiveData<FavMeal> getFavMealById(String id, String userId);
    LiveData<SchedMeal> getSchedMealById(String id, String userId);
    LiveData<RecentMeal> getRecentMealById(String id, String userId);

//    void insertUser(User user);
//    LiveData<User> getUserById(String userId);
//    void updateUser(User user);
//    void deleteUser(String userId);
}