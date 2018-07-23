package com.udacity.thefedex87.takemyorder;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.entity.User;
import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.fail;


/**
 * Created by feder on 19/07/2018.
 */
@RunWith(AndroidJUnit4.class)
public class CustomerMainActivityTest {
    private final static String RESTAURANT_KEY = "-LGAeRwxB26hjSpQCeuX";
    private final static String TABLE_KEY = "3";
    private final static String USER_EMAIL_FOR_TESTS = "user@testuser.com";
    private final static String USER_PASSWORD_FOR_TESTS = "usertest";

    private final static long MAX_WAIT_OBSERVER_COMPLETED = 5000;
    private final static long WAIT_OBSERVER_COMPLETED_SLEEP = 100;

    private User user;


    private List<Meal> mealList;

    private List<WaiterCall> waiterCalls;
    private String lastWaiterCallKey = "";

    private boolean isOperationCompleted;

    @Rule
    public ActivityTestRule<CustomerMainActivity> activityCustomerMainActivity = new ActivityTestRule<CustomerMainActivity>(CustomerMainActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation()
                    .getTargetContext();


            //Check if we are logged with the test user, if not I log him
            if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().getEmail() != USER_EMAIL_FOR_TESTS)
                FirebaseAuth.getInstance().signOut();
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                loginTestUser();
            }



            Room.inMemoryDatabaseBuilder(targetContext, AppDatabase.class);

            Intent intent = new Intent(targetContext, CustomerMainActivity.class);
            intent.putExtra(LoginMapsActivity.USER_RESTAURANT_KEY, RESTAURANT_KEY);
            intent.putExtra(LoginMapsActivity.USER_RESTAURANT_TABLE_KEY, "4");

            return intent;
        }
    };

    @Test
    public void testIfClickingOnFabButtonRestaurantMenuActivityIsOpened(){
        onView(withId(R.id.add_to_order_fab)).perform(click());
        onView(withId(R.id.activity_title)).check(matches(withText("Menu")));
    }

    @Test
    public void testClickingOnFavouritesMenuOpenFavouritesFoodsActivity(){
        //openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withMenuIdOrText(R.id.favourites, R.string.favourites)).perform(click());
        onView(withId(R.id.activity_title)).check(matches(withText(R.string.favourites)));
    }

    @Test
    public void testClickingOnCheckoutOrderMenuOpenCheckoutOrderActivity(){
        onView(withMenuIdOrText(R.id.checkout_order, R.string.checkout_order)).perform(click());
        onView(withId(R.id.receipt_image)).check(matches(isDisplayed()));
    }

    @Test
    public void testClickingOnDeleteCurrentOrderRemoveCurrentOrderList(){

        getUser();

        if (user == null)
            fail("User not found, can't execute test");

        getMealsList();


        if (mealList.size() == 0){
            saveMockMealIntoDb();
        }

        onView(withMenuIdOrText(R.id.delete_current_order, R.string.delete_current_order)).perform(click());

        onView(withText(R.string.confirm_delete_text)).check(matches(isDisplayed()));

        onView(withText("OK")).perform(click());

        getMealsList();

        if(mealList.size() != 0)
            fail("Current order not deleted");
    }

    @Test
    public void testClickingOnCallWaiterAddACallIntoDB(){
        getWaiterCalls();
        int waitersCallPrev = waiterCalls.size();

        onView(withMenuIdOrText(R.id.call_waiter, R.string.call_waiter)).perform(click());
        onView(withText(R.string.confirm_call_waiter_text)).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

        getWaiterCalls();
        int waitersCallPost = waiterCalls.size();

        if(waitersCallPost != waitersCallPrev + 1)
            fail("Not added one waiter call");

        removeLastWaiterCall();
    }

    @AfterClass
    public static void tearDown(){
        FirebaseAuth.getInstance().signOut();
    }

    private void removeLastWaiterCall(){
        FirebaseDatabase.getInstance().getReference("waiters_calls/" + RESTAURANT_KEY + "/" + lastWaiterCallKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                waiterCalls = new ArrayList<>();
                for(DataSnapshot waiterCallSnapshot : dataSnapshot.getChildren()){
                    waiterCallSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getWaiterCalls(){
        isOperationCompleted = false;
        FirebaseDatabase.getInstance().getReference("waiters_calls/" + RESTAURANT_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                waiterCalls = new ArrayList<>();
                for(DataSnapshot waiterCallSnapshot : dataSnapshot.getChildren()){
                    WaiterCall waiterCall = waiterCallSnapshot.getValue(WaiterCall.class);
                    waiterCalls.add(waiterCall);
                    lastWaiterCallKey = waiterCallSnapshot.getKey();
                }
                isOperationCompleted = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        int cycle = 0;
        while(true){
            if (cycle * WAIT_OBSERVER_COMPLETED_SLEEP >= MAX_WAIT_OBSERVER_COMPLETED) break;

            if (isOperationCompleted) break;
            try {
                Thread.sleep(WAIT_OBSERVER_COMPLETED_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cycle++;
        }
    }

    private void loginTestUser(){
        isOperationCompleted = false;
        FirebaseAuth.getInstance().signInWithEmailAndPassword(USER_EMAIL_FOR_TESTS, USER_PASSWORD_FOR_TESTS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) isOperationCompleted = true;
                if(!task.isSuccessful()) fail("Can't login wit test user");
            }
        });

        int cycle = 0;
        while(true){
            if (cycle * WAIT_OBSERVER_COMPLETED_SLEEP >= MAX_WAIT_OBSERVER_COMPLETED) break;

            if (isOperationCompleted) break;
            try {
                Thread.sleep(WAIT_OBSERVER_COMPLETED_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cycle++;
        }
    }

    private void saveMockMealIntoDb(){
        final Meal meal = new Food();
        meal.setUserId(user.getId());
        meal.setFoodType(FoodTypes.STARTER);
        meal.setName("TEST MEAL");
        meal.setPrice(5);
        meal.setRestaurantId(RESTAURANT_KEY);
        meal.setMealId("TEST_MEAL_ID");

        isOperationCompleted = false;
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getInstrumentation()
                        .getTargetContext()).currentOrderDao().insertFood(meal);

                isOperationCompleted = true;
            }
        });

        int cycle = 0;
        while(true){
            if (cycle * WAIT_OBSERVER_COMPLETED_SLEEP >= MAX_WAIT_OBSERVER_COMPLETED) break;

            if (isOperationCompleted) break;
            try {
                Thread.sleep(WAIT_OBSERVER_COMPLETED_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cycle++;
        }
    }

    private void getUser(){
        isOperationCompleted = false;
        AppDatabase.getInstance(getInstrumentation()
                .getTargetContext()).userDao().getUserByUserFirebaseId(FirebaseAuth.getInstance().getUid()).observe(activityCustomerMainActivity.getActivity(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User userDb) {
                user = userDb;
                isOperationCompleted = true;
            }
        });
        int cycle = 0;
        while(true){
            if (cycle * WAIT_OBSERVER_COMPLETED_SLEEP >= MAX_WAIT_OBSERVER_COMPLETED) break;

            if (isOperationCompleted) break;
            try {
                Thread.sleep(WAIT_OBSERVER_COMPLETED_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cycle++;
        }

        if(!user.getEmail().equals(USER_EMAIL_FOR_TESTS))
            fail("Please run the app and loginTestUser into the system with the user: " + USER_EMAIL_FOR_TESTS + " with password: " + USER_PASSWORD_FOR_TESTS);
    }


    private void getMealsList(){
        isOperationCompleted = false;
        AppDatabase.getInstance(getInstrumentation()
                .getTargetContext()).currentOrderDao().getCurrentOrderList(user.getId(), RESTAURANT_KEY).observe(activityCustomerMainActivity.getActivity(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> meals) {
                mealList = meals;
                isOperationCompleted = true;
            }
        });
        int cycle = 0;
        while(true){
            if (cycle * WAIT_OBSERVER_COMPLETED_SLEEP >= MAX_WAIT_OBSERVER_COMPLETED) break;

            if (isOperationCompleted) break;
            try {
                Thread.sleep(WAIT_OBSERVER_COMPLETED_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cycle++;
        }
    }

    private static Matcher<View> withMenuIdOrText(@IdRes int id, @StringRes int menuText) {
        Matcher<View> matcher = withId(id);
        try {
            onView(matcher).check(matches(isDisplayed()));
            return matcher;
        } catch (Exception NoMatchingViewException) {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
            return withText(menuText);
        }
    }
}
