package com.example.tabkhtech.model.remote;

import com.example.tabkhtech.model.pojos.CategoryResponse;
import com.example.tabkhtech.model.pojos.CountryResponse;
import com.example.tabkhtech.model.pojos.IngredientResponse;
import com.example.tabkhtech.model.pojos.MealResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MealRemoteDataSourceImpl implements MealRemoteDataSource {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String FLAGS_URL = "https://flagsapi.com/";
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

    private void handleMealCall(Call<MealResponse> call, MealNetworkCallback callback) {
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
    private void handleSingleMealCall(Call<MealResponse> call, SingleMealNetworkCallback callback) {
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSingleMealSuccess(response.body().getMeals().get(0));
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
    public void getRandomMeal(SingleMealNetworkCallback callback) {
        handleSingleMealCall(service.getRandomMeal(), callback);
    }

    @Override
    public void getMealsByCategory(String category, MealNetworkCallback callback) {
        handleMealCall(service.getMealsByCategory(category), callback);
    }

    @Override
    public void getMealsByIngredient(String ingredient, MealNetworkCallback callback) {
        handleMealCall(service.getMealsByIngredient(ingredient), callback);
    }

    @Override
    public void getMealsByCountry(String country, MealNetworkCallback callback) {
        handleMealCall(service.getMealsByCountry(country), callback);
    }

    @Override
    public void getMealById(String id, SingleMealNetworkCallback callback) {
        service.getMealById(id).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSingleMealSuccess(response.body().getMeals().get(0));
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
    public void getAllCategories(CategoryNetworkCallback callback) {
        service.getAllCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onCategorySuccess(response.body().getCategories());
                } else {
                    callback.onFailure("Empty or failed response");
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });

    }

    @Override
    public void getAllCountries(CountryNetworkCallback callback) {
        service.getAllCountries().enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onCountrySuccess(response.body().getCountries());
                } else {
                    callback.onFailure("Empty or failed response");
                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });

    }

    @Override
    public void getAllIngredients(IngredientNetworkCallback callback) {
        service.getAllIngredients().enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onIngredientSuccess(response.body().getIngredients());
                } else {
                    callback.onFailure("Empty or failed response");
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}