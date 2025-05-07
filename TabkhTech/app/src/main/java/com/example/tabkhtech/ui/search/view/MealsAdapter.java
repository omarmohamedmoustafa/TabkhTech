package com.example.tabkhtech.ui.search.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.pojos.Meal;

import java.util.ArrayList;
import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {

    private List<Meal> meals = new ArrayList<>();
    private OnMealClickListener mealClickListener;

    public MealsAdapter(List<Meal> meals, OnMealClickListener mealClickListener) {
        this.meals = meals != null ? meals : new ArrayList<>();
        this.mealClickListener = mealClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        if (meal != null && meal.getStrMeal() != null) {
            holder.title.setText(meal.getStrMeal());
        } else {
            holder.title.setText("Unknown Meal");
        }

        String imageUrl = meal != null ? meal.getStrMealThumb() : null;
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.meal)
                .error(R.drawable.meal)
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            if (meal != null) {
                mealClickListener.onMealClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setMeals(List<Meal> newMeals) {
        this.meals = newMeals != null ? newMeals : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setFilteredList(List<Meal> filteredList) {
        this.meals = filteredList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImg);
            title = itemView.findViewById(R.id.itemName);
            if (title == null) {
                throw new IllegalStateException("TextView with ID mealName not found in search_item.xml");
            }
            if (image == null) {
                throw new IllegalStateException("ImageView with ID mealImg not found in search_item.xml");
            }
        }
    }
}