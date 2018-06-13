package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class RestaurantPhotoAdapter extends PagerAdapter {
    private List<String> photoUrls;
    private Context context;
    private ImageLoadingState imageLoadState;

    private NetworkComponent networkComponent;

    public RestaurantPhotoAdapter(@NonNull List<String> photoUrls, ImageLoadingState imageLoadState, Context context){
        this.photoUrls = photoUrls;
        this.context = context;
        this.imageLoadState = imageLoadState;

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

        ImageView restaurantImage = newView.findViewById(R.id.restaurant_image);
        final ProgressBar progressBar = newView.findViewById(R.id.progress_bar);

        if (imageLoadState != null) imageLoadState.imageLoadStarting();
        networkComponent.getPicasso().load(photoUrls.get(position)).into(restaurantImage, new Callback() {
            @Override
            public void onSuccess() {
                Timber.d("Image loading completed");
                progressBar.setVisibility(View.GONE);
                if (imageLoadState != null) imageLoadState.imageLoadCompleted();
            }

            @Override
            public void onError() {
                Timber.e("Image loading error");
                progressBar.setVisibility(View.GONE);
                if (imageLoadState != null) imageLoadState.imageLoadCompleted();
            }
        });

        container.addView(newView);

        return newView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface ImageLoadingState {
        void imageLoadStarting();
        void imageLoadCompleted();
    }
}
