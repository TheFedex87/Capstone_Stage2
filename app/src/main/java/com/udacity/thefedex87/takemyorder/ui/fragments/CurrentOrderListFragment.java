package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.DaggerViewModelComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkModule;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.dagger.ViewModelModule;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.UserRoomContainer;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInOrderAdapter;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModelFactory;
import com.udacity.thefedex87.takemyorder.ui.widgets.UpdateWidgetService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by federico.creti on 14/06/2018.
 */

public class CurrentOrderListFragment extends Fragment {
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

    private NetworkComponent networkComponent;

    public CurrentOrderListFragment(){
        adapter = new FoodInOrderAdapter();
    }

    @Override
    public void onAttach(Context context) {
        TakeMyOrderApplication.appComponent().inject(this);
        db = AppDatabase.getInstance(context);

        super.onAttach(context);
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

        ApplicationModule applicationModule = new ApplicationModule(applicationContext);

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(applicationModule)
                .userInterfaceModule(
                        new UserInterfaceModule(LinearLayoutManager.VERTICAL))
                .build();


        networkComponent = DaggerNetworkComponent.builder().applicationModule(applicationModule).networkModule(new NetworkModule()).build();

        currentOrderList.setAdapter(adapter);
        currentOrderList.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder to remove a meal from current order
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Timber.d("Request remove of a meal from current order");
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setTitle(getString(R.string.confirm_delete_title))
                        .setMessage(getString(R.string.confirm_delete_meal_text))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                int position = viewHolder.getAdapterPosition();
                                                List<Meal> foods = adapter.getCurrentOrderEntryList();
                                                db.currentOrderDao().deleteFood(foods.get(position));
                                                Timber.d("Meal removed from current order");
                                            }
                                        });
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                Timber.d("Meal not removed from current order");
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        }).attachToRecyclerView(currentOrderList);


        return rootView;
    }

    public void setupViewModel(){
        //Setup the CustomerMainViewModel
        CustomerMainViewModelFactory customerMainViewModelFactory = DaggerViewModelComponent
                .builder()
                .applicationModule(new ApplicationModule(applicationContext))
                .viewModelModule(new ViewModelModule(restaurantId, null, ((UserRoomContainer)getActivity()).getUserRoomId()))
                .build()
                .getCustomerMainViewModelFactory();

        CustomerMainViewModel customerMainViewModel = ViewModelProviders.of(this, customerMainViewModelFactory).get(CustomerMainViewModel.class);

        //Observe for current order
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

        //Observe for favourite meal of user in order to store the favourite into SharedPreference to be loaded into the widget
        customerMainViewModel.getAllFavouriteMealOfUser().observe(this, new Observer<List<FavouriteMeal>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteMeal> favouriteMeals) {
                final SharedPreferences.Editor editor = getActivity().getSharedPreferences(CustomerMainActivity.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();

                //Retrieve the whole list of favourites to store them into SHaredPreferences

                List<String> serializedFavouriteMeals = new ArrayList<>();
                Gson gson = networkComponent.getGson();

                for(FavouriteMeal favouriteMeal : favouriteMeals){
                    String serializedFavourite = gson.toJson(favouriteMeal);
                    serializedFavouriteMeals.add(serializedFavourite);
                }

                Set<String> stringSet = new HashSet<>();
                stringSet.addAll(serializedFavouriteMeals);
                editor.putStringSet(CustomerMainActivity.SHARED_PREFERENCES_FAVOURITES_LIST, stringSet);
                editor.putLong(CustomerMainActivity.SHARED_PREFERENCES_USER_ID, ((UserRoomContainer)getActivity()).getUserRoomId());
                editor.apply();

                //Call the service to update the widget of favourites
                Intent updateWidgetIntent = new Intent(getActivity(), UpdateWidgetService.class);
                updateWidgetIntent.setAction(UpdateWidgetService.UPDATE_WIDGET_ACTION);
                getActivity().startService(updateWidgetIntent);
            }
        });
    }

    public double getTotalOrderList(){ return totalOrderPrice; }
}
