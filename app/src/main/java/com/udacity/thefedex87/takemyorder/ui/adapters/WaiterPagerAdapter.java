package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.models.WaiterCall;
import com.udacity.thefedex87.takemyorder.models.WaiterReadyOrder;
import com.udacity.thefedex87.takemyorder.ui.fragments.MenuSingleFragment;
import com.udacity.thefedex87.takemyorder.ui.fragments.WaiterCallsFragment;
import com.udacity.thefedex87.takemyorder.ui.fragments.WaiterReadyOrderFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by feder on 12/07/2018.
 */

public class WaiterPagerAdapter extends FragmentPagerAdapter {
//    private List<WaiterCall> calls;
//    private List<WaiterReadyOrderFragment> readyOrders;
    private String restaurantId;
    private WaiterCallsFragment waiterCallsFragment;
    private WaiterReadyOrderFragment waiterReadyOrder;

    @Inject
    Context context;

    public WaiterPagerAdapter(FragmentManager fragmentManager, String restaurantId){
        super(fragmentManager);

        TakeMyOrderApplication.appComponent().inject(this);
        this.restaurantId = restaurantId;
    }

    public void setCalls(List<WaiterCall> calls){
        waiterCallsFragment.setCalls(calls);
    }

    public void setReadyOrders(List<WaiterReadyOrder> readyOrders){
        waiterReadyOrder.setReadyOrders(readyOrders);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        if (fragment instanceof WaiterCallsFragment) {
            ((WaiterCallsFragment) fragment).setRestaurantId(restaurantId);
            this.waiterCallsFragment = (WaiterCallsFragment) fragment;
        }
        else if(fragment instanceof  WaiterReadyOrderFragment){
            ((WaiterReadyOrderFragment)fragment).setRestaurantId(restaurantId);
            waiterReadyOrder = (WaiterReadyOrderFragment)fragment;
        }
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                WaiterCallsFragment waiterCallsFragment = (WaiterCallsFragment) Fragment.instantiate(context, WaiterCallsFragment.class.getName());
                //waiterCallsFragment.setRestaurantId(restaurantId);
                return waiterCallsFragment;
            case 1:
                WaiterReadyOrderFragment waiterReadyOrderFragment = (WaiterReadyOrderFragment)Fragment.instantiate(context, WaiterReadyOrderFragment.class.getName());
                //waiterReadyOrderFragment.setRestaurantId(restaurantId);
                return waiterReadyOrderFragment;
            default:
                return null;
        }
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
//    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object fragment) {
        return ((Fragment) fragment).getView() == view;
    }
}
