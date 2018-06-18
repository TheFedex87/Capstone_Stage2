package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.models.Meal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 17/06/2018.
 */

public class FoodInMenuAdapter extends RecyclerView.Adapter<FoodInMenuAdapter.FoodInMenuViewHolder> {
    private List<Meal> meals;
    private Context context;

    private FirebaseStorage firebaseStorage;

    private NetworkComponent networkInterfaceComponent;

    public FoodInMenuAdapter(Context context){
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance();

        networkInterfaceComponent = DaggerNetworkComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .build();
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
    public void onBindViewHolder(@NonNull FoodInMenuViewHolder holder, int position) {

        holder.foodInMenuName.setText(meals.get(position).getName());

        if(meals.get(position).getImageName() != null && !meals.get(position).getImageName().isEmpty()){
            String imagePath = "https://firebasestorage.googleapis.com/v0/b/takemyorder-8a08a.appspot.com/o/meals_images%2F" + meals.get(position).getMealId() +  "?alt=media";
            networkInterfaceComponent.getPicasso().load(imagePath).fit().into(holder.foodImage);
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

    class FoodInMenuViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.food_in_menu_name)
        TextView foodInMenuName;

        @BindView(R.id.food_image)
        ImageView foodImage;

        @BindView(R.id.food_description)
        TextView foodDescription;

        public FoodInMenuViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
