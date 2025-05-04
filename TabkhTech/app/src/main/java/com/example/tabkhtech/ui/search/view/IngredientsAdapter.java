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
import com.example.tabkhtech.model.pojos.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    List<Ingredient> ingredients;

    OnIngredientClickListener ingredientClickListener;


    public IngredientsAdapter(List<Ingredient> ingredients, OnIngredientClickListener ingredientClickListener) {
        this.ingredients = ingredients;
        this.ingredientClickListener = ingredientClickListener;

    }
    public void setFilteredList(List<Ingredient> filteredList) {
        this.ingredients = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.title.setText(ingredient.getStrIngredient());
        String ingredientName = ingredient.getStrIngredient().replace(" ", "_");
        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredientName + "-Small.png";

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientClickListener.onIngredientClick(ingredient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImg);
            title = itemView.findViewById(R.id.itemName);
        }

    }
}
