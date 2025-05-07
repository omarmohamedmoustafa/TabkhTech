package com.example.tabkhtech.model.pojos;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "sched_meals", primaryKeys = {"idMeal", "userId", "scheduledDate"})
public class SchedMeal extends Meal {
    @NonNull
    private String scheduledDate;
    @NonNull
    private String userId; // New field to associate meal with user

    public SchedMeal() {
        super();
    }

    public SchedMeal(Meal meal, String userId, String scheduledDate) {
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
        this.scheduledDate = scheduledDate;
        this.userId = userId;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}