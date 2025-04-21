package com.example.tabkhtech.model.remote;

import com.example.tabkhtech.model.pojos.Categories;
import com.example.tabkhtech.model.pojos.Countries;
import com.example.tabkhtech.model.pojos.Ingredients;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.pojos.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {
    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    @GET("categories.php")
    Call<Categories> getCategories();

    @GET("list.php?a=list")
    Call<Countries> getCountries();

    @GET("list.php?i=list")
    Call<Ingredients> getIngredients();

    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Call<MealResponse> getMealsByCountry(@Query("a") String country);

    @GET("lookup.php")
    Call<MealResponse> getMealById(@Query("i") String id);

}
