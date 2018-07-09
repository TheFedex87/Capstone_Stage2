package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.DaggerViewModelComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkModule;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.dagger.ViewModelModule;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.ui.activities.UserRoomContainer;
import com.udacity.thefedex87.takemyorder.ui.adapters.DishIngredientsAdapter;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.DishDetailsViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.DishDetailsViewModelFactory;
import com.udacity.thefedex87.takemyorder.room.DBManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.ROTATION_Y;

/**
 * Created by feder on 24/06/2018.
 */

public class DishDescriptionFragment extends Fragment {
    private Food food;
    private String restaurantId;

    private boolean viewCreated;
    private boolean dataSet;

    @BindView(R.id.dish_description_tv)
    TextView mealDescription;

    @BindView(R.id.food_price)
    TextView foodPrice;

    @BindView(R.id.ingredients_list)
    RecyclerView ingredientsList;

    @BindView(R.id.favourite_food)
    ImageView favouriteMealImage;

    @Nullable
    @BindView(R.id.dish_description_meal_image)
    ImageView foodImage;

    @Nullable
    @BindView(R.id.food_image_to_animate)
    CircleImageView foodImageToAnimate;

    @Inject
    Context context;

    private UserInterfaceComponent userInterfaceComponent;
    private NetworkComponent networkComponent;
    private boolean isMealAFavourite = false;

    private FavouriteMeal favouriteMealFromDB;

    private AppDatabase db;

    public DishDescriptionFragment(){
    }

    public void setData(final Food food, final String restaurantId){
        this.food = food;
        this.restaurantId = restaurantId;
        dataSet = true;

        if (viewCreated)
            setUi();
    }

    @Override
    public void onAttach(Context context) {
        TakeMyOrderApplication.appComponent().inject(this);

        db = AppDatabase.getInstance(context);

        networkComponent = DaggerNetworkComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .build();

        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dish_description_fragment, container, false);

        ButterKnife.bind(this, rootView);
        viewCreated = true;

        if(dataSet)
            setUi();

        return rootView;
    }

    private void setUi(){
        //DishDetailsViewModelFactory dishDetailsViewModelFactory = new DishDetailsViewModelFactory(AppDatabase.getInstance(getActivity()), food.getMealId(), ((UserRoomContainer)getActivity()).getUserRoomId());

        DishDetailsViewModelFactory dishDetailsViewModelFactory = DaggerViewModelComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .viewModelModule(new ViewModelModule(food.getMealId(), ((UserRoomContainer)getActivity()).getUserRoomId()))
                .build()
                .getDishDetailsViewModelFactory();

        final DishDetailsViewModel dishDetailsViewModel = ViewModelProviders.of(getActivity(), dishDetailsViewModelFactory).get(DishDetailsViewModel.class);
        //TODO: check if there is another way to pass new arguments at the view model, CHECK USING TRANSORMATIONS!!!
        dishDetailsViewModel.setData(food.getMealId(), ((UserRoomContainer)getActivity()).getUserRoomId());
        dishDetailsViewModel.getUserFavouriteMealByMealId().observe(getActivity(), new Observer<FavouriteMeal>() {
            @Override
            public void onChanged(@Nullable FavouriteMeal meal) {
                if (meal != null) {
                    favouriteMealImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_fill));
                    isMealAFavourite = true;
                    favouriteMealFromDB = meal;
                } else {
                    favouriteMealImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_empty));
                    isMealAFavourite = false;
                    favouriteMealFromDB = null;
                }
            }
        });

        ApplicationModule applicationModule = new ApplicationModule(context);

        userInterfaceComponent = DaggerUserInterfaceComponent
                .builder()
                .userInterfaceModule(new UserInterfaceModule(food.getIngredients(), LinearLayoutManager.VERTICAL))
                .applicationModule(applicationModule)
                .build();

        mealDescription.setText(food.getDescription());

        if (foodImage != null){
            Picasso picasso = DaggerNetworkComponent
                    .builder()
                    .applicationModule(applicationModule)
                    .build().getPicasso();

            String imagePath = "https://firebasestorage.googleapis.com/v0/b/takemyorder-8a08a.appspot.com/o/meals_images%2F" + food.getMealId() +  "?alt=media";

            picasso.load(imagePath).into(foodImage);
            picasso.load(imagePath).into(foodImageToAnimate);
        }

        foodPrice.setText(food.getPrice() + " €");

        ingredientsList.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
        DishIngredientsAdapter dishIngredientsAdapter = userInterfaceComponent.getDishIngredientsAdapter();
        ingredientsList.setAdapter(dishIngredientsAdapter);

        //Setup favourite meal icon
        favouriteMealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final float destRotation = isMealAFavourite ? 0 : 360;
                final Drawable destDrawable = isMealAFavourite ?
                        ContextCompat.getDrawable(context, R.drawable.ic_favorite_empty) :
                        ContextCompat.getDrawable(context, R.drawable.ic_favorite_fill);



                final Interpolator interpolator = AnimationUtils.loadInterpolator(getContext(), android.R
                        .interpolator.fast_out_slow_in);

                PropertyValuesHolder favouriteIconRotation = PropertyValuesHolder.ofFloat(ROTATION_Y, 180);
                ObjectAnimator favouriteIconAnimation = ObjectAnimator.ofPropertyValuesHolder(favouriteMealImage, favouriteIconRotation);
                favouriteIconAnimation.setDuration(150);
                favouriteIconAnimation.setInterpolator(interpolator);
                favouriteIconAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animation.removeListener(this);

                        favouriteMealImage.setImageDrawable(destDrawable);

                        favouriteMealImage.animate().setInterpolator(interpolator).setDuration(150).rotationY(destRotation).start();
                    }
                });
                favouriteIconAnimation.start();

                if (!isMealAFavourite) {
                    DBManager.saveFavouritesIntoDB(db, dishDetailsViewModel, getActivity(), food, restaurantId, ((UserRoomContainer)getActivity()).getUserRoomId());
                } else{
                    DBManager.removeFromFavourite(db, favouriteMealFromDB.getId(), ((UserRoomContainer)getActivity()).getUserRoomId());
                }

                isMealAFavourite = !isMealAFavourite;
            }
        });
    }
}
