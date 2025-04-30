package com.example.tabkhtech.model.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;


@Database(entities = {FavMeal.class, SchedMeal.class, RecentMeal.class}, version = 3)
public abstract class MealDatabase extends RoomDatabase {
    private static MealDatabase instance = null;

    public abstract FavMealDAO favMealDAO();
    public abstract RecentMealDAO recentMealDAO();
    public abstract SchedMealDAO schedMealDAO();

    public static MealDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MealDatabase.class, "meals")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}