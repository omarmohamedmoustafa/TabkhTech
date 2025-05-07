package com.example.tabkhtech.model.pojos;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "recent_meals", primaryKeys = {"idMeal", "userId"})
public class RecentMeal extends Meal {
    private long lastOpened;
    @NonNull
    private String userId;

    public RecentMeal() {
        super();
    }

    public RecentMeal(Meal meal, String userId) {
        this.setIdMeal(meal.getIdMeal());
        this.setStrMeal(meal.getStrMeal());
        this.setStrMealThumb(meal.getStrMealThumb());
        this.setStrCategory(meal.getStrCategory());
        this.setStrArea(meal.getStrArea());
        this.setStrInstructions(meal.getStrInstructions());
        this.setStrTags(meal.getStrTags());
        this.setStrYoutube(meal.getStrYoutube());
        this.setStrSource(meal.getStrSource());
        this.setStrImageSource(meal.getStrImageSource());
        this.setStrCreativeCommonsConfirmed(meal.getStrCreativeCommonsConfirmed());
        this.setDateModified(meal.getDateModified());
        this.setIngredients(meal.getIngredients());
        this.setMeasures(meal.getMeasures());
        this.setStrMealAlternate(meal.getStrMealAlternate());
        this.lastOpened = System.currentTimeMillis();
        this.userId = userId;
    }

    public long getLastOpened() {
        return lastOpened;
    }

    public void setLastOpened(long lastOpened) {
        this.lastOpened = lastOpened;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}