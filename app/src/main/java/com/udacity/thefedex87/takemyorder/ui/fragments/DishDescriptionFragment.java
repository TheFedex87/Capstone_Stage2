package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 24/06/2018.
 */

public class DishDescriptionFragment extends Fragment {
    @BindView(R.id.dish_description)
    TextView mealDescription;

    @Inject
    Context context;

    private UserInterfaceComponent userInterfaceComponent;
    private NetworkComponent networkComponent;

    private Meal food;

    public DishDescriptionFragment(){

    }

    public void setFood(Food food){
        this.food = food;

        mealDescription.setText(food.getDescription());
    }

    @Override
    public void onAttach(Context context) {
        TakeMyOrderApplication.appComponent().inject(this);

        userInterfaceComponent = DaggerUserInterfaceComponent
                .builder()
                .userInterfaceModule(new UserInterfaceModule(null, null, null, 0))
                .applicationModule(new ApplicationModule(context))
                .build();

        networkComponent = DaggerNetworkComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .build();

        super.onAttach(context);
        //((DishDescriptionActivity)context).fragmentLoaded();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dish_description_fragment, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }
}
