package com.udacity.thefedex87.takemyorder.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.Restaurant;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.WaiterViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.WaiterViewModelFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class WaiterMainActivity extends AppCompatActivity {

    private String restaurantId;
    private WaiterViewModelFactory waiterViewModelFactory;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.waiter_tabs)
    TabLayout waiterTabs;

    @Nullable
    @BindView(R.id.waiter_pager)
    ViewPager waiterPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_main);

        Timber.i("Waiter logged");

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(LoginMapsActivity.WAITER_RESTAURANT_KEY)){
            restaurantId = intent.getStringExtra(LoginMapsActivity.WAITER_RESTAURANT_KEY);

            setupUi();
            setupViewModel();
        }
    }

    private void setupViewModel() {
        waiterViewModelFactory = new WaiterViewModelFactory(restaurantId);
        WaiterViewModel waiterViewModel = ViewModelProviders.of(this, waiterViewModelFactory).get(WaiterViewModel.class);
        waiterViewModel.getRestaurant().observe(this, new Observer<List<WaiterCall>>() {
            @Override
            public void onChanged(@Nullable List<WaiterCall> waiterCalls) {
                int f = 4;
            }
        });
    }

    private void setupUi() {
        if (waiterTabs != null){
            waiterTabs.addTab(waiterTabs.newTab().setText(getString(R.string.waiter_ready_orders)));
            waiterTabs.addTab(waiterTabs.newTab().setText(getString(R.string.waiter_calls)));

            waiterTabs.setTabGravity(TabLayout.GRAVITY_FILL);
            waiterTabs.setTabMode(TabLayout.MODE_FIXED);
            waiterPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(waiterTabs));
            waiterTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    waiterPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }
}
