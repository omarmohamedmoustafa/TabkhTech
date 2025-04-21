package com.example.tabkhtech.model.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tabkhtech.model.pojos.Meal;

@Database(entities = {Meal.class} , version =1)
public abstract class MealDatabase extends RoomDatabase {
    private static MealDatabase instance = null;
    public abstract MealDAO MealDAO();
    public static  MealDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MealDatabase.class, "meals")
                    .build();
        }
        return instance;
    }
}
