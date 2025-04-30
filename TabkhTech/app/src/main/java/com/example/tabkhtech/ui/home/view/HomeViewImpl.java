package com.example.tabkhtech.ui.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.ui.home.presenter.HomePresenter;
import com.example.tabkhtech.ui.home.presenter.HomePresenterImpl;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.remote.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.ui.single_meal.view.mealFragment;

import java.util.ArrayList;


public class HomeViewImpl extends Fragment implements HomeView, OnMealClickListener {

    private ImageView mealImage;
    private TextView mealName;
    private RecyclerView rv;
    private HomeAdapter adapter;
    private HomePresenter presenter;
    private Meal currentMeal;
    private mealFragment mealFrag;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentContainerView fragmentContainerView;
    private CardView randomMealCard;

    public HomeViewImpl() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        presenter = new HomePresenterImpl(
                RepositoryImpl.getInstance(MealLocalDataSourceImpl.getInstance(getContext()), MealRemoteDataSourceImpl.getInstance()),
                this
        );

        presenter.getRandomMeal();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mealImage = view.findViewById(R.id.mealImg);
        mealName = view.findViewById(R.id.mealName);
        rv = view.findViewById(R.id.recentMealRV);
        fragmentContainerView = view.findViewById(R.id.homeFragmentContainer);
        randomMealCard = view.findViewById(R.id.randomMealCard);
        fragmentManager = getParentFragmentManager();

        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new HomeAdapter(new ArrayList<>(), this);
        rv.setAdapter(adapter);

        presenter.getRecentlyViewedMeals(5).observe(getViewLifecycleOwner(), recentMeals -> {
            adapter.updateMeals(recentMeals);
        });

        randomMealCard.setOnClickListener(v -> {
            if (currentMeal != null) {
                onMealClick(currentMeal);
            }
        });
    }

    @Override
    public void showRandomMeal(Meal meal) {
        currentMeal = meal;
        mealName.setText(meal.getStrMeal());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(mealImage);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMealClick(Meal meal) {
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
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFragmentContainer, mealFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentContainerView.setVisibility(View.VISIBLE);
    }
}