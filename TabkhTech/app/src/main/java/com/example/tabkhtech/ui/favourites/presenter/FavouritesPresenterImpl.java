package com.example.tabkhtech.ui.favourites.presenter;

import androidx.lifecycle.LiveData;

import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.ui.favourites.view.FavouritesView;
import com.example.tabkhtech.model.repository.Repository;

import java.util.List;

public class FavouritesPresenterImpl implements FavouritesPresenter{
    Repository repository;
    FavouritesView view;
    public FavouritesPresenterImpl( FavouritesView view,Repository repository) {
        this.repository = repository;
        this.view = view;
    }


    @Override
    public LiveData<List<FavMeal>> getAllFavMeals(String userId) {
        return repository.getAllFavMeals(userId);
    }
}
