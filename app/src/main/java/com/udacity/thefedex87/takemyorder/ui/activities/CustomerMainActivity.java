package com.udacity.thefedex87.takemyorder.ui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Customer;
import com.udacity.thefedex87.takemyorder.models.Order;
import com.udacity.thefedex87.takemyorder.models.Restaurant;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.User;
import com.udacity.thefedex87.takemyorder.ui.fragments.FoodListFragment;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModelFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CustomerMainActivity extends AppCompatActivity {
    public static final String FOOD_DESCRIPTION_KEY = "FOOD_DESCRIPTION_KEY";
    public static final String RESTAURANT_ID_KEY = "RESTAURANT_ID_KEY";
    public static final String USER_ID_KEY = "USER_ID_KEY";
    public static final String ORDER_TOTAL_PRICE_KEY = "ORDER_TOTAL_PRICE_KEY";
    public static final String TABLE_NUMBER_KEY = "TABLE_NUMBER_KEY";

    private FirebaseAuth firebaseAuth;
    //private Customer customer;
    private Restaurant restaurant;
    private String restaurantId;
    private String table;

    private long userRoomId;

    private FoodListFragment foodListFragment;

    @BindView(R.id.add_to_order_fab)
    FloatingActionButton addToOrderFab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Inject
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        TakeMyOrderApplication.appComponent().inject(this);

        Timber.d("Opened CustomerMainActivity");

        if (getIntent() != null &&
                getIntent().hasExtra(LoginMapsActivity.USER_INFO_KEY) &&
                getIntent().hasExtra(LoginMapsActivity.USER_RESTAURANT_KEY) &&
                getIntent().hasExtra(LoginMapsActivity.USER_RESTAURANT_TABLE_KEY)) {

            ButterKnife.bind(this);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //customer = getIntent().getParcelableExtra(LoginMapsActivity.USER_INFO_KEY);
            restaurantId = getIntent().getStringExtra(LoginMapsActivity.USER_RESTAURANT_KEY);
            table = getIntent().getStringExtra(LoginMapsActivity.USER_RESTAURANT_TABLE_KEY);


            //Setup view model (used in this activity to retrieve the restaurant from Firebase)
            setupViewModel();

            //Retrieve the food list fragment
            foodListFragment = (FoodListFragment) getSupportFragmentManager().findFragmentById(R.id.current_order);
//            Order order = new Order();
//            order.setUserId("USERD ID TEST");
//            foodListFragment.setOrder(order);

            //Assign the table to the fragment
            foodListFragment.setTableNumber(table);


            addToOrderFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Launch the Restaurant menu activity passing the restaurantId
                    Intent intent = new Intent(CustomerMainActivity.this, RestaurantMenuActivity.class);
                    intent.putExtra(LoginMapsActivity.USER_RESTAURANT_KEY, restaurantId);
                    intent.putExtra(USER_ID_KEY, userRoomId);
                    startActivity(intent);
                }
            });
        } else{
            Timber.d("Some important key not passed to the activity");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_customer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favourites:
                Intent intent = new Intent(this, FavouritesFoodsActivity.class);
                intent.putExtra(RESTAURANT_ID_KEY, restaurantId);
                intent.putExtra(USER_ID_KEY, userRoomId);
                startActivity(intent);
                return true;
            case R.id.delete_current_order:
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle(getString(R.string.confirm_delete_title))
                        .setMessage(getString(R.string.confirm_delete_text))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDatabase.getInstance(CustomerMainActivity.this).currentOrderDao().deleteAllFoods();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            case R.id.checkout_order:
                Intent intentCheckoutOrder = new Intent(this, CheckoutOrderActivity.class);
                intentCheckoutOrder.putExtra(ORDER_TOTAL_PRICE_KEY, foodListFragment.getTotalOrderList());
                intentCheckoutOrder.putExtra(TABLE_NUMBER_KEY, table);
                intentCheckoutOrder.putExtra(RESTAURANT_ID_KEY, restaurantId);
                startActivity(intentCheckoutOrder);
                return true;
            case R.id.call_waiter:
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference waiters = db.getReference("waitersCalls").child(restaurantId);

                return true;
            case R.id.log_out:
                signout();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewModel(){
        //Retrieve the restaurant using VIewModel
        CustomerMainViewModelFactory customerMainViewModelFactory = new CustomerMainViewModelFactory(AppDatabase.getInstance(this), restaurantId, FirebaseAuth.getInstance().getUid());
        CustomerMainViewModel customerMainViewModel = ViewModelProviders.of(this, customerMainViewModelFactory).get(CustomerMainViewModel.class);
        customerMainViewModel.getRestaurant().observe(this, new Observer<Restaurant>() {
            @Override
            public void onChanged(@Nullable Restaurant restaurant) {
                if (restaurant == null){
                    Timber.e("Invalid ID restaurant");
                    Toast.makeText(CustomerMainActivity.this, getString(R.string.error_invalid_id_restaurant), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    collapsingToolbarLayout.setTitleEnabled(false);
                    toolbar.setTitle(restaurant.getName());
                }
            }
        });

        //Check if logged user already exists into DB, if not I insert he
        final LiveData<User> userLiveData = customerMainViewModel.getUserByUserFirebaseId();
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                userLiveData.removeObserver(this);
                if (user == null){
                    final User newUser = new User();
                    newUser.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    newUser.setUserFirebaseId(FirebaseAuth.getInstance().getUid());

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            userRoomId = AppDatabase.getInstance(context).userDao().insertUser(newUser);
                        }
                    });
                } else {
                    userRoomId = user.getId();
                }
            }
        });
    }

    private void signout(){
        //Signout from Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Timber.d("User signed out");
        finish();
    }

    public long getUserRoomId(){
        return userRoomId;
    }
}
