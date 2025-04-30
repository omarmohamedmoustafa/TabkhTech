package com.example.tabkhtech.ui.single_meal.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.pojos.RecentMeal;
import com.example.tabkhtech.model.remote.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.ui.single_meal.presenter.SingleMealPresenter;
import com.example.tabkhtech.ui.single_meal.presenter.SingleMealPresenterImpl;

import java.util.List;

public class mealFragment extends Fragment implements SingleMealView {

    SingleMealPresenter presenter;
    ImageView mealImage;
    TextView mealName;
    TextView mealCategory;
    TextView mealArea;
    TextView mealInstructions;
    ImageButton favoriteButton;
    Meal currentMeal;
    WebView webView;
    RecyclerView rv;
    List<String> ingredients;
    List<String> measures;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SingleMealPresenterImpl(
                RepositoryImpl.getInstance(
                        MealLocalDataSourceImpl.getInstance(this.getContext()),
                        MealRemoteDataSourceImpl.getInstance()),
                this
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mealImage = view.findViewById(R.id.meal_image);
        mealName = view.findViewById(R.id.meal_name);
        mealCategory = view.findViewById(R.id.meal_category);
        mealArea = view.findViewById(R.id.meal_area);
        mealInstructions = view.findViewById(R.id.meal_instructions);
        favoriteButton = view.findViewById(R.id.favorite_button);
        webView = view.findViewById(R.id.webView);
        rv = view.findViewById(R.id.ingredientsRV);

        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        Bundle bundle = getArguments();
        if (bundle != null) {
            Meal meal = new Meal();
            meal.setStrMealThumb(bundle.getString("imageUrl"));
            meal.setStrMeal(bundle.getString("name"));
            meal.setStrCategory(bundle.getString("category"));
            meal.setStrArea(bundle.getString("area"));
            meal.setStrInstructions(bundle.getString("instructions"));
            meal.setStrYoutube(bundle.getString("youtube"));
            meal.setIdMeal(bundle.getString("id"));

            ingredients = bundle.getStringArrayList("ingredients");
            measures = bundle.getStringArrayList("measures");

            boolean fromFavorites = bundle.getBoolean("fromFavorites", false);
            showMeal(meal, fromFavorites);
        }
    }

    @Override
    public void showMeal(Meal meal) {
        showMeal(meal, false);
    }

    private void showMeal(Meal meal, boolean fromFavorites) {
        currentMeal = meal;
        mealName.setText(meal.getStrMeal());
        mealCategory.setText(meal.getStrCategory());
        mealArea.setText(meal.getStrArea());
        mealInstructions.setText(meal.getStrInstructions());
        if (meal.getStrMealThumb() != null) {
            Glide.with(this)
                    .load(meal.getStrMealThumb())
                    .into(mealImage);
        }
        if (meal.getStrYoutube() != null && meal.getStrYoutube().contains("youtube.com/watch?v=")) {
            String embedUrl = meal.getStrYoutube().replace("watch?v=", "embed/");
            String video = "<iframe width=\"100%\" height=\"100%\" src=\"" + embedUrl +
                    "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadDataWithBaseURL(null, video, "text/html", "utf-8", null);
        } else {
            webView.setVisibility(View.GONE);
        }

        // Populate the RecyclerView with ingredients and measures
        if (ingredients != null && measures != null && !ingredients.isEmpty()) {
            rv.setVisibility(View.VISIBLE);
            MealIngredientsAdapter adapter = new MealIngredientsAdapter(ingredients, measures);
            rv.setAdapter(adapter);
        } else {
            rv.setVisibility(View.GONE);
        }

        RecentMeal recentMeal = new RecentMeal(meal);
        presenter.insertRecentMeal(recentMeal);

        favoriteButton.setSelected(fromFavorites);
        LiveData<FavMeal> favMealLiveData = ((SingleMealPresenterImpl) presenter).getFavMealById(meal.getIdMeal());
        favMealLiveData.observe(getViewLifecycleOwner(), favMeal -> {
            boolean isFavorite = favMeal != null;
            favoriteButton.setSelected(isFavorite);
        });

        favoriteButton.setOnClickListener(v -> {
            boolean newState = !favoriteButton.isSelected();
            favoriteButton.setSelected(newState);
            FavMeal favMeal = new FavMeal();
            favMeal.setIdMeal(meal.getIdMeal());
            favMeal.setStrMeal(meal.getStrMeal());
            favMeal.setStrMealThumb(meal.getStrMealThumb());
            favMeal.setStrCategory(meal.getStrCategory());
            favMeal.setStrArea(meal.getStrArea());
            favMeal.setStrInstructions(meal.getStrInstructions());
            favMeal.setStrYoutube(meal.getStrYoutube());
            favMeal.setIngredients(meal.getIngredients());
            favMeal.setMeasures(meal.getMeasures());
            if (newState) {
                presenter.addToFavorites(favMeal);
            } else {
                presenter.removeFromFavorites(favMeal);
            }
        });
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}