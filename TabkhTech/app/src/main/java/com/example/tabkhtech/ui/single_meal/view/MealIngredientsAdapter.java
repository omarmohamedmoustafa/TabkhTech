package com.example.tabkhtech.ui.single_meal.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.pojos.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class MealIngredientsAdapter extends RecyclerView.Adapter<MealIngredientsAdapter.ViewHolder> {

    private List<String> ingredients = new ArrayList<>();
    private List<String> measures = new ArrayList<>();

    public MealIngredientsAdapter(List<String> ingredients, List<String> measures) {
        this.ingredients = ingredients;
        this.measures = measures;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind ingredient name
        holder.ingredientName.setText(ingredients.get(position));

        // Bind measurement
        holder.ingredientMeasure.setText(measures.get(position));
        String ingredientName = ingredients.get(position).replace(" ", "_").toLowerCase();
        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredientName + "-small.png";

        // Load the image using Glide
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.ingredientImage);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ingredientImage;
        TextView ingredientName;
        TextView ingredientMeasure;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImg);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientMeasure = itemView.findViewById(R.id.ingredientMeasure);
        }
    }
}