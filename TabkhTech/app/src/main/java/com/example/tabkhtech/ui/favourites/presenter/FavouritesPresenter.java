package com.example.tabkhtech.ui.favourites.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;

import java.util.List;

public interface FavouritesPresenter {
    LiveData<List<FavMeal>> getFavouriteMeals();
}
