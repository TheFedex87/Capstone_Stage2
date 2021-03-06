package com.udacity.thefedex87.takemyorder.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.executors.AppExecutors;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInMenuAdapter;
import com.udacity.thefedex87.takemyorder.ui.fragments.DishDescriptionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static android.view.View.ALPHA;
import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;

public class DishDescriptionActivity extends AppCompatActivity implements UserRoomContainer {
    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.dish_description_meal_image)
    ImageView dishDescriptionMealImage;

    @BindView(R.id.food_image_to_animate)
    CircleImageView foodImageToAnimate;

    @BindView(R.id.add_to_order_fab)
    FloatingActionButton addToOrderFab;

    @BindView(R.id.dishDetailContainer)
    CoordinatorLayout dishDetailsContainer;

    @BindView(R.id.dish_description_food_image_container)
    RelativeLayout dishDescriptionFoodImageContainer;

    private Food food;
    private String restaurantId;
    private DishDescriptionFragment dishDescriptionFragment;
    private long userRoomId;
    private boolean addingOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_description);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if(bundle != null && bundle.containsKey(CustomerMainActivity.FOOD_DESCRIPTION_KEY) && bundle.containsKey(CustomerMainActivity.RESTAURANT_ID_KEY)){
                ButterKnife.bind(this);

                food = bundle.getParcelable(CustomerMainActivity.FOOD_DESCRIPTION_KEY);
                restaurantId = bundle.getString(CustomerMainActivity.RESTAURANT_ID_KEY);
                userRoomId = bundle.getLong(CustomerMainActivity.USER_ID_KEY, -1);

                initUi();

                dishDescriptionFragment = (DishDescriptionFragment) getSupportFragmentManager().findFragmentById(R.id.dish_description);
                dishDescriptionFragment.setData(food, restaurantId);

                //Set the current meal into the current order, and creating of the animation
                addToOrderFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!addingOrder) {
                            //Setting this variable a true, to indicate that there is insertion in course, this is used to avoid that
                            //the add button is pressed again until the animation is not finished
                            addingOrder = true;
                            final AppDatabase db = AppDatabase.getInstance(DishDescriptionActivity.this);

                            dishDetailsContainer.getOverlay().add(foodImageToAnimate);

                            //Takes the location on screen of the views involved into the animation

                            final int[] parentPos = new int[2];
                            dishDetailsContainer.getLocationOnScreen(parentPos);

                            final int[] fabPos = new int[2];
                            addToOrderFab.getLocationOnScreen(fabPos);

                            final int[] foodImagePos = new int[2];
                            foodImageToAnimate.getLocationOnScreen(foodImagePos);

                            PropertyValuesHolder alphaFoodImage = PropertyValuesHolder.ofFloat(ALPHA, 0);
                            PropertyValuesHolder scaleXFoodImage = PropertyValuesHolder.ofFloat(SCALE_X, 0.2f);
                            PropertyValuesHolder scaleYFoodImage = PropertyValuesHolder.ofFloat(SCALE_Y, 0.2f);
                            final ObjectAnimator scaleFoodImageAnimator = ObjectAnimator.ofPropertyValuesHolder(dishDescriptionMealImage, alphaFoodImage, scaleXFoodImage, scaleYFoodImage);
                            scaleFoodImageAnimator.setDuration(200);
                            scaleFoodImageAnimator.start();

                            PropertyValuesHolder alphaImageFoodToAnimate = PropertyValuesHolder.ofFloat(ALPHA, 1);
                            PropertyValuesHolder transXImageFoodToAnimate = PropertyValuesHolder.ofFloat(TRANSLATION_X, fabPos[0] - foodImagePos[0] - parentPos[0]);
                            PropertyValuesHolder transYImageFoodToAnimate = PropertyValuesHolder.ofFloat(TRANSLATION_Y, fabPos[1] - foodImagePos[1] - parentPos[1]);

                            ObjectAnimator imageFoodToAnimateAnimation = ObjectAnimator.ofPropertyValuesHolder(foodImageToAnimate, alphaImageFoodToAnimate, transXImageFoodToAnimate, transYImageFoodToAnimate);
                            imageFoodToAnimateAnimation.setDuration(400);
                            imageFoodToAnimateAnimation.setStartDelay(100);
                            imageFoodToAnimateAnimation.start();


                            ObjectAnimator foodImageToAnimateAlphaAnimation = ObjectAnimator.ofFloat(foodImageToAnimate, ALPHA, 0);
                            foodImageToAnimateAlphaAnimation.setDuration(100);
                            foodImageToAnimateAlphaAnimation.setStartDelay(400);
                            foodImageToAnimateAlphaAnimation.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);

                                    animation.removeListener(this);

                                    dishDetailsContainer.getOverlay().remove(foodImageToAnimate);
                                    dishDescriptionFoodImageContainer.addView(foodImageToAnimate);

                                    foodImageToAnimate.animate().translationX(0).setDuration(0);
                                    foodImageToAnimate.animate().translationY(0).setDuration(0);

                                    scaleFoodImageAnimator.reverse();
                                    scaleFoodImageAnimator.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            addingOrder = false;
                                        }
                                    });
                                }
                            });
                            foodImageToAnimateAlphaAnimation.start();

                            food.setUserId(userRoomId);
                            food.setRestaurantId(restaurantId);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    db.currentOrderDao().insertFood(food);
                                }
                            });
                        }
                    }
                });
            } else {
                if (bundle == null)
                    Timber.e("Missing Bundle or requested keys");
                else if(!bundle.containsKey(CustomerMainActivity.FOOD_DESCRIPTION_KEY))
                    Timber.e("Missing FOOD_DESCRIPTION_KEY from bundle");
                else
                    Timber.e("Missing RESTAURANT_ID_KEY from bundle");
                finish();
            }
        } else {
            Timber.e("No intent provided");
            finish();
        }
    }

    private void initUi(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(food.getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dishDescriptionMealImage.setTransitionName(FoodInMenuAdapter.IMAGE_TRANSITION_NAME);
            ActivityCompat.postponeEnterTransition(this);
        }

        NetworkComponent networkComponent = DaggerNetworkComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        String imagePath = "https://firebasestorage.googleapis.com/v0/b/takemyorder-8a08a.appspot.com/o/meals_images%2F" + food.getMealId() +  "?alt=media";
        networkComponent.getPicasso().load(imagePath).into(dishDescriptionMealImage, new Callback() {
            @Override
            public void onSuccess() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityCompat.startPostponedEnterTransition(DishDescriptionActivity.this);
                }
            }

            @Override
            public void onError() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityCompat.startPostponedEnterTransition(DishDescriptionActivity.this);
                }
            }
        });

        //Load the food image inside the CircleImageView used to create the animation
        networkComponent.getPicasso().load(imagePath).into(foodImageToAnimate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public long getUserRoomId(){
        return userRoomId;
    }
}
