package com.example.tabkhtech.model.pojos;


import androidx.room.Entity;

@Entity(tableName = "sched_meals")
public class SchedMeal extends Meal{
    private String scheduledDate;

    public SchedMeal() {
        super();
    }
    public SchedMeal(Meal meal){
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
        this.setStrIngredient1(meal.getStrIngredient1());
        this.setStrIngredient2(meal.getStrIngredient2());
        this.setStrIngredient3(meal.getStrIngredient3());
        this.setStrIngredient4(meal.getStrIngredient4());
        this.setStrIngredient5(meal.getStrIngredient5());
        this.setStrIngredient6(meal.getStrIngredient6());
        this.setStrIngredient7(meal.getStrIngredient7());
        this.setStrIngredient8(meal.getStrIngredient8());
        this.setStrIngredient9(meal.getStrIngredient9());
        this.setStrIngredient10(meal.getStrIngredient10());
        this.setStrIngredient11(meal.getStrIngredient11());
        this.setStrIngredient12(meal.getStrIngredient12());
        this.setStrIngredient13(meal.getStrIngredient13());
        this.setStrIngredient14(meal.getStrIngredient14());
        this.setStrIngredient15(meal.getStrIngredient15());
        this.setStrIngredient16(meal.getStrIngredient16());
        this.setStrIngredient17(meal.getStrIngredient17());
        this.setStrIngredient18(meal.getStrIngredient18());
        this.setStrIngredient19(meal.getStrIngredient19());
        this.setStrIngredient20(meal.getStrIngredient20());
        this.setScheduledDate("2024-10-13");
    }

    public String getScheduledDate() {
        return scheduledDate;
    }
    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
