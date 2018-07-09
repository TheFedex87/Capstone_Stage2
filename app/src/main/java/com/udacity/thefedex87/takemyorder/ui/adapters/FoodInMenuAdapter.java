package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.DaggerViewModelComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.ViewModelModule;
import com.udacity.thefedex87.takemyorder.models.Drink;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.activities.UserRoomContainer;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModelFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feder on 17/06/2018.
 */

public class FoodInMenuAdapter extends RecyclerView.Adapter<FoodInMenuAdapter.FoodInMenuViewHolder> {
    private List<Meal> meals;
    private Context context;

    private AppCompatActivity parentActivity;

    private FirebaseStorage firebaseStorage;

    private NetworkComponent networkInterfaceComponent;

    private FoodInMenuActionClick foodInMenuActionClick;

    private RestaurantMenuViewModelFactory restaurantMenuViewModelFactory;
    private RestaurantMenuViewModel restaurantMenuViewModel;

    private String restaurantId;

    private boolean isTwoPanelsMode;

    private int selectedIndex = -1;

    public FoodInMenuAdapter(Context context, FoodInMenuActionClick foodInMenuActionClick, AppCompatActivity parentActivity){
        this.parentActivity = parentActivity;
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance();

        networkInterfaceComponent = DaggerNetworkComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .build();

        this.foodInMenuActionClick = foodInMenuActionClick;


    }

    public void setMeals(List<Meal> meals){
        this.meals = meals;
        notifyDataSetChanged();
    }

    public Meal getSelectedMeal(){
        if (selectedIndex < 0) return null;
        return meals.get(selectedIndex);
    }

    public void setIsTwoPanelsMode(boolean isTwoPanelsMode){
        this.isTwoPanelsMode = isTwoPanelsMode;
    }

    public void setRestaurantId(String restaurantId){
        this.restaurantId = restaurantId;
    }

    @NonNull
    @Override
    public FoodInMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_in_menu, parent, false);

        return new FoodInMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodInMenuViewHolder holder, final int position) {

        if (!isTwoPanelsMode) {
            //If this meal is a drink, hide the add to favourite icon and the detials icon
            if (meals.get(position) instanceof Drink) {
                holder.favouriteFood.setVisibility(View.GONE);
                holder.showDishDetails.setVisibility(View.GONE);
            } else if (meals.get(position) instanceof Food) {
                holder.favouriteFood.setVisibility(View.VISIBLE);
                holder.showDishDetails.setVisibility(View.VISIBLE);

                Food food = (Food) meals.get(position);
                holder.foodDescription.setText(food.getDescription());
            }
        }

        if(isTwoPanelsMode){
            if (selectedIndex == position){
                holder.foodInMenuContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            } else {
                holder.foodInMenuContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            }
        }

        //restaurantMenuViewModelFactory = new RestaurantMenuViewModelFactory(AppDatabase.getInstance(parentActivity), restaurantId, ((UserRoomContainer)parentActivity).getUserRoomId());
        restaurantMenuViewModelFactory = DaggerViewModelComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .viewModelModule(new ViewModelModule(restaurantId, null, ((UserRoomContainer)parentActivity).getUserRoomId()))
                .build()
                .getRestaurantMenuViewModelFactory();

        restaurantMenuViewModel = ViewModelProviders.of(parentActivity, restaurantMenuViewModelFactory).get(RestaurantMenuViewModel.class);
        restaurantMenuViewModel.setData(meals.get(position).getMealId(), restaurantId, ((UserRoomContainer)parentActivity).getUserRoomId());

        //Through the view model, retrieve the food with the meal id which is the same of the current mealid, this allow me to show the counter which show the number of this
        //food added to the current order
        restaurantMenuViewModel.getCurrentOrdserListByMealId().observe(parentActivity, new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> currentOrderEntries) {
                if (currentOrderEntries.size() > 0){
                    holder.foodCountContainer.setVisibility(View.VISIBLE);
                    if(!isTwoPanelsMode) holder.subtractFood.setVisibility(View.VISIBLE);

                    if (holder.foodCount.getText().toString().isEmpty()) holder.foodCount.setText("0");
                    if (Integer.parseInt(holder.foodCount.getText().toString()) != currentOrderEntries.size()) {
                        holder.foodCount.setText(String.valueOf(currentOrderEntries.size()));
                        
                        AnimatorSet counterAnimation = (AnimatorSet) AnimatorInflater
                                .loadAnimator(parentActivity, R.animator.food_counter_animation);
                        counterAnimation.setTarget(holder.foodCountContainer);
                        counterAnimation.start();
                    }
                } else {
                    holder.foodCountContainer.setVisibility(View.GONE);
                    if(!isTwoPanelsMode) holder.subtractFood.setVisibility(View.GONE);
                }
            }
        });

        //Check if this food is a favourite food for the current user, if it is show the icon of added to favourite otherwise the icon which allow the user to add to the favourite
        if (!isTwoPanelsMode) {
            restaurantMenuViewModel.getUserFavouriteMealByMealId().observe(parentActivity, new Observer<FavouriteMeal>() {
                @Override
                public void onChanged(@Nullable final FavouriteMeal favouriteMeal) {
                    if (favouriteMeal != null) {
                        holder.favouriteFood.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_favorite_fill));
                    } else {
                        holder.favouriteFood.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_favorite_empty));
                    }

                    //Set the clicklistener fot the add/remove to favourite, passing it the favouriteMeal, if it is null, the food is not into favourite
                    holder.favouriteFood.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            foodInMenuActionClick.addRemoveFavourite((Food) meals.get(position), favouriteMeal, restaurantMenuViewModel);
                        }
                    });
                }
            });
        }

        holder.foodInMenuName.setText(meals.get(position).getName());

        if (!isTwoPanelsMode) {
            holder.foodPrice.setText(meals.get(position).getPrice() + "€");
        }

        //If the food as a set image, I load it using picasso
        if (!isTwoPanelsMode) {
            if (meals.get(position).getImageName() != null && !meals.get(position).getImageName().isEmpty()) {
                holder.foodImageProgressBar.setVisibility(View.VISIBLE);
                String imagePath = "https://firebasestorage.googleapis.com/v0/b/takemyorder-8a08a.appspot.com/o/meals_images%2F" + meals.get(position).getMealId() + "?alt=media";
                networkInterfaceComponent.getPicasso().load(imagePath).fit().into(holder.foodImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.foodImageProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.foodImageProgressBar.setVisibility(View.GONE);
                    }
                });
                networkInterfaceComponent.getPicasso().load(imagePath).fit().into(holder.foodImageToAnimate);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setTransitionName(holder.foodImage, "foodTransition");
            }
        }

//        if (isTwoPanelsMode && selectedIndex < 0){
//            selectedIndex = 0;
//            foodInMenuActionClick.showDishDetails(meals.get(selectedIndex), holder.foodImage);
//            holder.foodInMenuContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
//        }
    }

    @Override
    public int getItemCount() {
        if(meals == null) return 0;
        return meals.size();
    }

    public interface FoodInMenuActionClick{
        void addOrderClick(Meal selectedMeal, View sender, View imageView, ViewGroup foodImageContainer, ImageView originalImage);
        void subtractFood(Meal selectedMeal);
        void showDishDetails(Meal meal, ImageView imageView);
        void addRemoveFavourite(Food food, FavouriteMeal favouriteMealFromDB, ViewModel viewModel);
    }

    class FoodInMenuViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.food_in_menu_container)
        LinearLayout foodInMenuContainer;

        @Nullable
        @BindView(R.id.food_image_container)
        RelativeLayout foodImageContainer;

        @BindView(R.id.food_in_menu_name)
        TextView foodInMenuName;

        @Nullable
        @BindView(R.id.food_price)
        TextView foodPrice;

        @Nullable
        @BindView(R.id.food_image)
        ImageView foodImage;

        @Nullable
        @BindView(R.id.food_image_to_animate)
        CircleImageView foodImageToAnimate;

        @Nullable
        @BindView(R.id.food_description)
        TextView foodDescription;

        @Nullable
        @BindView(R.id.food_image_pb)
        ProgressBar foodImageProgressBar;

        @Nullable
        @BindView(R.id.add_to_current_order)
        TextView addToCurrentOrder;

        @Nullable
        @BindView(R.id.meal_in_order_counter_container)
        FrameLayout foodCountContainer;

        @BindView(R.id.food_id_count_in_current_order)
        TextView foodCount;

        @Nullable
        @BindView(R.id.show_food_details)
        TextView showDishDetails;

        @Nullable
        @BindView(R.id.subtract_food)
        TextView subtractFood;

        @Nullable
        @BindView(R.id.favourite_food)
        ImageView favouriteFood;

        private ViewGroup viewGroup;

        public FoodInMenuViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            if (!isTwoPanelsMode) {
                addToCurrentOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        foodInMenuActionClick.addOrderClick(meals.get(getAdapterPosition()), view, foodImageToAnimate, foodImageContainer, foodImage);
                        selectedIndex = getAdapterPosition();
                        if (isTwoPanelsMode) {
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            if (!isTwoPanelsMode) {
                subtractFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        foodInMenuActionClick.subtractFood(meals.get(getAdapterPosition()));
                        selectedIndex = getAdapterPosition();
                        if (isTwoPanelsMode) {
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            if (!isTwoPanelsMode) {
                showDishDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        foodInMenuActionClick.showDishDetails(meals.get(getAdapterPosition()), foodImage);
                        selectedIndex = getAdapterPosition();
                        if (isTwoPanelsMode) {
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            if (isTwoPanelsMode) {
                foodInMenuContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedIndex = getAdapterPosition();
                        if (isTwoPanelsMode) {
                            foodInMenuActionClick.showDishDetails(meals.get(getAdapterPosition()), foodImage);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        }
    }
}
