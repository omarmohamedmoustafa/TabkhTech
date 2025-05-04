package com.example.tabkhtech.model.remote;

import com.example.tabkhtech.model.pojos.CategoryResponse;
import com.example.tabkhtech.model.pojos.CountryResponse;
import com.example.tabkhtech.model.pojos.IngredientResponse;
import com.example.tabkhtech.model.pojos.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiService {
    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    @GET("categories.php")
    Call<CategoryResponse> getAllCategories();

    @GET("list.php?a=list")
    Call<CountryResponse> getAllCountries();

    @GET("list.php?i=list")
    Call<IngredientResponse> getAllIngredients();

    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Call<MealResponse> getMealsByCountry(@Query("a") String country);

    @GET("lookup.php")
    Call<MealResponse> getMealById(@Query("i") String id);
}
