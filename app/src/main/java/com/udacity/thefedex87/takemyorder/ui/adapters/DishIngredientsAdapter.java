package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 25/06/2018.
 */

public class DishIngredientsAdapter extends RecyclerView.Adapter<DishIngredientsAdapter.DishIngredientViewHolder> {
    private List<Ingredient> ingredients;

    public DishIngredientsAdapter(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DishIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient, parent, false);
        return new DishIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishIngredientViewHolder holder, int position) {
        holder.ingredientName.setText(ingredients.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    class DishIngredientViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ingredient_name)
        TextView ingredientName;

        public DishIngredientViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
