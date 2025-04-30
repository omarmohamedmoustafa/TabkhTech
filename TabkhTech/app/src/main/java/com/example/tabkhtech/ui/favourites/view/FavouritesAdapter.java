package com.example.tabkhtech.ui.favourites.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.pojos.FavMeal;
import com.example.tabkhtech.model.pojos.Meal;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    List<FavMeal> meals;
    OnMealClickListener mealClickListener;
    public FavouritesAdapter(List<FavMeal> meals, OnMealClickListener mealClickListener) {
        this.meals = meals;
        this.mealClickListener = mealClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_meal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.title.setText(meal.getStrMeal());
        Glide.with(holder.itemView.getContext())
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            mealClickListener.onMealClick(meal);
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.mealImg);
            title = itemView.findViewById(R.id.mealName);
        }
    }
}


