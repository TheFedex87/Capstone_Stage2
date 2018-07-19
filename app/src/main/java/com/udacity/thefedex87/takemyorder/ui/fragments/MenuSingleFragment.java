package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.DishDescriptionActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.UserRoomContainer;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInMenuAdapter;
import com.udacity.thefedex87.takemyorder.room.DBManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.view.View.ALPHA;
import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;

/**
 * Created by feder on 16/06/2018.
 */

//Fragment which contains a single food category, it is the fragment used by FoodTypePagerAdapter
public class MenuSingleFragment extends Fragment implements FoodInMenuAdapter.FoodInMenuActionClick {
    private List<Meal> meals;
    private String restaurantId;

    private List<Meal> currentOrder;

    @BindView(R.id.foods_in_menu_container)
    RecyclerView foodInMenuContainer;

    @BindView(R.id.food_list_placeholder)
    TextView foodListPlaceholder;

    @Nullable
    @BindView(R.id.divider)
    View divider;

    @Nullable
    @BindView(R.id.details_container)
    NestedScrollView detailsContainer;

    private FoodInMenuAdapter foodInMenuAdapter;
    private boolean isTwoPanelsMode;

    @Inject
    Context applicationContext;


    private AppDatabase db;

    private DishDescriptionFragment dishDescriptionFragment;

    private UserInterfaceComponent userInterfaceComponent;
    public MenuSingleFragment(){
        db = AppDatabase.getInstance(getActivity());
    }


    public void setMeals(List<Meal> meals){
        this.meals = meals;
        if (foodInMenuAdapter != null)
            foodInMenuAdapter.setMeals(meals);

        //If for this category the are not foods, show the text "No foods for this category"
        if (foodListPlaceholder != null) {
            if (meals.size() > 0) {
                foodListPlaceholder.setVisibility(View.GONE);
                if (divider != null) divider.setVisibility(View.VISIBLE);
            } else {
                foodListPlaceholder.setVisibility(View.VISIBLE);
                if (divider != null) divider.setVisibility(View.GONE);
            }
        }
    }

    public Meal getSelectedMeal(){
        return foodInMenuAdapter.getSelectedMeal();
    }

    public DishDescriptionFragment getSelectedDishDescriptionFragment(){
        return dishDescriptionFragment;
    }

    public void setRestaurantId(String restaurantId){
        this.restaurantId = restaurantId;
        if (foodInMenuAdapter != null) foodInMenuAdapter.setRestaurantId(restaurantId);
    }

    public void setCurrentOrder(List<Meal> currentOrder){
        this.currentOrder = currentOrder;
    }

    @Override
    public void onAttach(Context context) {
        TakeMyOrderApplication.appComponent().inject(this);
        userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(new ApplicationModule(applicationContext))
                .userInterfaceModule(
                        new UserInterfaceModule(LinearLayoutManager.VERTICAL, this, (AppCompatActivity)getActivity()))
                .build();

        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Created MenuSingleFragment");
        View viewRoot = inflater.inflate(R.layout.menu_single_fragment, container, false);

        ButterKnife.bind(this, viewRoot);

        isTwoPanelsMode = !(divider == null);

        if(isTwoPanelsMode)
            detailsContainer.setVisibility(View.GONE);

        //Setup the recylcer view of this food category
        foodInMenuAdapter = userInterfaceComponent.getFoodInMenuAdapter();
        foodInMenuContainer.setAdapter(foodInMenuAdapter);
        foodInMenuContainer.setLayoutManager(userInterfaceComponent.getGridLayoutManager());
        foodInMenuAdapter.setMeals(meals);
        foodInMenuAdapter.setRestaurantId(restaurantId);
        foodInMenuAdapter.setIsTwoPanelsMode(isTwoPanelsMode);

        if (meals != null) {
            if (meals.size() > 0) {
                foodListPlaceholder.setVisibility(View.GONE);
                if (divider != null) divider.setVisibility(View.VISIBLE);
            } else {
                foodListPlaceholder.setVisibility(View.VISIBLE);
                if (divider != null) divider.setVisibility(View.GONE);
            }
        }

        return viewRoot;
    }



    @Override
    public void addOrderClick(final Meal selectedMeal, final View sender, final View imageView, final ViewGroup foodImageContainer, final ImageView originalImage) {
        sender.setEnabled(false);

        final ViewGroup parentViewGroup;

        if(!isTwoPanelsMode){
            parentViewGroup = (ViewGroup)foodInMenuContainer
                    .getParent().getParent().getParent().getParent()
                    .getParent().getParent();
        } else {
            parentViewGroup = (ViewGroup)foodInMenuContainer
                    .getParent().getParent().getParent().getParent()
                    .getParent().getParent().getParent();
        }

        //Extract the position of controls involved into the animation
        parentViewGroup.getOverlay().add(imageView);
        final int[] parentPos = new int[2];
        parentViewGroup.getLocationOnScreen(parentPos);

        selectedMeal.setUserId(((UserRoomContainer)getActivity()).getUserRoomId());

        final int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 0;
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        final AppBarLayout appBar = parentViewGroup.findViewById(R.id.app_bar);
        final ImageView menuIcon = parentViewGroup.findViewById(R.id.menu_icon);

        final int[] appBarPos = new int[2];
        appBar.getLocationOnScreen(appBarPos);

        final int[] icMenuPos = new int[2];

        final int[] foodIcoPos = new int[2];
        imageView.getLocationOnScreen(foodIcoPos);


        PropertyValuesHolder translationAppBar = PropertyValuesHolder.ofFloat(TRANSLATION_Y, statusBarHeight - appBarPos[1]);

        final ObjectAnimator translateAppBarAnimator = ObjectAnimator.ofPropertyValuesHolder(appBar, translationAppBar);

        translateAppBarAnimator.setDuration(50);
        translateAppBarAnimator.start();
        translateAppBarAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                menuIcon.getLocationOnScreen(icMenuPos);

                PropertyValuesHolder scaleXFoodImage = PropertyValuesHolder.ofFloat(SCALE_X, 0.1f);
                PropertyValuesHolder scaleYFoodImage = PropertyValuesHolder.ofFloat(SCALE_Y, 0.1f);
                PropertyValuesHolder alphaFoodImage = PropertyValuesHolder.ofFloat(ALPHA, 0f);

                Interpolator interpolator = AnimationUtils.loadInterpolator(getContext(), android.R
                        .interpolator.fast_out_slow_in);
                PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(ALPHA, 1);
                PropertyValuesHolder transX = PropertyValuesHolder.ofFloat(TRANSLATION_X, icMenuPos[0] - foodIcoPos[0] - parentPos[0] - 75);
                PropertyValuesHolder transY = PropertyValuesHolder.ofFloat(TRANSLATION_Y, icMenuPos[1] - foodIcoPos[1] - parentPos[1] - 75);
                PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(SCALE_X, 0.3f);
                PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(SCALE_Y, 0.3f);

                final ObjectAnimator scaleFoodImageAnimator = ObjectAnimator.ofPropertyValuesHolder(originalImage, scaleXFoodImage, scaleYFoodImage, alphaFoodImage).setDuration(150);
                scaleFoodImageAnimator.start();

                ObjectAnimator moveFoodImageAnimator = ObjectAnimator.ofPropertyValuesHolder(imageView, alpha, transX, transY, scaleX, scaleY).setDuration(650);
                moveFoodImageAnimator.setInterpolator(interpolator);
                moveFoodImageAnimator.setDuration(400);
                moveFoodImageAnimator.setStartDelay(75);
                moveFoodImageAnimator.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        parentViewGroup.getOverlay().remove(imageView);
                        foodImageContainer.addView(imageView);

                        imageView.animate().alpha(0).setDuration(0).start();
                        imageView.animate().translationY(0).setDuration(0).start();
                        imageView.animate().translationX(0).setDuration(0).start();
                        imageView.animate().scaleX(1).setDuration(0).start();
                        imageView.animate().scaleY(1).setDuration(0).start();

                        scaleFoodImageAnimator.reverse();

                        final AppDatabase db = AppDatabase.getInstance(getActivity());

                        //Add the selected order into the current food list
                        selectedMeal.setRestaurantId(restaurantId);
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                db.currentOrderDao().insertFood(selectedMeal);
                            }
                        });

                        animation.removeListener(this);

                        PropertyValuesHolder translationAppBar = PropertyValuesHolder.ofFloat(TRANSLATION_Y, 0);
                        ObjectAnimator translateAppBarReverseAnimator = ObjectAnimator.ofPropertyValuesHolder(appBar, translationAppBar);
                        translateAppBarReverseAnimator.setStartDelay(300);
                        translateAppBarReverseAnimator.setDuration(50);
                        translateAppBarReverseAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                sender.setEnabled(true);
                                animation.removeListener(this);
                            }
                        });
                        translateAppBarReverseAnimator.start();

                    }
                });
                moveFoodImageAnimator.start();
            }
        });

    }

    @Override
    public void subtractFood(final Meal selectedMeal) {
        //Subtract one unit from this food from current order
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Meal mealToDelete = null;
                if (currentOrder != null) {
                    for (Meal meal : currentOrder) {
                        if (meal.getMealId().equals(selectedMeal.getMealId())) {
                            mealToDelete = meal;
                        }
                    }

                    if (mealToDelete != null)
                        db.currentOrderDao().deleteFood(mealToDelete);
                }
            }
        });
    }

    @Override
    public void showDishDetails(Meal meal, ImageView foodImage) {
        if (!isTwoPanelsMode) {
            Intent intent = new Intent(getActivity(), DishDescriptionActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(CustomerMainActivity.FOOD_DESCRIPTION_KEY, meal);
            b.putString(CustomerMainActivity.RESTAURANT_ID_KEY, restaurantId);
            b.putLong(CustomerMainActivity.USER_ID_KEY, ((UserRoomContainer) getActivity()).getUserRoomId());
            intent.putExtras(b);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle transitionBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), foodImage, foodImage.getTransitionName()).toBundle();
                startActivity(intent, transitionBundle);
            } else {
                startActivity(intent);
            }
        } else {
            //if (meal instanceof Food) {
                detailsContainer.setVisibility(View.VISIBLE);
//                DishDescriptionFragment dishDescriptionFragment = new DishDescriptionFragment();
//                dishDescriptionFragment.setData((Food)meal, restaurantId);
//
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.food_detail_container_into_double_panel, dishDescriptionFragment)
//                        .commit();

//                detailsContainer.setVisibility(View.VISIBLE);
                dishDescriptionFragment = (DishDescriptionFragment)getChildFragmentManager().findFragmentById(R.id.food_detail_container_into_double_panel);
                dishDescriptionFragment.setData(meal, restaurantId);
            //}
        }
    }

    @Override
    public void addRemoveFavourite(Food food, final FavouriteMeal favouriteMealFromDB, ViewModel viewModel) {
        if (favouriteMealFromDB == null) {
            DBManager.saveFavouritesIntoDB(db,
                    viewModel,
                    getActivity(),
                    food,
                    restaurantId,
                    ((UserRoomContainer)getActivity()).getUserRoomId());
        } else{
            DBManager.removeFromFavourite(db,
                    favouriteMealFromDB.getId(),
                    ((UserRoomContainer)getActivity()).getUserRoomId());
        }
    }
}
