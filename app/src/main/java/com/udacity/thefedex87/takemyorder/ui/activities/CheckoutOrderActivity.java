package com.udacity.thefedex87.takemyorder.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderGrouped;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.ui.adapters.CheckoutOrderAdapter;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CheckoutOrderViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckoutOrderActivity extends AppCompatActivity {
    private ApplicationModule applicationModule;

    @BindView(R.id.starters_summary_container)
    LinearLayout startersContainer;

    @BindView(R.id.order_summary_starter)
    RecyclerView orderSummaryStarters;

    @BindView(R.id.main_dishes_summary_container)
    LinearLayout mainDishesContainer;

    @BindView(R.id.order_summary_main_dishes)
    RecyclerView orderSummaryMainDishes;

    @BindView(R.id.side_dishes_summary_container)
    LinearLayout sideDishesContainer;

    @BindView(R.id.order_summary_side_dishes)
    RecyclerView orderSummarySideDishes;

    @BindView(R.id.desserts_summary_container)
    LinearLayout dessertsContainer;

    @BindView(R.id.order_summary_desserts)
    RecyclerView orderSummaryDesserts;

    @BindView(R.id.drinks_summary_container)
    LinearLayout drinkContainer;

    @BindView(R.id.order_summary_drinks)
    RecyclerView orderSummaryDrinks;

    @Inject
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);

        TakeMyOrderApplication.appComponent().inject(this);

        applicationModule = new ApplicationModule(context);

        setupUi();

        setupViewModel();
    }

    private void setupViewModel() {
        //CheckoutOrderViewModelFactory checkoutOrderViewModelFactory = new CheckoutOrderViewModelFactory(AppDatabase.getInstance(context), FoodTypes.STARTER);

        CheckoutOrderViewModel checkoutOrderStartersViewModel = ViewModelProviders.of(this).get(CheckoutOrderViewModel.class);
        checkoutOrderStartersViewModel.setFoodType(AppDatabase.getInstance(context), FoodTypes.STARTER);
        checkoutOrderStartersViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    startersContainer.setVisibility(View.VISIBLE);
                    setData(orderSummaryStarters, currentOrdersGrouped);
                }
            }
        });

        checkoutOrderStartersViewModel.setFoodType(AppDatabase.getInstance(context), FoodTypes.MAINDISH);
        checkoutOrderStartersViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    mainDishesContainer.setVisibility(View.VISIBLE);
                    setData(orderSummaryMainDishes, currentOrdersGrouped);
                }
            }
        });

        checkoutOrderStartersViewModel.setFoodType(AppDatabase.getInstance(context), FoodTypes.SIDEDISH);
        checkoutOrderStartersViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    sideDishesContainer.setVisibility(View.VISIBLE);
                    setData(orderSummarySideDishes, currentOrdersGrouped);
                }
            }
        });

        checkoutOrderStartersViewModel.setFoodType(AppDatabase.getInstance(context), FoodTypes.DESSERT);
        checkoutOrderStartersViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    dessertsContainer.setVisibility(View.VISIBLE);
                    setData(orderSummaryDesserts, currentOrdersGrouped);
                }
            }
        });

        checkoutOrderStartersViewModel.setFoodType(AppDatabase.getInstance(context), FoodTypes.DRINK);
        checkoutOrderStartersViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    drinkContainer.setVisibility(View.VISIBLE);
                    setData(orderSummaryDrinks, currentOrdersGrouped);
                }
            }
        });
    }

    private void setupUi() {
        ButterKnife.bind(this);


    }

    private void setData(RecyclerView recyclerView, List<CurrentOrderGrouped> meals){
        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent
                .builder()
                .applicationModule(applicationModule)
                .userInterfaceModule(new UserInterfaceModule(meals, LinearLayoutManager.VERTICAL))
                .build();

        CheckoutOrderAdapter orderSummaryAdapter = userInterfaceComponent.getCheckoutOrderAdapter();
        LinearLayoutManager linearLayoutManager = userInterfaceComponent.getLinearLayoutManager();

        recyclerView.setAdapter(orderSummaryAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
