package com.udacity.thefedex87.takemyorder.ui.fragments;

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
import com.udacity.thefedex87.takemyorder.ui.adapters.WaiterCallsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by feder on 12/07/2018.
 */

//Fragment used to load and show the waiter calls
public class WaiterCallsFragment extends Fragment implements WaiterCallsAdapter.WaiterCallsAdapterClick {

    private static int NOTIFICATION_ID = 1;

    private List<String> currentCallsId;
    private String restaurantId;

    private boolean viewAttached = false;
    private boolean dataSet = false;

    private Context context;

    private WaiterCallsAdapter waiterCallsAdapter;

    private boolean firstDownload = true;

    @BindView(R.id.waiter_calls_list)
    RecyclerView waiterCalls;

    @BindView(R.id.no_calls_text)
    TextView noCallsText;

    public WaiterCallsFragment(){
        currentCallsId = new ArrayList<>();
    }

    public void setRestaurantId(String restaurantId){
        this.restaurantId = restaurantId;
        dataSet = true;
    }

    public void setCalls(List<WaiterCall> calls){
        waiterCallsAdapter.swapCalls(calls);

        if (waiterCalls != null && calls.size() > 0) {
            noCallsText.setVisibility(View.GONE);
        } else {
            noCallsText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Created WaiterCallsFragment");
        View rootView = inflater.inflate(R.layout.waiter_calls_fragment, container, false);


        ButterKnife.bind(this, rootView);

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .userInterfaceModule(new UserInterfaceModule(LinearLayoutManager.VERTICAL, this))
                .build();

        waiterCallsAdapter = userInterfaceComponent.getWaiterCallsAdapter();

        waiterCalls.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());
        waiterCalls.setAdapter(waiterCallsAdapter);

        viewAttached = true;

        return rootView;
    }

    @Override
    public void takeCallClick(WaiterCall call) {
        //When the waiter click on "Take request" button, I delete from firebase the waiter call
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference callsReference = db.getReference("waiters_calls/" + restaurantId + "/" + call.getId());
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
