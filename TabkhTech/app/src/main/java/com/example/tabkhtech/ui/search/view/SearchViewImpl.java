package com.example.tabkhtech.ui.search.view;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabkhtech.R;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.pojos.Category;
import com.example.tabkhtech.model.pojos.Country;
import com.example.tabkhtech.model.pojos.Ingredient;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.remote.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.ui.search.presenter.SearchPresenter;
import com.example.tabkhtech.ui.search.presenter.SearchPresenterImpl;
import com.example.tabkhtech.ui.single_meal.view.mealFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class SearchViewImpl extends Fragment implements SearchViewInterface,
        OnIngredientClickListener, OnCategoryClickListener, OnCountryClickListener, OnMealClickListener {

    private SearchPresenter presenter;
    private mealFragment mealFrag;
    private MaterialButton searchByCategory, searchByCountry, searchByIngredient;
    private RecyclerView rv;
    private LinearLayoutManager layoutManager;
    private CategoriesAdapter categoriesAdapter;
    private CountriesAdapter countriesAdapter;
    private IngredientsAdapter ingredientsAdapter;
    private MealsAdapter mealsAdapter;
    private SearchView searchBar;
    private View rootView;

    private String currentSearchType = "categories";
    private String previousSearchType = "categories";
    private List<Category> allCategories = new ArrayList<>();
    private List<Country> allCountries = new ArrayList<>();
    private List<Ingredient> allIngredients = new ArrayList<>();
    private List<Meal> allMeals = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenterImpl(this,
                RepositoryImpl.getInstance(
                        MealLocalDataSourceImpl.getInstance(requireContext()),
                        MealRemoteDataSourceImpl.getInstance())
        );
        presenter.getAllCategories();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchByCategory = rootView.findViewById(R.id.btnCategories);
        searchByCountry = rootView.findViewById(R.id.btnCountries);
        searchByIngredient = rootView.findViewById(R.id.btnIngredients);
        setActiveButton(searchByCategory);

        searchBar = rootView.findViewById(R.id.searchView);
        searchBar.clearFocus();
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });
        searchBar.setOnCloseListener(() -> {
            switch (currentSearchType) {
                case "categories":
                    presenter.getAllCategories();
                    break;
                case "countries":
                    presenter.getAllCountries();
                    break;
                case "ingredients":
                    presenter.getAllIngredients();
                    break;
                case "meals":
                    showMeals(allMeals);
                    break;
            }
            return false;
        });

        rv = rootView.findViewById(R.id.searchRV);
        layoutManager = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(layoutManager);
        categoriesAdapter = new CategoriesAdapter(new ArrayList<>(), this);
        rv.setAdapter(categoriesAdapter);

        searchByCategory.setOnClickListener(v -> {
            currentSearchType = "categories";
            previousSearchType = "categories";
            setActiveButton(searchByCategory);
            presenter.getAllCategories();
        });
        searchByCountry.setOnClickListener(v -> {
            currentSearchType = "countries";
            previousSearchType = "countries";
            setActiveButton(searchByCountry);
            presenter.getAllCountries();
        });
        searchByIngredient.setOnClickListener(v -> {
            currentSearchType = "ingredients";
            previousSearchType = "ingredients";
            setActiveButton(searchByIngredient);
            presenter.getAllIngredients();
        });
    }

    private void filterData(String query) {
        query = query.toLowerCase().trim();
        boolean isEmptyQuery = query.isEmpty();

        switch (currentSearchType) {
            case "categories":
                List<Category> filteredCategories = new ArrayList<>();
                if (!isEmptyQuery) {
                    for (Category category : allCategories) {
                        if (category.getStrCategory().toLowerCase().contains(query)) {
                            filteredCategories.add(category);
                        }
                    }
                } else {
                    filteredCategories.addAll(allCategories);
                }
                if (categoriesAdapter == null) {
                    categoriesAdapter = new CategoriesAdapter(filteredCategories, this);
                    rv.setAdapter(categoriesAdapter);
                } else {
                    categoriesAdapter.setFilteredList(filteredCategories);
                    categoriesAdapter.notifyDataSetChanged();
                }
                if (filteredCategories.isEmpty() && !isEmptyQuery) {
                    Toast.makeText(requireContext(), "No categories found", Toast.LENGTH_SHORT).show();
                }
                break;

            case "countries":
                List<Country> filteredCountries = new ArrayList<>();
                if (!isEmptyQuery) {
                    for (Country country : allCountries) {
                        if (country.getStrArea().toLowerCase().contains(query)) {
                            filteredCountries.add(country);
                        }
                    }
                } else {
                    filteredCountries.addAll(allCountries);
                }
                if (countriesAdapter == null) {
                    countriesAdapter = new CountriesAdapter(filteredCountries, this);
                    rv.setAdapter(countriesAdapter);
                } else {
                    countriesAdapter.setFilteredList(filteredCountries);
                    countriesAdapter.notifyDataSetChanged();
                }
                if (filteredCountries.isEmpty() && !isEmptyQuery) {
                    Toast.makeText(requireContext(), "No countries found", Toast.LENGTH_SHORT).show();
                }
                break;

            case "ingredients":
                List<Ingredient> filteredIngredients = new ArrayList<>();
                if (!isEmptyQuery) {
                    for (Ingredient ingredient : allIngredients) {
                        if (ingredient.getStrIngredient().toLowerCase().contains(query)) {
                            filteredIngredients.add(ingredient);
                        }
                    }
                } else {
                    filteredIngredients.addAll(allIngredients);
                }
                if (ingredientsAdapter == null) {
                    ingredientsAdapter = new IngredientsAdapter(filteredIngredients, this);
                    rv.setAdapter(ingredientsAdapter);
                } else {
                    ingredientsAdapter.setFilteredList(filteredIngredients);
                    ingredientsAdapter.notifyDataSetChanged();
                }
                if (filteredIngredients.isEmpty() && !isEmptyQuery) {
                    Toast.makeText(requireContext(), "No ingredients found", Toast.LENGTH_SHORT).show();
                }
                break;

            case "meals":
                List<Meal> filteredMeals = new ArrayList<>();
                if (!isEmptyQuery) {
                    for (Meal meal : allMeals) {
                        if (meal.getStrMeal().toLowerCase().contains(query)) {
                            filteredMeals.add(meal);
                        }
                    }
                } else {
                    filteredMeals.addAll(allMeals);
                }
                if (mealsAdapter == null) {
                    mealsAdapter = new MealsAdapter(filteredMeals, this);
                    rv.setAdapter(mealsAdapter);
                } else {
                    mealsAdapter.setFilteredList(filteredMeals);
                    mealsAdapter.notifyDataSetChanged();
                }
                if (filteredMeals.isEmpty() && !isEmptyQuery) {
                    Toast.makeText(requireContext(), "No meals found", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setActiveButton(MaterialButton activeButton) {
        searchByCategory.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
        searchByCountry.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
        searchByIngredient.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
        activeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        activeButton.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void showCountries(List<Country> countries) {
        this.allCountries = countries;
        countriesAdapter = new CountriesAdapter(countries, this);
        rv.setAdapter(countriesAdapter);
        countriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        this.allIngredients = ingredients;
        ingredientsAdapter = new IngredientsAdapter(ingredients, this);
        rv.setAdapter(ingredientsAdapter);
        ingredientsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCategories(List<Category> categories) {
        this.allCategories = categories;
        categoriesAdapter = new CategoriesAdapter(categories, this);
        rv.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMeals(List<Meal> meals) {
        this.allMeals = meals;
        currentSearchType = "meals";
        mealsAdapter = new MealsAdapter(meals, this);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(mealsAdapter);
        mealsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMealDetails(Meal meal) {

        addRecentMeal(meal);

        mealFrag = new mealFragment();
        Bundle args = new Bundle();
        args.putString("id", meal.getIdMeal());
        args.putString("imageUrl", meal.getStrMealThumb());
        args.putString("name", meal.getStrMeal());
        args.putString("category", meal.getStrCategory());
        args.putString("area", meal.getStrArea());
        args.putString("instructions", meal.getStrInstructions());
        args.putString("youtube", meal.getStrYoutube());
        args.putBoolean("fromFavorites", false);
        args.putStringArrayList("ingredients", new ArrayList<>(meal.getIngredients()));
        args.putStringArrayList("measures", new ArrayList<>(meal.getMeasures()));
        mealFrag.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, mealFrag);
        transaction.addToBackStack(null);
        transaction.commit();

        View fragmentContainer = getActivity().findViewById(R.id.frame_layout);
        View mainContent = rootView.findViewById(R.id.mainContentLayout);
        if (fragmentContainer != null && mainContent != null) {
            fragmentContainer.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        }
    }

    public void addRecentMeal(Meal meal) {
        long currentTime = System.currentTimeMillis();
        RecentMeal recentMeal = new RecentMeal(meal);
        recentMeal.setLastOpened(currentTime);
        presenter.insertRecentMeal(recentMeal);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClick(Category category) {
        presenter.getMealsByCategory(category.getStrCategory());
    }

    @Override
    public void onCountryClick(Country country) {
        presenter.getMealsByCountry(country.getStrArea());
    }

    @Override
    public void onIngredientClick(Ingredient ingredient) {
        presenter.getMealsByIngredient(ingredient.getStrIngredient());
    }

    @Override
    public void onMealClick(Meal meal) {
        presenter.getMealById(meal.getIdMeal());
    }

    public boolean handleBackPress() {
        View fragmentContainer = getActivity().findViewById(R.id.frame_layout);
        View mainContent = rootView.findViewById(R.id.mainContentLayout);
        if (fragmentContainer != null && mainContent != null && fragmentContainer.getVisibility() == View.VISIBLE) {
            getParentFragmentManager().popBackStack();
            fragmentContainer.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
            return true;
        } else if (currentSearchType.equals("meals")) {
            currentSearchType = previousSearchType;
            if (currentSearchType.equals("categories")) {
                setActiveButton(searchByCategory);
                presenter.getAllCategories();
            } else if (currentSearchType.equals("countries")) {
                setActiveButton(searchByCountry);
                presenter.getAllCountries();
            } else if (currentSearchType.equals("ingredients")) {
                setActiveButton(searchByIngredient);
                presenter.getAllIngredients();
            }
            return true;
        }
        return false;
    }


}