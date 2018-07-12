package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.WaiterViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.WaiterViewModelFactory;

import java.util.List;

/**
 * Created by feder on 12/07/2018.
 */

public class WaiterReadyOrderFragment extends Fragment {
//    private List<> calls;
    private String restaurantId;

    private boolean viewAttached = false;
    private boolean dataSet = false;

    public WaiterReadyOrderFragment(){

    }

    public void setRestaurantId(String restaurantId){
        this.restaurantId = restaurantId;
        dataSet = true;

        if(viewAttached)
            setupViewModel(restaurantId);
    }

    private void setupViewModel(String restaurantId) {
        WaiterViewModelFactory waiterViewModelFactory = new WaiterViewModelFactory(restaurantId);
        WaiterViewModel waiterViewModel = ViewModelProviders.of(this, waiterViewModelFactory).get(WaiterViewModel.class);
        waiterViewModel.getRestaurant().observe(this, new Observer<List<WaiterCall>>() {
            @Override
            public void onChanged(@Nullable List<WaiterCall> waiterCalls) {
                int f = 4;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.waiter_ready_orders_fragment, container, false);

        viewAttached = true;

        if(dataSet)
            setupViewModel(restaurantId);

        return rootView;
    }
}
