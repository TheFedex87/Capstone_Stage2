package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by federico.creti on 14/06/2018.
 */

public class FoodInOrderAdapter extends RecyclerView.Adapter<FoodInOrderAdapter.FoodInOrderViewHolder> {
    private List<CurrentOrderEntry> currentOrderEntryList;

    public void swapCurrentOrderEntryList(List<CurrentOrderEntry> currentOrderEntryList){
        this.currentOrderEntryList = currentOrderEntryList;
        notifyDataSetChanged();
    }

    public List<CurrentOrderEntry> getCurrentOrderEntryList(){
        return currentOrderEntryList;
    }

    @NonNull
    @Override
    public FoodInOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_in_order, parent, false);

        return new FoodInOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodInOrderViewHolder holder, int position) {
        holder.foodName.setText(currentOrderEntryList.get(position).getName());
        holder.foodType.setText(currentOrderEntryList.get(position).getFoodType().toString());
        holder.foodPrice.setText(String.valueOf(currentOrderEntryList.get(position).getPrice()) + "€");
    }

    @Override
    public int getItemCount() {
        if (currentOrderEntryList == null) return 0;
        return currentOrderEntryList.size();
    }

    class FoodInOrderViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.food_name)
        TextView foodName;

        @BindView(R.id.food_type)
        TextView foodType;

        @BindView(R.id.food_price)
        TextView foodPrice;

        public FoodInOrderViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
