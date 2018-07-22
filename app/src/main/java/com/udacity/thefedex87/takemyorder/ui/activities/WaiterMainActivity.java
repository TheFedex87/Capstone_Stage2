package com.udacity.thefedex87.takemyorder.ui.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.models.WaiterReadyOrder;
import com.udacity.thefedex87.takemyorder.services.WaiterAttentionRequestService;
import com.udacity.thefedex87.takemyorder.ui.adapters.WaiterPagerAdapter;
import com.udacity.thefedex87.takemyorder.ui.fragments.WaiterCallsFragment;
import com.udacity.thefedex87.takemyorder.ui.fragments.WaiterReadyOrderFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class WaiterMainActivity extends AppCompatActivity {
    private final String RESTAURANT_KEY = "RESTAURANT_KEY";
    public static String NOTIFICATION_CHANNEL_ID = "WAITER_ATTENTION_REQUEST";

    public static String ACTION_WAITER_CALLS = "com.udacity.thefedex87.takemyorder.ui.activities.WAITERS_CALLS_RECEIVED_ACTION";
    public static String ACTION_WAITER_READY_ORDERS = "com.udacity.thefedex87.takemyorder.ui.activities.WAITERS_READY_ORDERS_RECEIVED_ACTION";

    public static final String WAITER_CALLS_LIST = "WAITER_CALLS_LIST";
    public static final String WAITER_READY_ORDERS_LIST = "WAITER_READY_ORDERS_LIST";

    private String restaurantId;
    private FirebaseAuth firebaseAuth;

    private WaiterPagerAdapter waiterPagerAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.waiter_tabs)
    TabLayout waiterTabs;

    @Nullable
    @BindView(R.id.waiter_pager)
    ViewPager waiterPager;

    @Inject
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_main);


        Timber.i("Waiter logged");
        firebaseAuth = FirebaseAuth.getInstance();

        ButterKnife.bind(this);

        TakeMyOrderApplication.appComponent().inject(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(LoginMapsActivity.WAITER_RESTAURANT_KEY)) {
            restaurantId = intent.getStringExtra(LoginMapsActivity.WAITER_RESTAURANT_KEY);

            setupUi();

            registerReceiver();

            startWaiterService();
        }
        else{
            if (intent == null)
                Timber.e("Intent not provided");
            else if(!intent.hasExtra(LoginMapsActivity.WAITER_RESTAURANT_KEY))
                Timber.e("WAITER_RESTAURANT_KEY not provided");
            else
                Timber.e("WAITER_RESTAURANT_KEY not provided");

            finish();
        }
    }

    private void startWaiterService(){
        //Enable the service which listen for new Waiter's request (customer call or ready order from kitchen)
        Intent intentService = new Intent(context, WaiterAttentionRequestService.class);
        intentService.putExtra(LoginMapsActivity.RESTAURANTS_INFO_KEY, restaurantId);
        context.startService(intentService);
    }

    private void registerReceiver(){
        //Register the broadcast receiver which came from WaiterAttentionRequestService
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_WAITER_CALLS);
        filter.addAction(ACTION_WAITER_READY_ORDERS);
        AttentionRequestReceiver attentionRequestReceiver = new AttentionRequestReceiver();

        registerReceiver(attentionRequestReceiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_waiter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.log_out:
                signout();
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(RESTAURANT_KEY, restaurantId);
        super.onSaveInstanceState(outState);
    }

    private void setupUi() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.waiter_main_title, firebaseAuth.getCurrentUser().getDisplayName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (waiterTabs != null){
            //If this is a single panel layout (portrait)
            waiterTabs.addTab(waiterTabs.newTab().setText(getString(R.string.waiter_calls)));
            waiterTabs.addTab(waiterTabs.newTab().setText(getString(R.string.waiter_ready_orders)));

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

            waiterPagerAdapter = DaggerUserInterfaceComponent
                    .builder()
                    .applicationModule(new ApplicationModule(context))
                    .userInterfaceModule(new UserInterfaceModule(getSupportFragmentManager(), restaurantId))
                    .build()
                    .getWaiterPagerAdapter();

            waiterPager.setAdapter(waiterPagerAdapter);


        } else {
            //If we are in a master-details layout (landscape)
            ((WaiterCallsFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_calls)).setRestaurantId(restaurantId);
            ((WaiterReadyOrderFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_ready_orders)).setRestaurantId(restaurantId);
        }

        registerNotificationChannels();
    }

    private void registerNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = getString(R.string.waiter_notification_channel_name);
            String channelDescription = getString(R.string.waiter_notification_channel_description);
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channelDescription);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] { 1000 });

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void signout(){
        //Signout from Firebase
        firebaseAuth.signOut();
        Timber.d("UserBase signed out");

        WaiterAttentionRequestService.setIsExecutingTask(false);
        finish();
    }

    //Receiver which listens for waiters call and ready orders and fill the UI
    public class AttentionRequestReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_WAITER_CALLS)){
                List<WaiterCall> calls = intent.getParcelableArrayListExtra(WAITER_CALLS_LIST);
                if (waiterTabs != null) {
                    waiterPagerAdapter.setCalls(calls);
                } else {
                    ((WaiterCallsFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_calls)).setCalls(calls);
                }
            } else if(intent.getAction().equals(ACTION_WAITER_READY_ORDERS)){
                List<WaiterReadyOrder> readyOrders = intent.getParcelableArrayListExtra(WAITER_READY_ORDERS_LIST);
                if (waiterTabs != null) {
                    waiterPagerAdapter.setReadyOrders(readyOrders);
                } else {
                    ((WaiterReadyOrderFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_ready_orders)).setReadyOrders(readyOrders);
                }
            }
        }
    }
}
