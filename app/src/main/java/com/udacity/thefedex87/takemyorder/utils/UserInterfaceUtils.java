package com.udacity.thefedex87.takemyorder.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.udacity.thefedex87.takemyorder.R;

/**
 * Created by feder on 12/06/2018.
 */

public final class UserInterfaceUtils {
    public static void SetAverageDots(double rating, ImageView average1, ImageView average2, ImageView average3, ImageView average4, ImageView average5, Context context){
        int intRating = (int)rating;
        double decimalRating = rating - intRating;
        switch ((int)rating){
            case 1:
                average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                if(decimalRating >= 0.25 && decimalRating <= 0.75){
                    average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_half));
                } else if(decimalRating > 0.75) {
                    average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                }
                break;
            case 2:
                average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                if(decimalRating >= 0.25 && decimalRating <= 0.75){
                    average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_half));
                } else if(decimalRating > 0.75) {
                    average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                }
                break;
            case 3:
                average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                if(decimalRating >= 0.25 && decimalRating <= 0.75){
                    average4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_half));
                } else if(decimalRating > 0.75) {
                    average4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                }
                break;
            case 4:
                average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                if(decimalRating >= 0.25 && decimalRating <= 0.75){
                    average5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_half));
                } else if(decimalRating > 0.75) {
                    average5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                }
                break;
            case 5:
                average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                average5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                break;
        }
    }
}
