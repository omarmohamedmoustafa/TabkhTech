package com.example.tabkhtech.ui.search.presenter;

import com.example.tabkhtech.model.pojos.Category;
import com.example.tabkhtech.model.pojos.Country;
import com.example.tabkhtech.model.pojos.Ingredient;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.remote.CategoryNetworkCallback;
import com.example.tabkhtech.model.remote.CountryNetworkCallback;
import com.example.tabkhtech.model.remote.IngredientNetworkCallback;
import com.example.tabkhtech.model.remote.MealNetworkCallback;
import com.example.tabkhtech.model.remote.SingleMealNetworkCallback;
import com.example.tabkhtech.model.repository.Repository;
import com.example.tabkhtech.ui.search.view.SearchViewInterface;

import java.util.List;

public class SearchPresenterImpl implements SearchPresenter, IngredientNetworkCallback, CountryNetworkCallback, CategoryNetworkCallback, MealNetworkCallback, SingleMealNetworkCallback {
    SearchViewInterface searchView;
    Repository repository;

    public SearchPresenterImpl(SearchViewInterface searchView, Repository repository) {
        this.searchView = searchView;
        this.repository = repository;
    }

    @Override
    public void onCategorySuccess(List<Category> categories) {
      searchView.showCategories(categories);
    }

    @Override
    public void onCountrySuccess(List<Country> countries) {
        searchView.showCountries(countries);
    }

    @Override
    public void onIngredientSuccess(List<Ingredient> ingredients) {
        searchView.showIngredients(ingredients);
    }

    @Override
    public void onSuccess(List<Meal> meals) {
        searchView.showMeals(meals);
    }

    @Override
    public void onSingleMealSuccess(Meal meal) {
        searchView.showMealDetails(meal);
    }

    @Override
    public void onFailure(String errorMessage) {
        searchView.showError(errorMessage);
    }

    @Override
    public void getAllIngredients() {
        repository.getAllIngredients(this);
    }

    @Override
    public void getAllCountries() {
        repository.getAllCountries(this);
    }

    @Override
    public void getAllCategories() {
        repository.getAllCategories(this);
    }

    @Override
    public void getMealsByIngredient(String ingredient) {
        repository.getMealsByIngredient(ingredient,this);
    }

    @Override
    public void getMealsByCountry(String country) {
        repository.getMealsByCountry(country,this);
    }

    @Override
    public void getMealsByCategory(String category) {
        repository.getMealsByCategory(category,this);
    }

    @Override
    public void getMealById(String id) {
        repository.getMealById(id, this);
    }

    @Override
    public void insertRecentMeal(RecentMeal meal) {
        repository.insertRecentMeal(meal);
    }
}
