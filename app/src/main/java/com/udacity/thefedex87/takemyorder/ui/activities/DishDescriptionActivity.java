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
import com.udacity.thefedex87.takemyorder.ui.fragments.DishDescriptionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.ALPHA;
import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;

public class DishDescriptionActivity extends AppCompatActivity {
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

                initUi();

                dishDescriptionFragment = (DishDescriptionFragment) getSupportFragmentManager().findFragmentById(R.id.dish_description);
                dishDescriptionFragment.setData(food, restaurantId);

                addToOrderFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AppDatabase db = AppDatabase.getInstance(DishDescriptionActivity.this);

                        dishDetailsContainer.getOverlay().add(foodImageToAnimate);

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
                        //PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(SCALE_X, 0.3f);
                        //PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(SCALE_Y, 0.3f);
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
                            }
                        });
                        foodImageToAnimateAlphaAnimation.start();

//                        foodImageToAnimate.animate().alpha(0).setDuration(100).setStartDelay(200).setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//
//                                animation.removeAllListeners();
//
//                                dishDetailsContainer.getOverlay().remove(foodImageToAnimate);
//                                dishDescriptionFoodImageContainer.addView(foodImageToAnimate);
//
//                                foodImageToAnimate.animate().translationX(0).setDuration(0);
//                                foodImageToAnimate.animate().translationY(0).setDuration(0);
//
//                                scaleFoodImageAnimator.reverse();
//                            }
//                        });


                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                db.currentOrderDao().insertFood(food);
                            }
                        });
                    }
                });
            }
        }
    }

    private void initUi(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(food.getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dishDescriptionMealImage.setTransitionName("foodTransition");
            //getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.curve));
        }

        ActivityCompat.postponeEnterTransition(this);

        NetworkComponent networkComponent = DaggerNetworkComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        String imagePath = "https://firebasestorage.googleapis.com/v0/b/takemyorder-8a08a.appspot.com/o/meals_images%2F" + food.getMealId() +  "?alt=media";
        networkComponent.getPicasso().load(imagePath).into(dishDescriptionMealImage, new Callback() {
            @Override
            public void onSuccess() {
                ActivityCompat.startPostponedEnterTransition(DishDescriptionActivity.this);
            }

            @Override
            public void onError() {
                ActivityCompat.startPostponedEnterTransition(DishDescriptionActivity.this);
            }
        });
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
}
