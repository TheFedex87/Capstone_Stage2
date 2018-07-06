package com.udacity.thefedex87.takemyorder.room;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMealIngredientJoin;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMealUserJoin;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.DishDetailsViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModel;

import java.util.List;

/**
 * Created by federico.creti on 29/06/2018.
 */

public final class DBManager {

    public static void saveFavouritesIntoDB(final AppDatabase db, final ViewModel viewModel, final LifecycleOwner lifecycleOwner, final Food food, final String restaurantId, final long userId){
        final LiveData<FavouriteMeal> favouriteMealLiveData;

        //Cast the ViewModel to the specific ViewModel
        if (viewModel instanceof DishDetailsViewModel) {
            favouriteMealLiveData = ((DishDetailsViewModel) viewModel).getFavouriteMealByMealId();
        }
        else if(viewModel instanceof RestaurantMenuViewModel) {
            ((RestaurantMenuViewModel) viewModel).setData(food.getMealId(), restaurantId, -1);
            favouriteMealLiveData = ((RestaurantMenuViewModel) viewModel).getFavouriteMealByMealId();
        } else {
            favouriteMealLiveData = null;
        }

        //Check if the food is already into the DB of favourites, if so I add only the join beetween userId and mealId, otherwise
        //I add the meal into the DB of favourites
        favouriteMealLiveData.observe(lifecycleOwner, new Observer<FavouriteMeal>() {
            @Override
            public void onChanged(@Nullable final FavouriteMeal favouriteMeal) {
                favouriteMealLiveData.removeObserver(this);
                final long favouriteMealId;
                if (favouriteMeal == null){
                    final FavouriteMeal newFavouriteMeal = new FavouriteMeal();
                    newFavouriteMeal.setFoodType(food.getFoodType());
                    newFavouriteMeal.setImageName(food.getImageName());
                    newFavouriteMeal.setMealId(food.getMealId());
                    newFavouriteMeal.setName(food.getName());
                    newFavouriteMeal.setPrice(food.getPrice());
                    newFavouriteMeal.setRestaurantId(restaurantId);
                    newFavouriteMeal.setDescription(food.getDescription());

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            long favouriteMealId = db.favouriteMealsDao().insertFavouriteMeal(newFavouriteMeal);
                            insertFavouriteDetails(db, userId, favouriteMealId, viewModel, food, lifecycleOwner);
                        }
                    });
                } else {
                    favouriteMealId = favouriteMeal.getId();
                    insertFavouriteDetails(db, userId, favouriteMealId, viewModel, food, lifecycleOwner);
                }
            }
        });


    }

    private static void insertFavouriteDetails(final AppDatabase db, long userId, long favouriteMealId, ViewModel viewModel, final Food food, LifecycleOwner lifecycleOwner){
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

    public static void removeFromFavourite(final AppDatabase db, final long favouriteMealFromDBId, final long userRoomId){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //db.favouriteMealsDao().deleteFavouriteMeal(favouriteMealFromDB);
                db.favouriteMealsDao().deleteFavouriteMealUserJoin(userRoomId, favouriteMealFromDBId);
            }
        });
    }
}
