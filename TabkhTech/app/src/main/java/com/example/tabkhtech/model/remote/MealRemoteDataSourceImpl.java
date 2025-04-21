package com.example.tabkhtech.model.remote;

import com.example.tabkhtech.model.pojos.MealResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MealRemoteDataSourceImpl implements MealRemoteDataSource {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static MealRemoteDataSource instance;
    private MealApiService service;

    private MealRemoteDataSourceImpl() {
        service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MealApiService.class);
    }

    public static MealRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new MealRemoteDataSourceImpl();
        }
        return instance;
    }

    private void handleMealCall(Call<MealResponse> call, NetworkCallback callback) {
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getMeals());
                } else {
                    callback.onFailure("Empty or failed response");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void getRandomMeal(NetworkCallback callback) {
        handleMealCall(service.getRandomMeal(), callback);
    }

    @Override
    public void getMealsByCategory(String category, NetworkCallback callback) {
        handleMealCall(service.getMealsByCategory(category), callback);
    }

    @Override
    public void getMealsByIngredient(String ingredient, NetworkCallback callback) {
        handleMealCall(service.getMealsByIngredient(ingredient), callback);
    }

    @Override
    public void getMealsByCountry(String country, NetworkCallback callback) {
        handleMealCall(service.getMealsByCountry(country), callback);
    }

    @Override
    public void getMealById(String id, NetworkCallback callback) {
        handleMealCall(service.getMealById(id), callback);
    }
}