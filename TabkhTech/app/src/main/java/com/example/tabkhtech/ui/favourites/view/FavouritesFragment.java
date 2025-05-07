package com.example.tabkhtech.ui.favourites.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabkhtech.R;
import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.ui.favourites.presenter.FavouritesPresenter;
import com.example.tabkhtech.ui.favourites.presenter.FavouritesPresenterImpl;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.remote.retrofit.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.ui.detailed_meal.view.MealFragment;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment implements OnMealClickListener, FavouritesView {

    private RecyclerView rv;
    private LinearLayoutManager layoutManager;
    private FavouritesAdapter adapter;
    private FavouritesPresenter presenter;
    private View rootView;
    private SharedPreferences sharedPreferences;
    private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "guest"); // Default to "guest"
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
        ImageView ivNoFavorites = rootView.findViewById(R.id.ivNoFavorites);
        presenter.getAllFavMeals(userId).observe(getViewLifecycleOwner(), favMeals -> {
            if (favMeals != null && !favMeals.isEmpty()) {
                adapter = new FavouritesAdapter(favMeals, this);
                rv.setAdapter(adapter);
                rv.setVisibility(View.VISIBLE);
                ivNoFavorites.setVisibility(View.GONE);
            } else {
                rv.setVisibility(View.GONE);
                ivNoFavorites.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onMealClick(FavMeal meal) {
        MealFragment mealFrag = new MealFragment();
        Bundle args = new Bundle();
        args.putString("id", meal.getIdMeal());
        args.putString("imageUrl", meal.getStrMealThumb());
        args.putString("name", meal.getStrMeal());
        args.putString("category", meal.getStrCategory());
        args.putString("area", meal.getStrArea());
        args.putString("instructions", meal.getStrInstructions());
        args.putString("youtube", meal.getStrYoutube());
        args.putBoolean("fromFavorites", true); // Indicate meal is from favorites
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

    @Override
    public void onDeleteClick(FavMeal meal) {
        presenter.deleteMeal(meal);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

//    public void handleBackPress() {
//        FragmentContainerView favFragmentContainer = rootView.findViewById(R.id.favFragmentContainer);
//        LinearLayout favMainContentLayout = rootView.findViewById(R.id.favMainContentLayout);
//        if (favFragmentContainer.getVisibility() == View.VISIBLE) {
//            favFragmentContainer.setVisibility(View.GONE);
//            favMainContentLayout.setVisibility(View.VISIBLE);
//            getChildFragmentManager().popBackStack();
//        } else {
//            requireActivity().onBackPressed();
//        }
//    }
    public boolean handleBackPress() {
        FragmentContainerView favFragmentContainer = rootView.findViewById(R.id.favFragmentContainer);
        LinearLayout favMainContentLayout = rootView.findViewById(R.id.favMainContentLayout);
        if (favFragmentContainer.getVisibility() == View.VISIBLE) {
            favFragmentContainer.setVisibility(View.GONE);
            favMainContentLayout.setVisibility(View.VISIBLE);
            getChildFragmentManager().popBackStack();
            return true; // Back press handled
        }
        return false; // Back press not handled, navigate to Home
    }
}