package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.transition.ChangeBounds;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.models.Meal;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInMenuAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.ALPHA;
import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;

/**
 * Created by feder on 16/06/2018.
 */

public class MenuSingleFragment extends Fragment implements FoodInMenuAdapter.FoodInMenuActionClick {
    private List<Meal> meals;

    @BindView(R.id.foods_in_menu_container)
    RecyclerView foodInMenuContainer;

//    @BindView(R.id.menu_icon)
//    ImageView menuIcon;

    private FoodInMenuAdapter foodInMenuAdapter;

    @Inject
    Context applicationContext;

    private ViewGroup container;

    public MenuSingleFragment(){

    }

    public void setMeals(List<Meal> meals){
        this.meals = meals;
        if (foodInMenuAdapter != null)
            foodInMenuAdapter.setMeals(meals);
    }

    @Override
    public void onAttach(Context context) {
        TakeMyOrderApplication.appComponent().inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.menu_single_fragment, container, false);

        ButterKnife.bind(this, viewRoot);

        this.container = container;

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(new ApplicationModule(applicationContext))
                .userInterfaceModule(
                        new UserInterfaceModule(LinearLayoutManager.VERTICAL, this))
                .build();

        foodInMenuAdapter = new FoodInMenuAdapter(applicationContext, this);//userInterfaceComponent.getFoodInMenuAdapter();
        foodInMenuContainer.setAdapter(foodInMenuAdapter);
        foodInMenuContainer.setLayoutManager(userInterfaceComponent.getGridLayoutManager());
        foodInMenuAdapter.setMeals(meals);

        return viewRoot;
    }

    @Override
    public void addOrderClick(final View sender, final View imageView, final ViewGroup foodImageContainer, final ImageView originalImage) {
        //TransitionManager.go(Scene.getSceneForLayout((ViewGroup)getActivity().findViewById(R.id.food_in_menu_container), R.layout.food_in_menu_scene_2, getActivity()));

        sender.setEnabled(false);

        final ViewGroup parentViewGroup = (ViewGroup)foodInMenuContainer
                .getParent().getParent().getParent().getParent()
                .getParent();
        parentViewGroup.getOverlay().add(imageView);
        int[] parentPos = new int[2];
        parentViewGroup.getLocationOnScreen(parentPos);


//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
//
//        Transition t = TransitionInflater.from(getContext()).inflateTransition(R.transition.test);
//        t.setDuration(1000);
//
//        TransitionManager.beginDelayedTransition((ViewGroup)imageView.getParent(), t);
//
//        //imageView.setVisibility(View.GONE);
//        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        imageView.setLayoutParams(lp);



        ImageView menuIcon = parentViewGroup.findViewById(R.id.menu_icon);

        int[] icMenuPos = new int[2];
        menuIcon.getLocationOnScreen(icMenuPos);

        int[] foodIcoPos = new int[2];
        imageView.getLocationOnScreen(foodIcoPos);


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

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(originalImage, scaleXFoodImage, scaleYFoodImage, alphaFoodImage).setDuration(150);
        objectAnimator.start();

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, alpha, transX, transY, scaleX, scaleY).setDuration(650);
        animator.setInterpolator(interpolator);
        animator.setStartDelay(75);
        animator.addListener(new AnimatorListenerAdapter() {

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

                originalImage.animate().scaleY(1f).setDuration(300).start();
                originalImage.animate().scaleX(1f).setDuration(300).start();
                originalImage.animate().alpha(1f).setDuration(100).start();

                sender.setEnabled(true);

//                animation.removeListener(this);
//                animation.setDuration(0);
//                ((ValueAnimator) animation).reverse();
            }
        });
        animator.start();
    }
}
