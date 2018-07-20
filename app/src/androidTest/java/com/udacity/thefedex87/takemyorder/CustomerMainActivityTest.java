package com.udacity.thefedex87.takemyorder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.RestaurantMenuActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by feder on 19/07/2018.
 */
@RunWith(AndroidJUnit4.class)
public class CustomerMainActivityTest {
    @Rule
    public ActivityTestRule<CustomerMainActivity> activityCustomerMainActivity = new ActivityTestRule<CustomerMainActivity>(CustomerMainActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation()
                    .getTargetContext();

            Intent intent = new Intent(targetContext, CustomerMainActivity.class);
            intent.putExtra(LoginMapsActivity.USER_RESTAURANT_KEY, "-LGAeRwxB26hjSpQCeuX");
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
    public void testIfClickingOnFavouritesMenuFavouritesFoodsActivityIsOpened(){
        //openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withMenuIdOrText(R.id.favourites, R.string.favourites)).perform(click());
        onView(withId(R.id.activity_title)).check(matches(withText(R.string.favourites)));
    }

    @Test
    public void testIfClickingOnCheckoutOrderMenuCheckoutOrderActivityIsOpened(){
        onView(withMenuIdOrText(R.id.checkout_order, R.string.checkout_order)).perform(click());
        onView(withId(R.id.receipt_image)).check(matches(isDisplayed()));
    }

    public static Matcher<View> withMenuIdOrText(@IdRes int id, @StringRes int menuText) {
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
