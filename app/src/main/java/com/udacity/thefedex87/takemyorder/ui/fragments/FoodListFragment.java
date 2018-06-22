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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Meal;
import com.udacity.thefedex87.takemyorder.models.Order;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.ui.activities.RestaurantDetailsActivity;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInOrderAdapter;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by federico.creti on 14/06/2018.
 */

public class FoodListFragment extends Fragment {
    private Order order;

    @BindView(R.id.current_order_list)
    RecyclerView currentOrderList;

    @BindView(R.id.food_list_placeholder)
    TextView foodListPlaceholder;

    FoodInOrderAdapter adapter;

    @Inject
    Context applicationContext;

    private AppDatabase db;

    public FoodListFragment(){
        adapter = new FoodInOrderAdapter();
    }

    @Override
    public void onAttach(Context context) {
        TakeMyOrderApplication.appComponent().inject(this);
        db = AppDatabase.getInstance(context);

        super.onAttach(context);
    }

    public void setOrder(Order order){
        this.order = order;
        //textView.setText(order.getUserId());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        ButterKnife.bind(this, rootView);

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(new ApplicationModule(applicationContext))
                .userInterfaceModule(
                        new UserInterfaceModule(LinearLayoutManager.VERTICAL, null))
                .build();

        currentOrderList.setAdapter(adapter);
        currentOrderList.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Meal> foods = adapter.getCurrentOrderEntryList();
                        db.currentOrderDao().deleteFood(foods.get(position));
                    }
                });
            }
        }).attachToRecyclerView(currentOrderList);


        return rootView;
    }

    public void setupViewModel(){
        CustomerMainViewModel customerMainViewModel = ViewModelProviders.of(this).get(CustomerMainViewModel.class);
        customerMainViewModel.getCurrentOrderList().observe(getActivity(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> currentOrderEntries) {
                adapter.swapCurrentOrderEntryList(currentOrderEntries);
                if (currentOrderEntries.size() != 0)
                    foodListPlaceholder.setVisibility(View.GONE);
                else
                    foodListPlaceholder.setVisibility(View.VISIBLE);
            }
        });
    }
}
