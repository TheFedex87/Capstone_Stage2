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
import com.udacity.thefedex87.takemyorder.dagger.DaggerViewModelComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.dagger.ViewModelModule;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.models.Order;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.UserRoomContainer;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInOrderAdapter;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModelFactory;

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

    @BindView(R.id.total_price)
    TextView totalOrderPriceTV;

    @BindView(R.id.table_number)
    TextView tableNumber;

    @Inject
    Context applicationContext;

    private AppDatabase db;

    private FoodInOrderAdapter adapter;

    private double totalOrderPrice;

    private String restaurantId;

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
    }

    public void setTableNumber(String tableNumber){
        this.tableNumber.setText(getString(R.string.table_number, tableNumber));
    }

    public void setRestaurantId(String restaurantId){
        this.restaurantId = restaurantId;
    }

    public void userLoaded(){
        setupViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        ButterKnife.bind(this, rootView);

        totalOrderPriceTV.setText(getString(R.string.total_price, 0.00f));

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(new ApplicationModule(applicationContext))
                .userInterfaceModule(
                        new UserInterfaceModule(LinearLayoutManager.VERTICAL, null, null))
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
        //Setup the CustomerMainViewModel in order to observe the current order
        //CustomerMainViewModelFactory customerMainViewModelFactory = new CustomerMainViewModelFactory(AppDatabase.getInstance(getContext()), null, null, ((UserRoomContainer)getActivity()).getUserRoomId());
        CustomerMainViewModelFactory customerMainViewModelFactory = DaggerViewModelComponent
                .builder()
                .applicationModule(new ApplicationModule(applicationContext))
                .viewModelModule(new ViewModelModule(restaurantId, null, ((UserRoomContainer)getActivity()).getUserRoomId()))
                .build()
                .getCustomerMainViewModelFactory();

        CustomerMainViewModel customerMainViewModel = ViewModelProviders.of(this, customerMainViewModelFactory).get(CustomerMainViewModel.class);
        customerMainViewModel.getCurrentOrderList().observe(getActivity(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> currentOrderEntries) {
                adapter.swapCurrentOrderEntryList(currentOrderEntries);
                if (currentOrderEntries.size() != 0)
                    foodListPlaceholder.setVisibility(View.GONE);
                else
                    foodListPlaceholder.setVisibility(View.VISIBLE);

                totalOrderPrice = 0;

                //Calculate total price of order
                for(Meal meal : currentOrderEntries){
                    totalOrderPrice += meal.getPrice();
                }
                totalOrderPriceTV.setText(getString(R.string.total_price, totalOrderPrice));
            }
        });
    }

    public double getTotalOrderList(){ return totalOrderPrice; }
}
