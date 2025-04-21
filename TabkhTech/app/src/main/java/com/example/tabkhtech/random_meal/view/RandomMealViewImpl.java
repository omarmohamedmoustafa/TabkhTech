package com.example.tabkhtech.random_meal.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.local.MealLocalDataSourceImpl;
import com.example.tabkhtech.model.pojos.Meal;
import com.example.tabkhtech.model.remote.MealRemoteDataSourceImpl;
import com.example.tabkhtech.model.repository.RepositoryImpl;
import com.example.tabkhtech.random_meal.presenter.RandomMealPresenter;
import com.example.tabkhtech.random_meal.presenter.RandomMealPresenterImpl;


public class RandomMealViewImpl extends AppCompatActivity implements RandomMealView ,OnAddToFavListener{

    private ImageView mealImage;
    private TextView mealName, mealCategory, mealInstructions;
    private Button favButton;

    private RandomMealPresenter presenter;
    private Meal currentMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);

        // Init views
        mealImage = findViewById(R.id.meal_image);
        mealName = findViewById(R.id.meal_name);
        mealCategory = findViewById(R.id.meal_category);
        mealInstructions = findViewById(R.id.meal_instructions);
        favButton = findViewById(R.id.fav_button);

        // Init presenter
        presenter = new RandomMealPresenterImpl(
                RepositoryImpl.getInstance(MealLocalDataSourceImpl.getInstance(this), MealRemoteDataSourceImpl.getInstance()),
                this
        );
        presenter.getRandomMeal();

        favButton.setOnClickListener(v -> {
            if (currentMeal != null) {
                presenter.addToFavorites(currentMeal);
                Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showRandomMeal(Meal meal) {
        currentMeal = meal;

        mealName.setText(meal.getStrMeal());
        mealCategory.setText(meal.getStrCategory());
        mealInstructions.setText(meal.getStrInstructions());

        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(mealImage);

        favButton.setEnabled(true); // <--- This is important
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnAddToFavClick(Meal meal) {

    }
}
