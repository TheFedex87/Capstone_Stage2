package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.model.Customer;
import com.udacity.thefedex87.takemyorder.model.Restaurant;
import com.udacity.thefedex87.takemyorder.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class LoginMapsActivity extends AppCompatActivity {
    public final static String RESTAURANTS_INFO_KEY = "RESTAURANTS_INFO";
    public final static String USER_INFO_KEY = "USER_INFO_KEY";
    public static final int RC_SIGN_IN = 1;

    @BindView(R.id.map)
    ImageView map;

    @BindView(R.id.login)
    ImageView login;

    @Inject
    Context context;

    private ApplicationModule applicationModule;
    private NetworkComponent networkComponent;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference restaurantsReference;
    private DatabaseReference usersReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private boolean logindRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_maps);

        TakeMyOrderApplication.appComponent().inject(this);

        applicationModule = new ApplicationModule(context);

        networkComponent = DaggerNetworkComponent.builder().applicationModule(applicationModule).build();

        initUi();

        firebaseDatabase = FirebaseDatabase.getInstance();
        restaurantsReference = firebaseDatabase.getReference("restaurants");

        //Intent intent = new Intent(this, RestaurantsMapActivity.class);
        //startActivity(intent);

        //PostMockData.postMockData();

//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference ref = db.getReference("restaurants");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int f = 5;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                int t = 6;
//            }
//        });
    }

    private void initUi(){
        ButterKnife.bind(this);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurantsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Restaurant> restaurants = new ArrayList<>();
                        for(DataSnapshot restaurantSnapshot : dataSnapshot.getChildren())
                        {
                            restaurants.add(restaurantSnapshot.getValue(Restaurant.class));
                        }

                        Intent intent = new Intent(LoginMapsActivity.this, RestaurantsMapActivity.class);
                        intent.putParcelableArrayListExtra(RESTAURANTS_INFO_KEY, (ArrayList<Restaurant>) restaurants);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the reference to FirebaseAuth
                firebaseAuth = FirebaseAuth.getInstance();

                logindRequest = true;

                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (!logindRequest) return;

                        if (user != null){
                            logindRequest = false;

                            //Connect to Firebase, check if user is a Waiter
                            String email = user.getEmail();
                            usersReference = firebaseDatabase.getReference("waiters");
                            usersReference
                                    .orderByChild("email")
                                    .equalTo(email)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //If dataSnapshot.getValue() is equal to null, a customer is logging into the system
                                            if (dataSnapshot.getValue() == null){
                                                //Customer login
                                                Timber.d("Logging in user: " + user.getEmail());
                                                Intent intent = new Intent(context, CustomerMainActivity.class);
                                                Customer customer = new Customer();
                                                customer.setEmail(user.getEmail());
                                                customer.setUserName(user.getDisplayName());
                                                intent.putExtra(USER_INFO_KEY, customer);
                                                startActivity(intent);

                                            } else {
                                                //Waiter login
                                                AuthUI.getInstance().signOut(LoginMapsActivity.this);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            //TODO: add facebook login
                            startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setIsSmartLockEnabled(false)
                                            .setAvailableProviders(Arrays.asList(
                                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                                            .build(),
                                    RC_SIGN_IN);
                        }
                    }
                };

                firebaseAuth.addAuthStateListener(authStateListener);
            }
        });
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK){
//
//        }
//    }
}
