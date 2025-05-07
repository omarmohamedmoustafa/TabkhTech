package com.example.tabkhtech.model.pojos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_meals", primaryKeys = {"idMeal", "userId"})
public class FavMeal extends Meal {
    private boolean isFavorite;
    @NonNull
    private String userId;

    public FavMeal() {
        super();
    }

    public FavMeal(Meal meal, String userId) {
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
        this.isFavorite = true;
        this.userId = userId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}