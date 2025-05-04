package com.example.tabkhtech.ui.single_meal.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.example.tabkhtech.model.pojos.SchedMeal;
import com.example.tabkhtech.model.remote.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.ui.single_meal.presenter.SingleMealPresenter;
import com.example.tabkhtech.ui.single_meal.presenter.SingleMealPresenterImpl;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    ImageButton calendarButton;
    private SharedPreferences sharedPreferences;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "guest"); // Default to "guest" if not found
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
        Log.d("mealFragment", "onCreateView called");
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
        calendarButton = view.findViewById(R.id.calendar_button);

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
            meal.setIngredients(ingredients);
            meal.setMeasures(measures);

            boolean fromFavorites = bundle.getBoolean("fromFavorites", false);
            showMeal(meal, fromFavorites);
        } else {
            Log.e("mealFragment", "No arguments received");
            Toast.makeText(getContext(), "No meal data provided", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMeal(Meal meal) {
        showMeal(meal, false);
    }

    private void showMeal(Meal meal, boolean fromFavorites) {
        try {
            currentMeal = meal;
            Log.d("mealFragment", "showMeal: " + meal.getStrMeal());

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

            if (ingredients != null && measures != null && !ingredients.isEmpty()) {
                for (int i = 0; i < ingredients.size(); i++) {
                    if (ingredients.get(i) == null || ingredients.get(i).isEmpty()) {
                        ingredients.remove(i);
                        measures.remove(i);
                        i--;
                    }
                }
                rv.setVisibility(View.VISIBLE);
                MealIngredientsAdapter adapter = new MealIngredientsAdapter(ingredients, measures);
                rv.setAdapter(adapter);
            } else {
                rv.setVisibility(View.GONE);
            }

            RecentMeal recentMeal = new RecentMeal(meal, userId);
            presenter.insertRecentMeal(recentMeal);

            favoriteButton.setSelected(fromFavorites);
            LiveData<FavMeal> favMealLiveData = ((SingleMealPresenterImpl) presenter).getFavMealById(meal.getIdMeal(), userId);
            favMealLiveData.observe(getViewLifecycleOwner(), favMeal -> {
                boolean isFavorite = favMeal != null;
                favoriteButton.setSelected(isFavorite);
            });

            favoriteButton.setOnClickListener(v -> {
                boolean newState = !favoriteButton.isSelected();
                favoriteButton.setSelected(newState);
                FavMeal favMeal = new FavMeal(currentMeal, userId);
                if (newState) {
                    presenter.insertFavMeal(favMeal);
                    Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.deleteFavMeal(favMeal);
                    Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                }
            });

            calendarButton.setOnClickListener(v -> {
                Calendar today = Calendar.getInstance();
                long todayMillis = today.getTimeInMillis();

                Calendar endDate = Calendar.getInstance();
                endDate.add(Calendar.DAY_OF_MONTH, 7);
                long endDateMillis = endDate.getTimeInMillis();

                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                        .setStart(todayMillis)
                        .setEnd(endDateMillis);

                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Schedule Meal (Next 7 Days)")
                        .setSelection(todayMillis)
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();

                datePicker.show(getParentFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    if (selection >= todayMillis && selection <= endDateMillis) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String selectedDate = dateFormat.format(selection);
                        Log.d("mealFragment", "Scheduled meal for date: " + selectedDate);

                        SchedMeal schedMeal = new SchedMeal(currentMeal, selectedDate, userId);
                        presenter.insertSchedMeal(schedMeal);

                        Toast.makeText(getContext(), "Meal scheduled for " + selectedDate, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w("mealFragment", "Invalid date selected: " + selection);
                        Toast.makeText(getContext(), "Please select a date within the next 7 days", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } catch (Exception e) {
            Log.e("mealFragment", "Error in showMeal", e);
            Toast.makeText(getContext(), "Error displaying meal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}