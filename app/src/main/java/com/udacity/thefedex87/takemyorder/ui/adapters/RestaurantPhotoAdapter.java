package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;

import java.util.List;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class RestaurantPhotoAdapter extends PagerAdapter {
    private List<String> photoUrls;
    private Context context;

    private NetworkComponent networkComponent;

    public RestaurantPhotoAdapter(@NonNull List<String> photoUrls, Context context){
        this.photoUrls = photoUrls;
        this.context = context;

        networkComponent = DaggerNetworkComponent.builder().applicationModule(new ApplicationModule(context)).build();
    }

    @Override
    public int getCount() {
        return photoUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View newView = null;

        LayoutInflater inflater = (LayoutInflater.from(context));
        newView = inflater.inflate(R.layout.restaurant_image, container, false);

        ImageView imageView = newView.findViewById(R.id.restaurant_image);
        networkComponent.getPicasso().load(photoUrls.get(position)).into(imageView);

        container.addView(newView);

        return newView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
