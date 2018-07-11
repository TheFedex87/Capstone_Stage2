package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.vision.barcode.Barcode;
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
import com.udacity.thefedex87.takemyorder.models.Customer;
import com.udacity.thefedex87.takemyorder.models.Restaurant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class LoginMapsActivity extends AppCompatActivity {
    //Keys
    public final static String RESTAURANTS_INFO_KEY = "RESTAURANTS_INFO";
    public final static String USER_INFO_KEY = "USER_INFO_KEY";
    public final static String USER_RESTAURANT_KEY = "USER_RESTAURANT_KEY";
    public final static String USER_RESTAURANT_TABLE_KEY = "USER_RESTAURANT_TABLE_KEY";

    //Activity results
    public static final int RC_SIGN_IN = 1;
    public static final int RC_PHOTO_PICKER = 2;
    public static final int RC_PHOTO_SHOT = 3;
    public static final int RC_LIVE_BARCODE_SCAN = 4;

    @BindView(R.id.map)
    ImageView map;

    @BindView(R.id.login)
    ImageView login;

    @BindView(R.id.header)
    RelativeLayout header;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    @Inject
    Context context;

    private ApplicationModule applicationModule;
    private NetworkComponent networkComponent;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference restaurantsReference;
    private DatabaseReference usersReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Customer customer;

    private boolean logindRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_maps);

        Timber.plant(new Timber.DebugTree());

        TakeMyOrderApplication.appComponent().inject(this);

        applicationModule = new ApplicationModule(context);

        networkComponent = DaggerNetworkComponent.builder().applicationModule(applicationModule).build();

        initUi();

        firebaseDatabase = FirebaseDatabase.getInstance();
        restaurantsReference = firebaseDatabase.getReference("restaurants");

        //PostMockData.postMockData();

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

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

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
//                                                AlertDialog.Builder builder;
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    builder = new AlertDialog.Builder(LoginMapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//                                                } else {
//                                                    builder = new AlertDialog.Builder(LoginMapsActivity.this);
//                                                }
//                                                builder.setTitle("Delete entry")
//                                                        .setMessage("Are you sure you want to delete this entry?")
//                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                // continue with delete
//                                                            }
//                                                        })
//                                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                // do nothing
//                                                            }
//                                                        })
//                                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                                        .show();

                                                customer = new Customer();
                                                customer.setEmail(user.getEmail());
                                                customer.setUserName(user.getDisplayName());

                                                Intent intent = new Intent(LoginMapsActivity.this, BarcodeScannerActivity.class);
                                                startActivityForResult(intent, RC_LIVE_BARCODE_SCAN);
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


        setupEntryAnimation();

    }

    private void setupEntryAnimation(){
        float toolBarHeight = getResources().getDimension(R.dimen.main_toolbar_height);
        //header.animate().translationY(-toolBarHeight).setDuration(0).start();

        int startAngle = -15;
        int endAngle = 15;

        //Animation animation = AnimationUtils.loadAnimation(this, R.anim.pendulum_animation);
        //header.setAnimation(animation);
        //animation.start();

        RotateAnimation rotateAnimation = new RotateAnimation(0, -15, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.1f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        header.startAnimation(rotateAnimation);
        //header.animate().rotation(-15).setDuration(0).start();
        pendulumAnimation(startAngle, endAngle, header, true, 6);
    }

    private void pendulumAnimation(final float startAngle, final float endAngle, final View view, final boolean CCW, final float reduction){


//        RotateAnimation rotateAnim = new RotateAnimation(startAngle , endAngle , Animation.RELATIVE_TO_PARENT , x ,
//                Animation.RELATIVE_TO_PARENT , 10);

        final RotateAnimation rotateAnimation = new RotateAnimation(startAngle, endAngle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.1f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(200);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if ((endAngle >= 0 && CCW) || (endAngle <= 0 && !CCW)) {
                    if(CCW) {
                        pendulumAnimation(endAngle,startAngle + reduction, view,false,  reduction > 1 ? reduction - 1f : reduction);
                    } else {
                        pendulumAnimation(endAngle,startAngle - reduction, view,true, reduction > 1 ? reduction - 1f : reduction);
                    }
                } else {
                    RotateAnimation rotateAnimation = new RotateAnimation(endAngle, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.1f);
                    rotateAnimation.setFillAfter(true);
                    rotateAnimation.setInterpolator(AnimationUtils.loadInterpolator(LoginMapsActivity.this, android.R
                            .interpolator.fast_out_slow_in));
                    rotateAnimation.setDuration(200);
                    header.startAnimation(rotateAnimation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        header.startAnimation(rotateAnimation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_LIVE_BARCODE_SCAN && resultCode == RESULT_OK){
            Barcode barcode = data.getParcelableExtra("barcode");
            retrieveBarCode(barcode);
        }
    }

    private void retrieveBarCode(Barcode barcode) {
        //Retrieve the barcode content
        String qrString = barcode.displayValue;

        //Parse the barcode content into a JSONObject
        JSONObject json = null;
        try {
            json = new JSONObject(qrString);
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.e("Errore creating JSON object " + e.getMessage());
            Toast.makeText(this, getString(R.string.error_loading_bitmap), Toast.LENGTH_LONG).show();
            return;
        }

        //Extract the information from the JSONObject
        String restaurantId = null;
        String table = null;
        try {
            restaurantId = json.getString("restaurantId");
            table = json.getString("table");
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (restaurantId != null || table != null) {
            //If information are correctly extracted goto CustomerMainActivity
            //Customer login
            Timber.d("Logging in user: " + customer.getEmail());
            Intent intent = new Intent(context, CustomerMainActivity.class);
            //intent.putExtra(USER_INFO_KEY, customer);
            intent.putExtra(USER_RESTAURANT_KEY, restaurantId);
            intent.putExtra(USER_RESTAURANT_TABLE_KEY, table);
            startActivity(intent);
        } else {
            Timber.w(getString(R.string.error_no_valid_information_from_barcode));
            Toast.makeText(this, getString(R.string.error_no_valid_information_from_barcode), Toast.LENGTH_LONG).show();
        }
    }

//    private Bitmap resizeBitmap(Bitmap bm, int newHeight, int newWidth) {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // CREATE A MATRIX FOR THE MANIPULATION
//        Matrix matrix = new Matrix();
//        // RESIZE THE BIT MAP
//        matrix.postScale(scaleWidth, scaleHeight);
//
//        // "RECREATE" THE NEW BITMAP
//        Bitmap resizedBitmap = Bitmap.createBitmap(
//                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
//        return resizedBitmap;
//    }
//
//    private Bitmap resizeUntilLessDesideredSize(Bitmap bm, int maxBytes) {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//
//        // CREATE A MATRIX FOR THE MANIPULATION
//        Matrix matrix = new Matrix();
//        // RESIZE THE BIT MAP
//        matrix.postScale(0.8f, 0.8f);
//
//        // "RECREATE" THE NEW BITMAP
//        Bitmap resizedBitmap = Bitmap.createBitmap(
//                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
//
//        if (resizedBitmap.getByteCount() <= maxBytes)
//            return resizedBitmap;
//        else
//            return resizeUntilLessDesideredSize(resizedBitmap, maxBytes);
//    }
}
