package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.arch.lifecycle.Observer;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderGrouped;
import com.udacity.thefedex87.takemyorder.ui.activities.RestaurantMenuActivity;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModelFactory;

import java.util.HashMap;
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

    @NonNull
    @Override
    public FoodInMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_in_menu, parent, false);

        return new FoodInMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodInMenuViewHolder holder, final int position) {
//

        restaurantMenuViewModelFactory = new RestaurantMenuViewModelFactory(AppDatabase.getInstance(parentActivity), null);
        restaurantMenuViewModel = ViewModelProviders.of(parentActivity, restaurantMenuViewModelFactory).get(RestaurantMenuViewModel.class);
        restaurantMenuViewModel.setFoodId(meals.get(position).getMealId());
        restaurantMenuViewModel.getCurrentOrdserListByMealId().observe(parentActivity, new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> currentOrderEntries) {
                if (currentOrderEntries.size() > 0){
                    holder.foodCountContainer.setVisibility(View.VISIBLE);
                    holder.subtractFood.setVisibility(View.VISIBLE);


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
                    holder.subtractFood.setVisibility(View.GONE);
                }
            }
        });

        holder.foodInMenuName.setText(meals.get(position).getName());
        holder.foodPrice.setText(meals.get(position).getPrice() + "€");

        if(meals.get(position).getImageName() != null && !meals.get(position).getImageName().isEmpty()){
            holder.foodImageProgressBar.setVisibility(View.VISIBLE);
            String imagePath = "https://firebasestorage.googleapis.com/v0/b/takemyorder-8a08a.appspot.com/o/meals_images%2F" + meals.get(position).getMealId() +  "?alt=media";
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

        if (meals.get(position) instanceof Food) {
            Food food = (Food) meals.get(position);
            holder.foodDescription.setText(food.getDescription());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setTransitionName(holder.foodImage, "foodTransition");
        }
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
    }

    class FoodInMenuViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.food_image_container)
        RelativeLayout foodImageContainer;

        @BindView(R.id.food_in_menu_name)
        TextView foodInMenuName;

        @BindView(R.id.food_price)
        TextView foodPrice;

        @BindView(R.id.food_image)
        ImageView foodImage;

        @BindView(R.id.food_image_to_animate)
        CircleImageView foodImageToAnimate;

        @BindView(R.id.food_description)
        TextView foodDescription;

        @BindView(R.id.food_image_pb)
        ProgressBar foodImageProgressBar;

        @BindView(R.id.add_to_current_order)
        TextView addToCurrentOrder;

        @BindView(R.id.meal_in_order_counter_container)
        FrameLayout foodCountContainer;

        @BindView(R.id.food_id_count_in_current_order)
        TextView foodCount;

        @BindView(R.id.show_food_details)
        TextView showDishDetails;

        @BindView(R.id.subtract_food)
        TextView subtractFood;

        private ViewGroup viewGroup;

        public FoodInMenuViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            addToCurrentOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    foodInMenuActionClick.addOrderClick(meals.get(getAdapterPosition()), view, foodImageToAnimate, foodImageContainer, foodImage);
                }
            });

            subtractFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foodInMenuActionClick.subtractFood(meals.get(getAdapterPosition()));
                }
            });

            showDishDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foodInMenuActionClick.showDishDetails(meals.get(getAdapterPosition()), foodImage);
                }
            });
        }
    }
}
