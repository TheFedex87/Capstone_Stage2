package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.ui.adapters.WaiterCallsAdapter;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.WaiterViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.WaiterViewModelFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 12/07/2018.
 */

public class WaiterCallsFragment extends Fragment {
    private List<WaiterCall> calls;
    private String restaurantId;

    private boolean viewAttached = false;
    private boolean dataSet = false;

    private Context context;

    private WaiterCallsAdapter waiterCallsAdapter;

    @BindView(R.id.waiter_calls_list)
    RecyclerView waiterCalls;

    @BindView(R.id.no_calls_text)
    TextView noCallsText;

    public WaiterCallsFragment(){

    }

    public void setRestaurantId(String restaurantId){
        this.restaurantId = restaurantId;
        dataSet = true;
        if (viewAttached)
            setupViewModel(restaurantId);
    }

    private void setupViewModel(String restaurantId) {
        WaiterViewModelFactory waiterViewModelFactory = new WaiterViewModelFactory(restaurantId);
        WaiterViewModel waiterViewModel = ViewModelProviders.of(this, waiterViewModelFactory).get(WaiterViewModel.class);
        waiterViewModel.getRestaurant().observe(this, new Observer<List<WaiterCall>>() {
            @Override
            public void onChanged(@Nullable List<WaiterCall> waiterCalls) {
                waiterCallsAdapter.swapCalls(waiterCalls);
                if (waiterCalls != null && waiterCalls.size() > 0)
                    noCallsText.setVisibility(View.GONE);
                else
                    noCallsText.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setWaiterCalls(List<WaiterCall> calls){
        this.calls = calls;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.waiter_calls_fragment, container, false);


        ButterKnife.bind(this, rootView);

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .userInterfaceModule(new UserInterfaceModule(LinearLayoutManager.VERTICAL))
                .build();

        waiterCallsAdapter = userInterfaceComponent.getWaiterCallsAdapter();

        waiterCalls.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
        waiterCalls.setAdapter(waiterCallsAdapter);

        viewAttached = true;

        if(dataSet)
            setupViewModel(restaurantId);

        return rootView;
    }
}
