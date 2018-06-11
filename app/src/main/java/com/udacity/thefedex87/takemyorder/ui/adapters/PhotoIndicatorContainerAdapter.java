package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.udacity.thefedex87.takemyorder.R;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class PhotoIndicatorContainerAdapter extends BaseAdapter {
    private int activePhoto;
    private int numberOfPhoto;
    private Context context;

    public PhotoIndicatorContainerAdapter(Context context, int activePhoto, int numberOfPhoto) {
        this.context = context;
        this.numberOfPhoto = numberOfPhoto;
    }

    @Override
    public int getCount() {
        return numberOfPhoto;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        View newView = inflater.inflate(R.layout.photo_indicator, viewGroup, false);

        return null;
    }
}
