package com.udacity.thefedex87.takemyorder;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.entity.User;
import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.RestaurantMenuActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.fail;

/**
 * Created by feder on 20/07/2018.
 */

@RunWith(AndroidJUnit4.class)
public class RestaurantMenuActivityTest {
    private final static String RESTAURANT_KEY = "-LGAeRwxB26hjSpQCeuX";
    private final static long USER_ID_VALUE = 2;
    private final static String USER_EMAIL_FOR_TESTS = "user@testuser.com";
    private final static String USER_PASSWORD_FOR_TESTS = "usertest";
    private final int DISH_POSITION = 0;
    private final String DISH_NAME = "Microwave Paneer Tikka Recipe";
    private final String DISH_ID = "-LGAeS18R4b_5CDZd7oF";

    private final static long MAX_WAIT_OBSERVER_COMPLETED = 5000;
    private final static long WAIT_OBSERVER_COMPLETED_SLEEP = 100;

    private User user;

    private IdlingResource idlingResource;

    private List<Meal> mealCurrentOrderList;

    private FavouriteMeal favouriteMeal;

    private boolean isOperationCompleted;

    @Rule
    public ActivityTestRule<RestaurantMenuActivity> activityRestaurantMenuActivity = new ActivityTestRule<RestaurantMenuActivity>(RestaurantMenuActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation()
                    .getTargetContext();

            Intent intent = new Intent(targetContext, RestaurantMenuActivity.class);
            intent.putExtra(LoginMapsActivity.USER_RESTAURANT_KEY, RESTAURANT_KEY);
            intent.putExtra(CustomerMainActivity.USER_ID_KEY, USER_ID_VALUE);

            return intent;
        }
    };

    @Before
    public void registerHidlingResource() {
        idlingResource = activityRestaurantMenuActivity.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void checkIfRestaurantMenuIsDownloadAndShowed(){
        onView(allOf(withId(R.id.foods_in_menu_container), isDisplayed())).check(matches(atPosition(DISH_POSITION, hasDescendant(withText(DISH_NAME)))));
    }

    @Test
    public void checkIfClickingOnDetailsOnFoodOpenDidhDetailsActivity(){
        onView(allOf(withId(R.id.foods_in_menu_container), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickOnRecyclerAtViewWithId(R.id.show_food_details)));
        onView(allOf(allOf(withId(R.id.food_price), isDisplayed()), allOf(withId(R.id.dish_description_tv), isDisplayed(), allOf(withId(R.id.ingredients_list), isDisplayed()))));
    }

    @Test
    public void testIfOrderIsAddedToCurrentOrderList() {
        addOrderFood(true);
    }

    @Test
    public void testIfSubtractOrderSubtractOneFoodFromCurrentOrder(){
        addOrderFood(false);

        int prevMealsCount = mealCurrentOrderList.size();
        onView(allOf(withId(R.id.foods_in_menu_container), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickOnRecyclerAtViewWithId(R.id.subtract_food)));
        getCurrentOrderMealsList();
        int newMealsList = mealCurrentOrderList.size();

        if(newMealsList != prevMealsCount - 1){
            fail("Food not removed from current order");
        }
    }

    @Test
    public void testIfAddRemoveFromFavouritesAddRemoveFromFavourites(){
        getUser();

        getMealFavouriteWithId(DISH_ID);

        if(favouriteMeal == null){
            clickAddRemoveFavouriteMeal();
            getMealFavouriteWithId(DISH_ID);
            if(favouriteMeal == null){
                fail("Favourite has not been added to favourites");
            }
            clickAddRemoveFavouriteMeal();
        } else {
            clickAddRemoveFavouriteMeal();
            getMealFavouriteWithId(DISH_ID);
            if(favouriteMeal != null){
                fail("Favourite has not been removed from favourites");
            }
            clickAddRemoveFavouriteMeal();
        }

        try {
            Thread.sleep(WAIT_OBSERVER_COMPLETED_SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    private void clickAddRemoveFavouriteMeal(){
        onView(allOf(withId(R.id.foods_in_menu_container), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(DISH_POSITION, clickOnRecyclerAtViewWithId(R.id.favourite_food)));
    }

    private void addOrderFood(boolean removeAddedFood){
        getUser();

        if (user == null)
            fail("User not found, can't execute test");

        getCurrentOrderMealsList();
        int prevMealsCount = mealCurrentOrderList.size();

        onView(allOf(withId(R.id.foods_in_menu_container), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickOnRecyclerAtViewWithId(R.id.add_to_current_order)));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getCurrentOrderMealsList();
        int newMealsCount = mealCurrentOrderList.size();

        if(newMealsCount != prevMealsCount + 1)
            fail("Not added one food to current order");
        else
            if(removeAddedFood)
                removeLastFoodInCurrentOrder();
    }

    private ViewAction clickOnRecyclerAtViewWithId(@IdRes final int id){
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                View childView = view.findViewById(id);
                childView.performClick();
            }
        };
    }

    private void getMealFavouriteWithId(String mealId){
        isOperationCompleted = false;
        AppDatabase.getInstance(getInstrumentation()
                .getTargetContext()).favouriteMealsDao().getUserFavouriteMealById(mealId, user.getId(), RESTAURANT_KEY).observe(activityRestaurantMenuActivity.getActivity(), new Observer<FavouriteMeal>() {
            @Override
            public void onChanged(@Nullable FavouriteMeal meal) {
                favouriteMeal = meal;
                isOperationCompleted = true;
            }
        });
        int cycle = 0;
        while(true){
            if (cycle * WAIT_OBSERVER_COMPLETED_SLEEP >= MAX_WAIT_OBSERVER_COMPLETED)
                break;

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
                .getTargetContext()).userDao().getUserByUserFirebaseId(FirebaseAuth.getInstance().getUid()).observe(activityRestaurantMenuActivity.getActivity(), new Observer<User>() {
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

        if(user == null || !user.getEmail().equals(USER_EMAIL_FOR_TESTS)){
            fail("Login into the system with the user: " + USER_EMAIL_FOR_TESTS + " with password: usertest");
        }

        if(USER_ID_VALUE != user.getId()){
            fail("Set the constant USER_ID_VALUE to " + user.getId());
        }
    }

    private void getCurrentOrderMealsList(){
        isOperationCompleted = false;
        AppDatabase.getInstance(getInstrumentation()
                .getTargetContext()).currentOrderDao().getCurrentOrderList(user.getId(), RESTAURANT_KEY).observe(activityRestaurantMenuActivity.getActivity(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> meals) {
                mealCurrentOrderList = meals;
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

    private void removeLastFoodInCurrentOrder(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getInstrumentation()
                        .getTargetContext()).currentOrderDao().deleteFood(mealCurrentOrderList.get(mealCurrentOrderList.size() - 1));
            }
        });
    }

    //This is a custom matcher which look in a specific position of a recycler view: https://stackoverflow.com/a/34795431/1857023
    private Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }

                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

//    public static Matcher<View> firstChildOf(final Matcher<View> parentMatcher) {
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("with first child view of type parentMatcher");
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//
//                if (!(view.getParent() instanceof ViewGroup)) {
//                    return parentMatcher.matches(view.getParent());
//                }
//                ViewGroup group = (ViewGroup) view.getParent();
//                return parentMatcher.matches(view.getParent()) && group.getChildAt(0).equals(view);
//
//            }
//        };
//    }
}
