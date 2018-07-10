package com.udacity.thefedex87.takemyorder.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerViewModelComponent;
import com.udacity.thefedex87.takemyorder.dagger.ViewModelModule;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.fragments.MenuCompleteFragment;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static android.view.View.ALPHA;
import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;

public class FavouritesFoodsActivity extends AppCompatActivity implements UserRoomContainer {
    private String restaurantId;
    private MenuCompleteFragment menuCompleteFragment;
    private List<Meal> currentOrder;
    private long userRoomId;
    private boolean addingOrder;

    private HashMap<FoodTypes, List<Meal>> favourites;

    @BindView(R.id.root_container)
    CoordinatorLayout rootContainer;

    @BindView(R.id.menu_icon_container)
    FrameLayout menuIconContainer;

    @BindView(R.id.counter_container)
    FrameLayout counterContainer;

    @BindView(R.id.couter_value)
    TextView counterValue;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout
    collapsingToolbarLayout;

    @Nullable
    @BindView(R.id.add_to_order_fab)
    FloatingActionButton addToOrderFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_foods);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(CustomerMainActivity.RESTAURANT_ID_KEY)  && intent.hasExtra(CustomerMainActivity.USER_ID_KEY)){
            restaurantId = intent.getStringExtra(CustomerMainActivity.RESTAURANT_ID_KEY);
            userRoomId = intent.getLongExtra(CustomerMainActivity.USER_ID_KEY, -1);

            ButterKnife.bind(this);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            collapsingToolbarLayout.setTitle(getString(R.string.favourites));

            favourites = new HashMap<>();
            favourites.put(FoodTypes.STARTER, new ArrayList<Meal>());
            favourites.put(FoodTypes.MAINDISH, new ArrayList<Meal>());
            favourites.put(FoodTypes.SIDEDISH, new ArrayList<Meal>());
            favourites.put(FoodTypes.DESSERT, new ArrayList<Meal>());

            menuIconContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            setupViewModel();

            //This activity use the same fragment of the menu
            menuCompleteFragment = (MenuCompleteFragment)getSupportFragmentManager().findFragmentById(R.id.restaurant_menu);
        }
    }

    public long getUserRoomId(){
        return userRoomId;
    }

    private void setupViewModel() {
        //FavouritesViewModelFactory favouritesViewModelFactory = new FavouritesViewModelFactory(AppDatabase.getInstance(this), restaurantId, userRoomId);

        FavouritesViewModelFactory favouritesViewModelFactory = DaggerViewModelComponent
                .builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .viewModelModule(new ViewModelModule(restaurantId, userRoomId))
                .build()
                .getFavouritesViewModelFactory();

        final FavouritesViewModel favouritesViewModel = ViewModelProviders.of(this, favouritesViewModelFactory).get(FavouritesViewModel.class);

        //Using the viewmodel of this activity retrieve the list of user favourites
        favouritesViewModel.getFavouriteMealsOfUser().observe(this, new Observer<List<FavouriteMeal>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteMeal> favouriteMeals) {

                //Clean the list of favourites, otherwise if remove a favourite the whole list will be added again to the current favourites causing duplicates
                favourites.get(FoodTypes.STARTER).clear();
                favourites.get(FoodTypes.MAINDISH).clear();
                favourites.get(FoodTypes.SIDEDISH).clear();
                favourites.get(FoodTypes.DESSERT).clear();

                //For each favourite, create the Food entity which will be passed to the meal list
                for(final FavouriteMeal favouriteMeal : favouriteMeals){
                    final Food food = new Food();
                    food.setFoodType(favouriteMeal.getFoodType());
                    food.setImageName(favouriteMeal.getImageName());
                    food.setMealId(favouriteMeal.getMealId());
                    food.setName(favouriteMeal.getName());
                    food.setPrice(favouriteMeal.getPrice());
                    food.setDescription(favouriteMeal.getDescription());

                    //Using the viewmodle, extract the list of the ingredient of this meal, and assign it the toolbar_bg_2 entity
                    favouritesViewModel.setMealId(favouriteMeal.getMealId());
                    final LiveData<List<Ingredient>> ingredientsLiveData = favouritesViewModel.getIngredientsOfMeal();
                    ingredientsLiveData.observe(FavouritesFoodsActivity.this, new Observer<List<Ingredient>>() {
                        @Override
                        public void onChanged(@Nullable List<Ingredient> ingredients) {
                            ingredientsLiveData.removeObserver(this);
                            food.setIngredients(ingredients);
                        }
                    });
                    favourites.get(favouriteMeal.getFoodType()).add(food);
                }

                menuCompleteFragment.setMenu(favourites, restaurantId);
                //if(currentOrder != null) menuCompleteFragment.setCurrentOrder(currentOrder);

                //If we are in a two panels layout
                if (addToOrderFab != null){
                    setupFab();
                }
            }
        });

        //Retrieve the current order from ViewModel
        favouritesViewModel.getCurrentOrderList().observe(this, new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> currentOrderEntries) {
                //TODO: gestire se menuCompleteFragment fosse null perchè non ancora creato e/o agganciato
                //if (menuCompleteFragment != null) menuCompleteFragment.setCurrentOrder(currentOrderEntries);
                menuCompleteFragment.setCurrentOrder(currentOrderEntries);
                currentOrder = currentOrderEntries;
                if (currentOrderEntries.size() > 0) {
                    counterContainer.setVisibility(View.VISIBLE);
                }
                else {
                    counterContainer.setVisibility(View.GONE);
                    return;
                }

                counterValue.setText(String.valueOf(currentOrderEntries.size() <= 99 ? currentOrderEntries.size() : 99));

                AnimatorSet counterAnimation = (AnimatorSet) AnimatorInflater
                        .loadAnimator(FavouritesFoodsActivity.this, R.animator.food_counter_animation);
                counterAnimation.setTarget(counterContainer);

                counterAnimation.start();
            }
        });
    }

    private void setupFab(){

        addToOrderFab.setOnClickListener(new View.OnClickListener() {
            @BindView(R.id.dish_description_meal_image)
            ImageView dishDescriptionMealImage;

            @BindView(R.id.dish_description_food_image_container)
            RelativeLayout dishDescriptionFoodImageContainer;

            @BindView(R.id.food_image_to_animate)
            CircleImageView foodImageToAnimate;

            @Override
            public void onClick(View view) {
                if(!addingOrder) {
                    final Meal meal = menuCompleteFragment.getSelectedMeal();
                    if (meal != null) {
                        addingOrder = true;
                        final AppDatabase db = AppDatabase.getInstance(FavouritesFoodsActivity.this);

                        //ButterKnife.bind(this, menuCompleteFragment.getCurrentTabFragment().getView());
                        View viewRoot = menuCompleteFragment.getCurrentTabFragment().getView();
                        dishDescriptionFoodImageContainer = viewRoot.findViewById(R.id.dish_description_food_image_container);
                        dishDescriptionMealImage = viewRoot.findViewById(R.id.dish_description_meal_image);
                        foodImageToAnimate = viewRoot.findViewById(R.id.food_image_to_animate);

                        rootContainer.getOverlay().add(foodImageToAnimate);

                        final int[] parentPos = new int[2];
                        rootContainer.getLocationOnScreen(parentPos);

                        final int[] fabPos = new int[2];
                        addToOrderFab.getLocationOnScreen(fabPos);

                        final int[] foodImagePos = new int[2];
                        foodImageToAnimate.getLocationOnScreen(foodImagePos);

                        PropertyValuesHolder alphaFoodImage = PropertyValuesHolder.ofFloat(ALPHA, 0);
                        PropertyValuesHolder scaleXFoodImage = PropertyValuesHolder.ofFloat(SCALE_X, 0.2f);
                        PropertyValuesHolder scaleYFoodImage = PropertyValuesHolder.ofFloat(SCALE_Y, 0.2f);
                        final ObjectAnimator scaleFoodImageAnimator = ObjectAnimator.ofPropertyValuesHolder(dishDescriptionMealImage, alphaFoodImage, scaleXFoodImage, scaleYFoodImage);
                        scaleFoodImageAnimator.setDuration(200);
                        scaleFoodImageAnimator.start();

                        PropertyValuesHolder alphaImageFoodToAnimate = PropertyValuesHolder.ofFloat(ALPHA, 1);
                        PropertyValuesHolder transXImageFoodToAnimate = PropertyValuesHolder.ofFloat(TRANSLATION_X, fabPos[0] - foodImagePos[0] - parentPos[0]);
                        PropertyValuesHolder transYImageFoodToAnimate = PropertyValuesHolder.ofFloat(TRANSLATION_Y, fabPos[1] - foodImagePos[1] - parentPos[1]);

                        ObjectAnimator imageFoodToAnimateAnimation = ObjectAnimator.ofPropertyValuesHolder(foodImageToAnimate, alphaImageFoodToAnimate, transXImageFoodToAnimate, transYImageFoodToAnimate);
                        imageFoodToAnimateAnimation.setDuration(400);
                        imageFoodToAnimateAnimation.setStartDelay(100);
                        imageFoodToAnimateAnimation.start();


                        ObjectAnimator foodImageToAnimateAlphaAnimation = ObjectAnimator.ofFloat(foodImageToAnimate, ALPHA, 0);
                        foodImageToAnimateAlphaAnimation.setDuration(100);
                        foodImageToAnimateAlphaAnimation.setStartDelay(400);
                        foodImageToAnimateAlphaAnimation.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                animation.removeListener(this);

                                rootContainer.getOverlay().remove(foodImageToAnimate);
                                dishDescriptionFoodImageContainer.addView(foodImageToAnimate);

                                foodImageToAnimate.animate().translationX(0).setDuration(0);
                                foodImageToAnimate.animate().translationY(0).setDuration(0);

                                scaleFoodImageAnimator.reverse();
                                scaleFoodImageAnimator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        addingOrder = false;
                                    }
                                });
                            }
                        });
                        foodImageToAnimateAlphaAnimation.start();

                        meal.setUserId(userRoomId);
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                db.currentOrderDao().insertFood(meal);
                            }
                        });
                    } else {
                        Timber.w(getString(R.string.no_food_selected));
                        Toast.makeText(FavouritesFoodsActivity.this, getString(R.string.no_food_selected), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
