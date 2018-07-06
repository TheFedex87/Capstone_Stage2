package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.Restaurant;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.User;
import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;

import java.util.List;

import timber.log.Timber;

/**
 * Created by federico.creti on 14/06/2018.
 */

public class CustomerMainViewModel extends ViewModel {
    private LiveData<List<Meal>> currentOrderList;
    private MutableLiveData<Restaurant> restaurantLivedata;
    private LiveData<User> userByUserFirebaseId;

    public CustomerMainViewModel(AppDatabase db, String restaurantId, String userName, long userRoomId) {
        currentOrderList = db.currentOrderDao().getCurrentOrderList(userRoomId);
        restaurantLivedata = new MutableLiveData<>();
        if (restaurantId != null && !restaurantId.isEmpty())
            retrieveRetaurant(restaurantId);

        if(userName != null && !userName.isEmpty()){
            userByUserFirebaseId = db.userDao().getUserByUserFirebaseId(userName);
        }
    }

    private void retrieveRetaurant(String restaurantId){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference restaurantReference = firebaseDatabase.getReference("restaurants");
        restaurantReference.orderByKey().equalTo(restaurantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurant restaurant = null;
                for(DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()) {
                    restaurant = restaurantSnapshot.getValue(Restaurant.class);
                }

                restaurantLivedata.setValue(restaurant);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Meal>> getCurrentOrderList() {
        return currentOrderList;
    }
    public LiveData<Restaurant> getRestaurant() { return restaurantLivedata; }
    public LiveData<User> getUserByUserFirebaseId() {
        return userByUserFirebaseId;
    }
}
