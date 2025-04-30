package com.example.tabkhtech.ui.favourites.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabkhtech.R;
import com.example.tabkhtech.ui.favourites.presenter.FavouritesPresenter;
import com.example.tabkhtech.ui.favourites.presenter.FavouritesPresenterImpl;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.remote.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.ui.single_meal.view.mealFragment;

import java.util.ArrayList;


public class FavouritesFragment extends Fragment implements OnMealClickListener, FavouritesView {

    private RecyclerView rv;
    private LinearLayoutManager layoutManager;
    private FavouritesAdapter adapter;
    private FavouritesPresenter presenter;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new FavouritesPresenterImpl(this,
                RepositoryImpl.getInstance(
                        MealLocalDataSourceImpl.getInstance(requireContext()),
                        MealRemoteDataSourceImpl.getInstance()
                ));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.mealsRV);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        loadFavourites();
    }

    private void loadFavourites() {
        presenter.getFavouriteMeals().observe(getViewLifecycleOwner(), meals -> {
            if (meals != null && rv != null) {
                adapter = new FavouritesAdapter(meals, this);
                rv.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onMealClick(Meal meal) {
        mealFragment mealFrag = new mealFragment();
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
        getChildFragmentManager().beginTransaction()
                .replace(R.id.favFragmentContainer, mealFrag)
                .addToBackStack("meal_detail")
                .commit();

        rootView.findViewById(R.id.favFragmentContainer).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.favMainContentLayout).setVisibility(View.GONE);
    }

    public boolean onBackPressed() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStack();
            rootView.findViewById(R.id.favFragmentContainer).setVisibility(View.GONE);
            rootView.findViewById(R.id.favMainContentLayout).setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}