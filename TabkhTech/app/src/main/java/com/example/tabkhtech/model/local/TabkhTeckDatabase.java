package com.example.tabkhtech.model.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.pojos.SchedMeal;
//import com.example.tabkhtech.model.pojos.User;
//import com.example.tabkhtech.model.pojos.User;

@Database(entities = {FavMeal.class, SchedMeal.class, RecentMeal.class}, version =1, exportSchema = false)
public abstract class TabkhTeckDatabase extends RoomDatabase {
    private static TabkhTeckDatabase instance = null;

    public abstract FavMealDAO favMealDAO();
    public abstract RecentMealDAO recentMealDAO();
    public abstract SchedMealDAO schedMealDAO();
//    public abstract UserDAO userDAO();
    public static TabkhTeckDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TabkhTeckDatabase.class, "meals")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}