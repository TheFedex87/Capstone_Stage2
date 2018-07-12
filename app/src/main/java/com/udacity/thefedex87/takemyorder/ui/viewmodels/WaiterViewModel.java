package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.models.WaiterReadyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by federico.creti on 12/07/2018.
 */

public class WaiterViewModel extends ViewModel {
    private final String CALLS_ROOT = "waiters_calls";
    private final String READY_ORDERS_ROOT = "orders";

    private MutableLiveData<List<WaiterCall>> calls;
    private MutableLiveData<List<WaiterReadyOrder>> readyOrders;

    public WaiterViewModel(String restaurantId){
        calls = new MutableLiveData<>();
        readyOrders = new MutableLiveData<>();
        retrieveWaiterCalls(restaurantId);
        retrieveWaiterReadyOrders(restaurantId);
    }

    private void retrieveWaiterReadyOrders(String restaurantId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference waiterCallReference = db.getReference(READY_ORDERS_ROOT + "/" +restaurantId);
        waiterCallReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO: manage if restaurant does not exists
                if(dataSnapshot != null){
                    List<WaiterReadyOrder> waiterReadyOrders = new ArrayList<>();
                    for(DataSnapshot waiterReadyOrderSnapshot : dataSnapshot.getChildren()) {
                        WaiterReadyOrder waiterReadyOrder = waiterReadyOrderSnapshot.getValue(WaiterReadyOrder.class);
                        waiterReadyOrder.setId(waiterReadyOrderSnapshot.getKey());
                        waiterReadyOrders.add(waiterReadyOrder);
                    }
                    readyOrders.setValue(waiterReadyOrders);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieveWaiterCalls(String restaurantId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference waiterCallReference = db.getReference(CALLS_ROOT + "/" +restaurantId);
        waiterCallReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO: manage if restaurant does not exists
                if(dataSnapshot != null){
                    List<WaiterCall> waiterCalls = new ArrayList<>();
                    for(DataSnapshot waiterCallSnapshot : dataSnapshot.getChildren()) {
                        WaiterCall waiterCall = waiterCallSnapshot.getValue(WaiterCall.class);
                        waiterCall.setId(waiterCallSnapshot.getKey());
                        waiterCalls.add(waiterCall);
                    }
                    calls.setValue(waiterCalls);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<WaiterCall>> getWaiterCalls() { return calls; }
    public LiveData<List<WaiterReadyOrder>> getWaiterReadyOrders() {return readyOrders;}
}
