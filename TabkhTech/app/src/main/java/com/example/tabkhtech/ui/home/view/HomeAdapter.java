package com.example.tabkhtech.ui.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.pojos.RecentMeal;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    List<RecentMeal> recentMeals;
    OnMealClickListener mealClickListener;

    public HomeAdapter(List<RecentMeal> recentMeals, OnMealClickListener mealClickListener) {
        this.recentMeals = recentMeals;
        this.mealClickListener = mealClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentMeal meal = recentMeals.get(position);
        holder.title.setText(meal.getStrMeal());
        String imageUrl = meal.getStrMealThumb();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            if (meal != null) {
                mealClickListener.onMealClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentMeals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recent_meal_image);
            title = itemView.findViewById(R.id.recent_meal_name);
        }
    }

    public void updateMeals(List<RecentMeal> newMeals) {
        this.recentMeals = newMeals;
        notifyDataSetChanged();
    }
}