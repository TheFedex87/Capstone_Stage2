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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.models.WaiterReadyOrder;
import com.udacity.thefedex87.takemyorder.ui.adapters.WaiterReadyOrdersAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by feder on 12/07/2018.
 */

//Fragment used to load and show the list of ready orders
public class WaiterReadyOrderFragment extends Fragment implements WaiterReadyOrdersAdapter.WaiterReadyOrderAdapterClick {
    private List<WaiterReadyOrder> readyOrders;
    private String restaurantId;

    private Context context;

    private WaiterReadyOrdersAdapter waiterReadyOrdersAdapter;

    @BindView(R.id.waiter_ready_orders_list)
    RecyclerView waiterReadyOrders;

    @BindView(R.id.no_ready_orders_text)
    TextView noReadyOrdersText;

    public WaiterReadyOrderFragment(){

    }

    public void setReadyOrders(List<WaiterReadyOrder> readyOrders){
        waiterReadyOrdersAdapter.swapReadyOrders(readyOrders);

        if (readyOrders != null && readyOrders.size() > 0) {
            noReadyOrdersText.setVisibility(View.GONE);
        } else {
            noReadyOrdersText.setVisibility(View.VISIBLE);
        }
    }

    public void setRestaurantId(String restaurantId){
        this.restaurantId = restaurantId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Created WaiterReadyOrderFragment");
        View rootView = inflater.inflate(R.layout.waiter_ready_orders_fragment, container, false);


        ButterKnife.bind(this, rootView);

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .userInterfaceModule(new UserInterfaceModule(LinearLayoutManager.VERTICAL, this))
                .build();

        waiterReadyOrdersAdapter = userInterfaceComponent.getWaiterReadyOrdersAdapter();

        waiterReadyOrders.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
        waiterReadyOrders.setAdapter(waiterReadyOrdersAdapter);

        return rootView;
    }

    @Override
    public void takeReadyOrderClick(WaiterReadyOrder readyOrder) {
        //When the waiter click on "Take request" button, I delete from firebase the ready order request
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference callsReference = db.getReference("orders/" + restaurantId + "/" + readyOrder.getId());
        callsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot callSnapshot : dataSnapshot.getChildren()){
                    callSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
