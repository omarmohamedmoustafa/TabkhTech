package com.example.tabkhtech.model.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Ingredients {
    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "ingredients=" + ingredients +
                '}';
    }

    public Ingredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    private class Ingredient  {
        private String strIngredient;

        private String measure;

        @Override
        public String toString() {
            return "Ingredient{" +
                    "ingredient='" + strIngredient + '\'' +
                    ", measure='" + measure + '\'' +
                    '}';
        }

        public Ingredient(String ingredient, String measure) {
            this.strIngredient = ingredient;
            this.measure = measure;
        }

        public String getIngredientName() {
            return strIngredient;
        }

        public void setIngredient(String ingredient) {
            this.strIngredient = ingredient;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }
    }
}
