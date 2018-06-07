package com.udacity.thefedex87.takemyorder.mock;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.thefedex87.takemyorder.model.Restaurant;
import com.udacity.thefedex87.takemyorder.model.Waiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by federico.creti on 07/06/2018.
 */

public final class PostMockData {
    public static void postMockData(){
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference rootRef = db.getReference("restaurants");

        final ArrayList<Waiter> waiters = getWaitersMockedList();

        rootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DatabaseReference waiterRef = db.getReference("waiters");
                Random rnd = new Random();
                for(int i = 0; i < 3; i++){
                    Waiter waiter = waiters.get(rnd.nextInt(waiters.size()));
                    waiter.setRestaurantId(dataSnapshot.getKey());
                    waiterRef.push().setValue(waiter);
                    waiters.remove(waiter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for(Restaurant restaurant : getRestaurantsMockedList()){
            rootRef.push().setValue(restaurant);
        }

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




    }

    private static List<Restaurant> getRestaurantsMockedList(){
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        Restaurant r = new Restaurant();
        r.setName("Enjoy Food");
        r.setLat(43.340191);
        r.setLng(12.294784);
        restaurants.add(r);

        r = new Restaurant();
        r.setName("MWP");
        r.setLat(43.350438);
        r.setLng(12.287428);
        restaurants.add(r);

        r = new Restaurant();
        r.setName("Eat Eat Eat");
        r.setLat(43.303262);
        r.setLng(12.335086);
        restaurants.add(r);

        r = new Restaurant();
        r.setName("Happy meal");
        r.setLat(43.108486);
        r.setLng(12.387825);
        restaurants.add(r);

        r = new Restaurant();
        r.setName("IWTWIWT");
        r.setLat(43.101909);
        r.setLng(12.310238);
        restaurants.add(r);

        return restaurants;
    }

    private static ArrayList<Waiter> getWaitersMockedList(){
        ArrayList<Waiter> waiters = new ArrayList<>();

        Waiter waiter = new Waiter();
        waiter.setFirstName("Marco");
        waiter.setLastName("Rossi");
        waiter.setUserName("m.rossi22");
        waiter.setPassword("mrossi22pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Luigi");
        waiter.setLastName("Pardi");
        waiter.setUserName("l.pardi45");
        waiter.setPassword("lpardi45pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Bernard");
        waiter.setLastName("Burdi");
        waiter.setUserName("b.burdi56");
        waiter.setPassword("bburdi12pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Federico");
        waiter.setLastName("Taddei");
        waiter.setUserName("f.taddei36");
        waiter.setPassword("ftaddei36pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("John");
        waiter.setLastName("Richard");
        waiter.setUserName("j.richard81");
        waiter.setPassword("jrichard81pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Alex");
        waiter.setLastName("Sarti");
        waiter.setUserName("a.sarti55");
        waiter.setPassword("asarti55pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Ace");
        waiter.setLastName("Ventura");
        waiter.setUserName("a.ventura69");
        waiter.setPassword("aventura69pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Chiara");
        waiter.setLastName("Misciotti");
        waiter.setUserName("c.misciotti70");
        waiter.setPassword("cmisciotti70pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Martina");
        waiter.setLastName("Vanti");
        waiter.setUserName("m.vanti99");
        waiter.setPassword("mvanti99pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Vale");
        waiter.setLastName("Rossi");
        waiter.setUserName("v.rossi46");
        waiter.setPassword("vrossi46pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Jack");
        waiter.setLastName("Sparrow");
        waiter.setUserName("j.sparrow14");
        waiter.setPassword("jsparrow14pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Franco");
        waiter.setLastName("Franchi");
        waiter.setUserName("f.franchi59");
        waiter.setPassword("ffranchi59pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Megan");
        waiter.setLastName("Miner");
        waiter.setUserName("m.miner23");
        waiter.setPassword("mminer23pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Aldo");
        waiter.setLastName("Zanchi");
        waiter.setUserName("a.zanchi88");
        waiter.setPassword("azanchi88pwd");
        waiters.add(waiter);

        waiter = new Waiter();
        waiter.setFirstName("Bran");
        waiter.setLastName("Stark");
        waiter.setUserName("b.stark60");
        waiter.setPassword("bstark60pwd");
        waiters.add(waiter);

        return waiters;
    }
}
