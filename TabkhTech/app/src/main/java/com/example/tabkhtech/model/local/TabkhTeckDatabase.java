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
import com.example.tabkhtech.model.pojos.User;

@Database(entities = {FavMeal.class, SchedMeal.class, RecentMeal.class, User.class}, version =8)
public abstract class TabkhTeckDatabase extends RoomDatabase {
    private static TabkhTeckDatabase instance = null;

    public abstract FavMealDAO favMealDAO();
    public abstract RecentMealDAO recentMealDAO();
    public abstract SchedMealDAO schedMealDAO();
    public abstract UserDAO userDAO();
    public static TabkhTeckDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TabkhTeckDatabase.class, "meals")
                    .addMigrations(MIGRATION_3_4)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE fav_meals ADD COLUMN userId TEXT");
            database.execSQL("ALTER TABLE sched_meals ADD COLUMN userId TEXT");
            database.execSQL("ALTER TABLE recent_meals ADD COLUMN userId TEXT");
        }
    };
}