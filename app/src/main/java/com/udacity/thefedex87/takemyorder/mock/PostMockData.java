package com.udacity.thefedex87.takemyorder.mock;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.thefedex87.takemyorder.model.Restaurant;

/**
 * Created by federico.creti on 07/06/2018.
 */

public final class PostMockData {
    public static void postMockData(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = db.getReference("restaurants");

//        rootRef.orderByChild("name").equalTo("Restaurant 1").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        Restaurant r = new Restaurant();
        r.setName("Restaurant 1");
        rootRef.push().setValue(r);

    }
}
