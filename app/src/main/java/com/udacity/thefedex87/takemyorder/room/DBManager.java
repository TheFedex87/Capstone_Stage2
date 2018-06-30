package com.udacity.thefedex87.takemyorder.room;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMealIngredientJoin;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMealUserJoin;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.DishDetailsViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModel;

import java.util.concurrent.Executor;

import static android.view.View.ALPHA;
import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;

/**
 * Created by federico.creti on 29/06/2018.
 */

public final class DBManager {
    public static void saveFavouritesIntoDB(final AppDatabase db, final ViewModel viewModel, final LifecycleOwner lifecycleOwner, final Food food, String restaurantId, final long userId){
        final FavouriteMeal favouriteMeal = new FavouriteMeal();
        favouriteMeal.setFoodType(food.getFoodType());
        favouriteMeal.setImageName(food.getImageName());
        favouriteMeal.setMealId(food.getMealId());
        favouriteMeal.setName(food.getName());
        favouriteMeal.setPrice(food.getPrice());
        favouriteMeal.setRestaurantId(restaurantId);
        favouriteMeal.setDescription(food.getDescription());


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long favouriteMealId = db.favouriteMealsDao().insertFavouriteMeal(favouriteMeal);

                //Insert into the FavouriteMealUserJoin the new insert favourite meal and the current logged user
                final FavouriteMealUserJoin favouriteMealUserJoin = new FavouriteMealUserJoin(userId, favouriteMealId);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        db.favouriteMealsDao().insertMealUser(favouriteMealUserJoin);
                    }
                });


                for(final Ingredient ingredient : food.getIngredients()){
                    final LiveData<Ingredient> ingredientLiveData;

                    if (viewModel instanceof DishDetailsViewModel) {
                        ((DishDetailsViewModel) viewModel).setIngredientName(ingredient.getIngredientName());
                        ingredientLiveData = ((DishDetailsViewModel) viewModel).getIngredient();
                    }
                    else if(viewModel instanceof RestaurantMenuViewModel) {
                        ((RestaurantMenuViewModel) viewModel).setIngredientName(ingredient.getIngredientName());
                        ingredientLiveData = ((RestaurantMenuViewModel) viewModel).getIngredient();
                    } else{
                        ingredientLiveData = null;
                    }

                    if (ingredientLiveData != null) {
                        ingredientLiveData.observe(lifecycleOwner, new Observer<Ingredient>() {
                            @Override
                            public void onChanged(@Nullable com.udacity.thefedex87.takemyorder.room.entity.Ingredient ingredientTmp) {
                                final com.udacity.thefedex87.takemyorder.room.entity.Ingredient ingredientIntoDB;

                                ingredientLiveData.removeObserver(this);

                                if (ingredientTmp != null) {
                                    ingredientIntoDB = ingredientTmp;
                                } else {
                                    ingredientIntoDB = new com.udacity.thefedex87.takemyorder.room.entity.Ingredient();

                                    ingredientIntoDB.setIngredientName(ingredient.getIngredientName());

                                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            db.favouriteMealsDao().insertIngredient(ingredientIntoDB);
                                        }
                                    });
                                }

                                final FavouriteMealIngredientJoin favouriteMealIngredientJoin = new FavouriteMealIngredientJoin(ingredientIntoDB.getIngredientName(),
                                        food.getMealId());

                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        db.favouriteMealsDao().insertMealIngredient(favouriteMealIngredientJoin);
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
    }

    public static void removeFromFavourite(final AppDatabase db, final FavouriteMeal favouriteMealFromDB){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.favouriteMealsDao().deleteFavouriteMeal(favouriteMealFromDB);
            }
        });
    }
}
