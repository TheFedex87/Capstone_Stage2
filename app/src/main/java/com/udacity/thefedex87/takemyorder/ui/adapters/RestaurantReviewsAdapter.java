package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class RestaurantReviewsAdapter extends RecyclerView.Adapter<RestaurantReviewsAdapter.RestaurantReviewsViewHolder> {

    @NonNull
    @Override
    public RestaurantReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantReviewsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class RestaurantReviewsViewHolder extends RecyclerView.ViewHolder{

        public RestaurantReviewsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
