package com.udacity.thefedex87.takemyorder.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.model.Order;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderEntry;
import com.udacity.thefedex87.takemyorder.ui.fragments.FoodListFragment;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CustomerMainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @BindView(R.id.add_to_order_fab)
    FloatingActionButton addToOrderFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        Timber.d("Opened CustomerMainActivity");
        ButterKnife.bind(this);

        FoodListFragment foodListFragment = (FoodListFragment) getSupportFragmentManager().findFragmentById(R.id.current_order);
        Order order = new Order();
        order.setUserId("USERD ID TEST");
        foodListFragment.setOrder(order);

        addToOrderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setupViewModel();
    }

    public void setupViewModel(){
        CustomerMainViewModel customerMainViewModel = ViewModelProviders.of(this).get(CustomerMainViewModel.class);
        customerMainViewModel.getCurrentOrderList().observe(this, new Observer<List<CurrentOrderEntry>>() {
            @Override
            public void onChanged(@Nullable List<CurrentOrderEntry> currentOrderEntries) {

            }
        });
    }

//    public void signout(View view){
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.signOut();
//        Timber.d("User signed out");
//        finish();
//    }
}
