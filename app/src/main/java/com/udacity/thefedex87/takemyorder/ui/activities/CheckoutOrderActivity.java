package com.udacity.thefedex87.takemyorder.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.DaggerViewModelComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.dagger.ViewModelModule;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Order;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderGrouped;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.adapters.CheckoutOrderAdapter;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CheckoutOrderViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CheckoutOrderViewModelFactory;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckoutOrderActivity extends AppCompatActivity {
    private ApplicationModule applicationModule;
    private double totPrice;
    private String table;
    private String restaurantId;
    private long userRoomId;
    private List<Meal> currentOrder;

    private FirebaseDatabase firebaseDatabase;

    @BindView(R.id.processing_order)
    RelativeLayout processingOrder;

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

    @BindView(R.id.total_price)
    TextView totalPrice;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);

        TakeMyOrderApplication.appComponent().inject(this);

        applicationModule = new ApplicationModule(context);

        Intent intent = getIntent();
        totPrice = intent.getDoubleExtra(CustomerMainActivity.ORDER_TOTAL_PRICE_KEY, 0);
        table = intent.getStringExtra(CustomerMainActivity.TABLE_NUMBER_KEY);
        restaurantId = intent.getStringExtra(CustomerMainActivity.RESTAURANT_ID_KEY);
        userRoomId = intent.getLongExtra(CustomerMainActivity.USER_ID_KEY, 0);

        setupUi();

        setupViewModel();
    }

    private void setupViewModel() {
        //CheckoutOrderViewModelFactory checkoutOrderViewModelFactory = new CheckoutOrderViewModelFactory(AppDatabase.getInstance(context), userRoomId);
        CheckoutOrderViewModelFactory checkoutOrderViewModelFactory = DaggerViewModelComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .viewModelModule(new ViewModelModule(userRoomId))
                .build()
                .getCheckoutOrderViewModelFactory();

        CheckoutOrderViewModel checkoutOrderViewModel = ViewModelProviders.of(this, checkoutOrderViewModelFactory).get(CheckoutOrderViewModel.class);
        checkoutOrderViewModel.setFoodType(FoodTypes.STARTER);
        checkoutOrderViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    startersContainer.setVisibility(View.VISIBLE);
                    setData(orderSummaryStarters, currentOrdersGrouped);
                }
            }
        });

        checkoutOrderViewModel.setFoodType(FoodTypes.MAINDISH);
        checkoutOrderViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    mainDishesContainer.setVisibility(View.VISIBLE);
                    setData(orderSummaryMainDishes, currentOrdersGrouped);
                }
            }
        });

        checkoutOrderViewModel.setFoodType(FoodTypes.SIDEDISH);
        checkoutOrderViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    sideDishesContainer.setVisibility(View.VISIBLE);
                    setData(orderSummarySideDishes, currentOrdersGrouped);
                }
            }
        });

        checkoutOrderViewModel.setFoodType(FoodTypes.DESSERT);
        checkoutOrderViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    dessertsContainer.setVisibility(View.VISIBLE);
                    setData(orderSummaryDesserts, currentOrdersGrouped);
                }
            }
        });

        checkoutOrderViewModel.setFoodType(FoodTypes.DRINK);
        checkoutOrderViewModel.getCurrentOrderByFoodType().observe(this, new Observer<List<CurrentOrderGrouped>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderGrouped> currentOrdersGrouped) {
                if (currentOrdersGrouped != null && currentOrdersGrouped.size() > 0) {
                    drinkContainer.setVisibility(View.VISIBLE);
                    setData(orderSummaryDrinks, currentOrdersGrouped);
                }
            }
        });

        checkoutOrderViewModel.getCurrentOrder().observe(this, new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> meals) {
                currentOrder = meals;
            }
        });
    }

    private void setupUi() {
        ButterKnife.bind(this);


        totalPrice.setText(getString(R.string.total_price, totPrice));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setData(RecyclerView recyclerView, List<CurrentOrderGrouped> meals){
        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent
                .builder()
                .applicationModule(applicationModule)
                .userInterfaceModule(new UserInterfaceModule(meals, LinearLayoutManager.VERTICAL))
                .build();

        CheckoutOrderAdapter orderSummaryAdapter = userInterfaceComponent.getCheckoutOrderAdapter();
        LinearLayoutManager linearLayoutManager = userInterfaceComponent.getLinearLayoutManager();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(orderSummaryAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_checkout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pay){
            if (currentOrder.size() > 0) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle(getString(R.string.confirm_payment_title))
                        .setMessage(getString(R.string.confirm_payment_text))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                processingOrder.setVisibility(View.VISIBLE);

                                //Here should be implementet PayPal payment system (ora any other payment system), for the purpose of the app, the payment method return true

                                if (getPaymentSuccedd()) {
                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference ordersRef = firebaseDatabase.getReference("orders").child(restaurantId);

                                    Order order = new Order(restaurantId, FirebaseAuth.getInstance().getUid(), table, currentOrder, new Date());
                                    ordersRef.push().setValue(order);

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AppDatabase.getInstance(CheckoutOrderActivity.this).currentOrderDao().deleteAllFoods(userRoomId);
                                                }
                                            });

                                            Toast.makeText(context, getString(R.string.order_submitted), Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }, 2000);


                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else{
                Toast.makeText(context, getString(R.string.nothing_on_current_order), Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean getPaymentSuccedd(){
        //Here should be done all the operation to redirect to the payment system
        return true;
    }
}
