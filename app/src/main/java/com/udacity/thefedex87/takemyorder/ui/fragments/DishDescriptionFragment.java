package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.arch.lifecycle.LiveData;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
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
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.activities.DishDescriptionActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.FavouritesFoodsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.UserRoomContainer;
import com.udacity.thefedex87.takemyorder.ui.adapters.DishIngredientsAdapter;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.DishDetailsViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.DishDetailsViewModelFactory;
import com.udacity.thefedex87.takemyorder.room.DBManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.ROTATION_Y;

/**
 * Created by feder on 24/06/2018.
 */

public class DishDescriptionFragment extends Fragment {
    private Meal meal;
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
    @BindView(R.id.dish_description_food_image_container)
    RelativeLayout foodImageContainer;

    @Nullable
    @BindView(R.id.dish_description_meal_image)
    ImageView foodImage;

    @Nullable
    @BindView(R.id.food_image_to_animate)
    CircleImageView foodImageToAnimate;

    @Nullable
    @BindView(R.id.dish_details_pb)
    ProgressBar dishDetailsProgressBar;

    @Inject
    Context context;

    private UserInterfaceComponent userInterfaceComponent;
    private NetworkComponent networkComponent;
    private boolean isMealAFavourite = false;

    private FavouriteMeal favouriteMealFromDB;

    private AppDatabase db;

    public DishDescriptionFragment(){
    }

    public void setData(final Meal meal, final String restaurantId){
        this.meal = meal;
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
        final ApplicationModule applicationModule = new ApplicationModule(context);

        DishDetailsViewModelFactory dishDetailsViewModelFactory = DaggerViewModelComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .viewModelModule(new ViewModelModule(meal.getMealId(), ((UserRoomContainer)getActivity()).getUserRoomId(), restaurantId))
                .build()
                .getDishDetailsViewModelFactory();

        final DishDetailsViewModel dishDetailsViewModel = ViewModelProviders.of(getActivity(), dishDetailsViewModelFactory).get(DishDetailsViewModel.class);
        if(meal instanceof Food) {
            //TODO: check if there is another way to pass new arguments at the view model, CHECK USING TRANSORMATIONS!!!
            dishDetailsViewModel.setData(meal.getMealId(), ((UserRoomContainer) getActivity()).getUserRoomId(), restaurantId);
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

            //Check if ingredients are set into meal, otherwise this mean we are in this activity from favourites activity or from
            //widget, and we have to load the ingredients from DB
            if(((Food) meal).getIngredients() == null || ((Food) meal).getIngredients().size() == 0){
                dishDetailsViewModel.setMealId(meal.getMealId());
                final LiveData<List<Ingredient>> ingredientsLiveData = dishDetailsViewModel.getIngredientsOfMeal();
                ingredientsLiveData.observe(getActivity(), new Observer<List<Ingredient>>() {
                    @Override
                    public void onChanged(@Nullable List<Ingredient> ingredients) {
                        ingredientsLiveData.removeObserver(this);
                        ((Food)meal).setIngredients(ingredients);

                        userInterfaceComponent = DaggerUserInterfaceComponent
                                .builder()
                                .userInterfaceModule(new UserInterfaceModule(((Food) meal).getIngredients(), LinearLayoutManager.VERTICAL))
                                .applicationModule(applicationModule)
                                .build();

                        mealDescription.setText(((Food) meal).getDescription());

                        ingredientsList.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
                        DishIngredientsAdapter dishIngredientsAdapter = userInterfaceComponent.getDishIngredientsAdapter();
                        ingredientsList.setAdapter(dishIngredientsAdapter);
                    }
                });
            } else {
                userInterfaceComponent = DaggerUserInterfaceComponent
                        .builder()
                        .userInterfaceModule(new UserInterfaceModule(((Food) meal).getIngredients(), LinearLayoutManager.VERTICAL))
                        .applicationModule(applicationModule)
                        .build();

                mealDescription.setText(((Food) meal).getDescription());

                ingredientsList.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
                DishIngredientsAdapter dishIngredientsAdapter = userInterfaceComponent.getDishIngredientsAdapter();
                ingredientsList.setAdapter(dishIngredientsAdapter);
            }
        } else {
            favouriteMealImage.setVisibility(View.GONE);
        }

        if (foodImage != null){
            if(!(getActivity() instanceof DishDescriptionActivity)) {
                dishDetailsProgressBar.setVisibility(View.VISIBLE);
                Picasso picasso = DaggerNetworkComponent
                        .builder()
                        .applicationModule(applicationModule)
                        .build().getPicasso();

                String imagePath = "https://firebasestorage.googleapis.com/v0/b/takemyorder-8a08a.appspot.com/o/meals_images%2F" + meal.getMealId() + "?alt=media";

                picasso.load(imagePath).into(foodImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        dishDetailsProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        dishDetailsProgressBar.setVisibility(View.GONE);
                    }
                });
                picasso.load(imagePath).into(foodImageToAnimate);
            } else {
                foodImageContainer.setVisibility(View.GONE);
            }
        }

        foodPrice.setText(meal.getPrice() + " €");


        //Setup favourite meal icon
        if (meal instanceof Food) {
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
                        DBManager.saveFavouritesIntoDB(db, dishDetailsViewModel, getActivity(), (Food) meal, restaurantId, ((UserRoomContainer) getActivity()).getUserRoomId());
                    } else {
                        DBManager.removeFromFavourite(db, favouriteMealFromDB.getId(), ((UserRoomContainer) getActivity()).getUserRoomId());
                    }

                    isMealAFavourite = !isMealAFavourite;
                }
            });
        }
    }
}
