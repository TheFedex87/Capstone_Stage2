package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderGrouped;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 03/07/2018.
 */

//Adapter used to display the list of food inside ther checkout activity
public class CheckoutOrderAdapter extends RecyclerView.Adapter<CheckoutOrderAdapter.OrderSummaryViewHolder> {
    private List<CurrentOrderGrouped> meals;

    public CheckoutOrderAdapter(List<CurrentOrderGrouped> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public OrderSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_summary_row, parent, false);

        return new OrderSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryViewHolder holder, int position) {
        holder.foodName.setText(meals.get(position).getName());
        holder.foodQuantity.setText("x " + meals.get(position).getCount());
        holder.foodPrice.setText(String.valueOf(meals.get(position).getPrice() * meals.get(position).getCount()) + " €");
    }

    @Override
    public int getItemCount() {
        return meals == null ? 0 : meals.size();
    }

    class OrderSummaryViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.food_name)
        TextView foodName;

        @BindView(R.id.food_quantity)
        TextView foodQuantity;

        @BindView(R.id.food_price)
        TextView foodPrice;

        public OrderSummaryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
