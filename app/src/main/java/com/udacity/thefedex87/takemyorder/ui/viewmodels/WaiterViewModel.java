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
import com.udacity.thefedex87.takemyorder.models.Restaurant;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by federico.creti on 12/07/2018.
 */

public class WaiterViewModel extends ViewModel {
    private final String CALLS_ROOT = "waiters_calls";
    private MutableLiveData<List<WaiterCall>> calls;

    public WaiterViewModel(String restaurantId){
        calls = new MutableLiveData<>();
        retrieveRestaurant(restaurantId);
    }

    private void retrieveRestaurant(String restaurantId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference waiterCallReference = db.getReference(CALLS_ROOT + "/" +restaurantId);
        waiterCallReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO: manage if restaurant does not exists
                if(dataSnapshot != null){
                    List<WaiterCall> waiterCalls = new ArrayList<>();
                    for(DataSnapshot waiterCallSnapshot : dataSnapshot.getChildren()) {
                        waiterCalls.add(waiterCallSnapshot.getValue(WaiterCall.class));
                    }
                    calls.setValue(waiterCalls);
                    //calls.postValue(waiterCalls);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<WaiterCall>> getRestaurant() { return calls; }
}
