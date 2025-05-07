package com.example.tabkhtech.ui.favourites.view;


import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.Meal;

public interface OnMealClickListener {
    void onMealClick(FavMeal meal);
    void onDeleteClick(FavMeal meal);
}
