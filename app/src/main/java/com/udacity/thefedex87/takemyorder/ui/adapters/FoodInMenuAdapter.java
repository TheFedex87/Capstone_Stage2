package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.udacity.thefedex87.takemyorder.models.Meal;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderGrouped;

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
    private HashMap<String, Integer> mealsCountInCurrentOrder;

    private FirebaseStorage firebaseStorage;

    private NetworkComponent networkInterfaceComponent;

    FoodInMenuActionClick foodInMenuActionClick;

    public FoodInMenuAdapter(Context context, FoodInMenuActionClick foodInMenuActionClick){
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance();

        networkInterfaceComponent = DaggerNetworkComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .build();

        this.foodInMenuActionClick = foodInMenuActionClick;
        mealsCountInCurrentOrder = new HashMap<>();
    }

    public void setMeals(List<Meal> meals){
        this.meals = meals;
        notifyDataSetChanged();
    }

    public void setMealsCount(List<CurrentOrderGrouped> mealsCount){
        for (CurrentOrderGrouped foodId: mealsCount ) {
            mealsCountInCurrentOrder.put(foodId.getMealId(), foodId.getCount());
        }
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
    public void onBindViewHolder(@NonNull final FoodInMenuViewHolder holder, int position) {
        if(mealsCountInCurrentOrder.containsKey(meals.get(position).getMealId())){
            holder.foodCount.setText(String.valueOf(mealsCountInCurrentOrder.get(meals.get(position).getMealId())));
        }

        holder.foodInMenuName.setText(meals.get(position).getName());

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
    }

    @Override
    public int getItemCount() {
        if(meals == null) return 0;
        return meals.size();
    }

    public interface FoodInMenuActionClick{
        void addOrderClick(Meal selectedMeal, View sender, View imageView, ViewGroup foodImageContainer, ImageView originalImage);
    }

    class FoodInMenuViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.food_image_container)
        RelativeLayout foodImageContainer;

        @BindView(R.id.food_in_menu_name)
        TextView foodInMenuName;

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

        @BindView(R.id.food_id_count_in_current_order)
        TextView foodCount;

        private ViewGroup viewGroup;

        public FoodInMenuViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

//            final ViewGroup parentViewGroup = (ViewGroup)foodImage
//                    .getParent().getParent().getParent().getParent()
//                    .getParent().getParent().getParent().getParent()
//                    .getParent();
//            parentViewGroup.getOverlay().add(foodImageToAnimate);

            addToCurrentOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    foodInMenuActionClick.addOrderClick(meals.get(getAdapterPosition()), view, foodImageToAnimate, foodImageContainer, foodImage);
                    //TransitionManager.go(Scene.getSceneForLayout((ViewGroup)context);
                }
            });
        }
    }
}
