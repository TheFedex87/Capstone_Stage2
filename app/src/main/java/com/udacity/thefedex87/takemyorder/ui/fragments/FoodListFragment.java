package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.model.Order;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by federico.creti on 14/06/2018.
 */

public class FoodListFragment extends Fragment {
    private Order order;


    public FoodListFragment(){

    }

    public void setOrder(Order order){
        this.order = order;
        //textView.setText(order.getUserId());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }
}
