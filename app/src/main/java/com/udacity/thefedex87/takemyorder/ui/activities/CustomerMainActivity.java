package com.udacity.thefedex87.takemyorder.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Customer;
import com.udacity.thefedex87.takemyorder.models.Order;
import com.udacity.thefedex87.takemyorder.models.Restaurant;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.ui.fragments.FoodListFragment;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CustomerMainActivity extends AppCompatActivity {
    public static final String FOOD_DESCRIPTION_KEY = "FOOD_DESCRIPTION_KEY";
    public static final String RESTAURANT_ID_KEY = "RESTAURANT_ID_KEY";

    private FirebaseAuth firebaseAuth;
    private Customer customer;
    private Restaurant restaurant;
    private String restaurantId;
    private String table;

    @BindView(R.id.add_to_order_fab)
    FloatingActionButton addToOrderFab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        Timber.d("Opened CustomerMainActivity");

        if (getIntent() != null &&
                getIntent().hasExtra(LoginMapsActivity.USER_INFO_KEY) &&
                getIntent().hasExtra(LoginMapsActivity.USER_RESTAURANT_KEY) &&
                getIntent().hasExtra(LoginMapsActivity.USER_RESTAURANT_TABLE_KEY)) {

            ButterKnife.bind(this);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            customer = getIntent().getParcelableExtra(LoginMapsActivity.USER_INFO_KEY);
            restaurantId = getIntent().getStringExtra(LoginMapsActivity.USER_RESTAURANT_KEY);
            table = getIntent().getStringExtra(LoginMapsActivity.USER_RESTAURANT_TABLE_KEY);


            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference restaurantReference = firebaseDatabase.getReference("restaurants");
            restaurantReference.orderByKey().equalTo(restaurantId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()) {
                        restaurant = restaurantSnapshot.getValue(Restaurant.class);
                    }

                    if (restaurant == null){
                        Timber.e("Invalid ID restaurant");
                        Toast.makeText(CustomerMainActivity.this, getString(R.string.error_invalid_id_restaurant), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        collapsingToolbarLayout.setTitleEnabled(false);
                        toolbar.setTitle(restaurant.getName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            FoodListFragment foodListFragment = (FoodListFragment) getSupportFragmentManager().findFragmentById(R.id.current_order);
            Order order = new Order();
            order.setUserId("USERD ID TEST");
            foodListFragment.setOrder(order);
            foodListFragment.setTableNumber(table);

            addToOrderFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    final AppDatabase db = AppDatabase.getInstance(CustomerMainActivity.this);
//                    final CurrentOrderEntry entry = new CurrentOrderEntry(0, "Pizza margherita", 3.4, CurrentOrderEntry.FoodTypes.MAINDISH, "asasasa");
//                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            db.currentOrderDao().insertFood(entry);
//                        }
//                    });
                    Intent intent = new Intent(CustomerMainActivity.this, RestaurantMenuActivity.class);
                    intent.putExtra(LoginMapsActivity.USER_RESTAURANT_KEY, restaurantId);
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
                startActivity(intent);
                return true;
            case R.id.checkout_order:
                return true;
            case R.id.call_waiter:
                return true;
            case R.id.log_out:
                signout();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signout(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Timber.d("User signed out");
        finish();
    }
}
