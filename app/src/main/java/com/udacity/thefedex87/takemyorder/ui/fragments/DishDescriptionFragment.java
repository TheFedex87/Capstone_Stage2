package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.activities.DishDescriptionActivity;
import com.udacity.thefedex87.takemyorder.ui.adapters.DishIngredientsAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 24/06/2018.
 */

public class DishDescriptionFragment extends Fragment {
    @BindView(R.id.dish_description_tv)
    TextView mealDescription;

    @BindView(R.id.ingredients_list)
    RecyclerView ingredientsList;

    @Inject
    Context context;

    private UserInterfaceComponent userInterfaceComponent;
    private NetworkComponent networkComponent;

    public DishDescriptionFragment(){

    }

    public void setFood(Food food){

        userInterfaceComponent = DaggerUserInterfaceComponent
                .builder()
                .userInterfaceModule(new UserInterfaceModule(food.getIngredients(), LinearLayoutManager.VERTICAL))
                .applicationModule(new ApplicationModule(context))
                .build();

        mealDescription.setText(food.getDescription());

        ingredientsList.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
        DishIngredientsAdapter dishIngredientsAdapter = userInterfaceComponent.getDishIngredientsAdapter();
        ingredientsList.setAdapter(dishIngredientsAdapter);
    }

    @Override
    public void onAttach(Context context) {
        TakeMyOrderApplication.appComponent().inject(this);

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

        return rootView;
    }
}
