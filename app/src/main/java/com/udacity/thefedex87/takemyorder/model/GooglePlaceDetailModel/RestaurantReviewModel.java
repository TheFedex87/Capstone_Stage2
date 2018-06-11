package com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class RestaurantReviewModel {
    @SerializedName("author_name")
    String authorName;
    @SerializedName("rating")
    double rating;
    @SerializedName("text")
    String review;
    @SerializedName("time")
    long time;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
