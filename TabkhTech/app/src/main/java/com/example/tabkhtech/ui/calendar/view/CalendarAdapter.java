package com.example.tabkhtech.ui.calendar.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tabkhtech.R;
import com.example.tabkhtech.model.pojos.SchedMeal;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    List<SchedMeal> meals;
    OnMealClickListener onMealClickListener;
    OnMealDeleteListener onMealDeleteListener;


    public CalendarAdapter(List<SchedMeal> meals, OnMealClickListener onMealClickListener, OnMealDeleteListener onMealDeleteListener) {
        this.meals = meals;
        this.onMealClickListener = onMealClickListener;
        this.onMealDeleteListener = onMealDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SchedMeal meal = meals.get(position);
        holder.textView.setText(meal.getStrMeal());
        Glide.with(holder.itemView.getContext())
                .load(meal.getStrMealThumb())
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> onMealClickListener.onMealClick(meal));
        holder.deleteButton.setOnClickListener(v -> onMealDeleteListener.onMealDelete(meal));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void updateList(List<SchedMeal> newList) {
        meals = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImg);
            textView = itemView.findViewById(R.id.itemName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}