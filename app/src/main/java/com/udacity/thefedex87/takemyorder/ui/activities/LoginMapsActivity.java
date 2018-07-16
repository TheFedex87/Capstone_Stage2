package com.udacity.thefedex87.takemyorder.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.udacity.thefedex87.takemyorder.models.Waiter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.view.View.ROTATION;
import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;

public class LoginMapsActivity extends AppCompatActivity {
    //Keys
    public final static String RESTAURANTS_INFO_KEY = "RESTAURANTS_INFO";
    public final static String USER_INFO_KEY = "USER_INFO_KEY";
    public final static String USER_RESTAURANT_KEY = "USER_RESTAURANT_KEY";
    public final static String USER_RESTAURANT_TABLE_KEY = "USER_RESTAURANT_TABLE_KEY";

    public final static String WAITER_RESTAURANT_KEY = "WAITER_RESTAURANT_KEY";

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

        Bundle b;
        if(getIntent() != null){
            b = getIntent().getExtras();
            if (b != null) {
                String d = b.getString("key1");
                int f = 55;
            }
        }

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
                                                customer = new Customer();
                                                customer.setEmail(user.getEmail());
                                                customer.setUserName(user.getDisplayName());

                                                Intent intent = new Intent(LoginMapsActivity.this, BarcodeScannerActivity.class);
                                                startActivityForResult(intent, RC_LIVE_BARCODE_SCAN);
                                            } else {
                                                //Waiter login
                                                //AuthUI.getInstance().signOut(LoginMapsActivity.this);


                                                Waiter waiter = null;
                                                for(DataSnapshot waiterSnapshot : dataSnapshot.getChildren()){
                                                    waiter = waiterSnapshot.getValue(Waiter.class);
                                                }

//                                                FirebaseMessaging.getInstance().subscribeToTopic("orders_" + waiter.getRestaurantId());
//                                                FirebaseMessaging.getInstance().subscribeToTopic("calls_" + waiter.getRestaurantId())
//                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                String msg;
//                                                                if (task.isSuccessful())
//                                                                    msg="Subscribed";
//                                                                else
//                                                                    msg="Subscribed error";
//                                                                Timber.d(msg);
//                                                            }
//                                                        });



                                                Intent intent = new Intent(LoginMapsActivity.this, WaiterMainActivity.class);
                                                intent.putExtra(WAITER_RESTAURANT_KEY, waiter.getRestaurantId());
                                                startActivity(intent);
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
//        float startAngle = -30;
//        header.animate().rotation(startAngle).setDuration(0).start();
//        header.animate().translationY(-140).setDuration(0).start();
//
////        AnimatorSet headerEntryAnimation = (AnimatorSet) AnimatorInflater
////                .loadAnimator(this, R.animator.pendulum);
////        headerEntryAnimation.setTarget(header);
////        headerEntryAnimation.start();
//
//        ObjectAnimator translateAnimation = ObjectAnimator.ofFloat(header, TRANSLATION_Y, 0);
//        translateAnimation.setInterpolator(AnimationUtils.loadInterpolator(LoginMapsActivity.this, android.R
//                .interpolator.fast_out_slow_in));
//        translateAnimation.setDuration(1500);
//
//        AnimatorSet headerEntryAnimation = new AnimatorSet();
//        headerEntryAnimation.playSequentially(buildPendulumAnimations(startAngle, header));
//        headerEntryAnimation.play(translateAnimation);
//        headerEntryAnimation.start();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        header.animate().translationX(-width).setDuration(0).start();
        map.animate().scaleX(0).setDuration(0).start();
        map.animate().scaleY(0).setDuration(0).start();
        login.animate().scaleX(0).setDuration(0).start();
        login.animate().scaleY(0).setDuration(0).start();

        AnimatorSet headerEntyAnimation = new AnimatorSet();
        headerEntyAnimation.playTogether(buildBrakeAnimations(header));
        headerEntyAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                AnimatorSet animatorSetScaleMap = new AnimatorSet();
                ObjectAnimator scaleMapXAnimation = ObjectAnimator.ofFloat(map, SCALE_X, 1);
                ObjectAnimator scaleMapYAnimation = ObjectAnimator.ofFloat(map, SCALE_Y, 1);
                animatorSetScaleMap.playTogether(scaleMapXAnimation, scaleMapYAnimation);
                animatorSetScaleMap.setDuration(350);

                AnimatorSet animatorSetScaleLogin = new AnimatorSet();
                ObjectAnimator scaleLoginXAnimation = ObjectAnimator.ofFloat(login, SCALE_X, 1);
                ObjectAnimator scaleLoginYAnimation = ObjectAnimator.ofFloat(login, SCALE_Y, 1);
                animatorSetScaleLogin.playTogether(scaleLoginXAnimation, scaleLoginYAnimation);
                animatorSetScaleLogin.setDuration(350);

                AnimatorSet animatorScale = new AnimatorSet();
                animatorScale.playSequentially(animatorSetScaleMap, animatorSetScaleLogin);
                animatorScale.start();
            }
        });
        headerEntyAnimation.start();
    }

    private List<Animator> buildBrakeAnimations(View target){
        List<Animator> animations = new ArrayList<>();
        ObjectAnimator translateX = ObjectAnimator.ofFloat(target, TRANSLATION_X, 100);
//        translateX.setInterpolator(AnimationUtils.loadInterpolator(LoginMapsActivity.this, android.R
//                .interpolator.fas));
        translateX.setDuration(800);
        animations.add(translateX);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(target, ROTATION, 10);
        rotate.setStartDelay(600);
        rotate.setDuration(200);
        animations.add(rotate);

        ObjectAnimator rotateBack = ObjectAnimator.ofFloat(target, ROTATION, 0);
        rotateBack.setStartDelay(800);
        rotateBack.setDuration(300);
        animations.add(rotateBack);

        ObjectAnimator translateBack = ObjectAnimator.ofFloat(target, TRANSLATION_X, 0);
        translateBack.setStartDelay(800);
        translateBack.setDuration(300);
        animations.add(translateBack);

        return animations;
    }

    private List<Animator> buildPendulumAnimations(float startAngle, View target){
        List<Animator> animations = new ArrayList<>();
        float angle = startAngle;
        float reduction = 10;
        boolean CW = false;
        while(true) {
            angle = angle * -1;
            if(angle > 0)
                angle -= reduction;
            else
                angle += reduction;

            if (reduction > 0.5)
                reduction *= 0.6;

            ObjectAnimator animation = ObjectAnimator.ofFloat(target, ROTATION, angle);
            animation.setDuration(200);
            animations.add(animation);
            if((angle > 0 && CW) || (angle < 0 && !CW)) break;
            CW = !CW;
        }
        ObjectAnimator animation = ObjectAnimator.ofFloat(target, ROTATION, -angle);
        animation.setDuration(200);
        animations.add(animation);
        return animations;
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
