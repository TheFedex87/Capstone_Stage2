package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.udacity.thefedex87.takemyorder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by federico.creti on 11/06/2018.
 */

//Adapter used for the circle used inside the restaurant details fragment to show the selected restaurant image, a selected image has fill circle, otherwise a empty circle
public class PhotoIndicatorContainerAdapter extends RecyclerView.Adapter<PhotoIndicatorContainerAdapter.PhotoIndicatorContainerViewHolder> {
    private int numberOfPhoto;
    private Context context;
    private int activePhoto = 0;

    public PhotoIndicatorContainerAdapter(Context context, int numberOfPhoto) {
        this.context = context;
        this.numberOfPhoto = numberOfPhoto;
    }

    public void setActivePhoto(int activePhoto){
        this.activePhoto = activePhoto;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoIndicatorContainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.photo_indicator, parent, false);
        return new PhotoIndicatorContainerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoIndicatorContainerViewHolder holder, int position) {
        if (position != activePhoto){
            holder.indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.photo_indicator));
        } else {
            holder.indicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.photo_indicator_selected));
        }
    }

    @Override
    public int getItemCount() {
        return numberOfPhoto;
    }

    class PhotoIndicatorContainerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.indicator) ImageView indicator;

        public PhotoIndicatorContainerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
