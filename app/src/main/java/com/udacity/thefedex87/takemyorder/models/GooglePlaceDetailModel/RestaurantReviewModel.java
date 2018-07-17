package com.udacity.thefedex87.takemyorder.models.GooglePlaceDetailModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by federico.creti on 11/06/2018.
 */

//This class is used by Retrofit as model to map the response from Google Place: reviews
public class RestaurantReviewModel {
    @SerializedName("author_name")
    String authorName;

    @SerializedName("rating")
    double rating;

    @SerializedName("text")
    String review;

    @SerializedName("time")
    long time;

    @SerializedName("relative_time_description")
    String relativeTimeDescription;

    @SerializedName("profile_photo_url")
    String profilePhotoUrl;

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

    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    public void setRelativeTimeDescription(String relativeTimeDescription) {
        this.relativeTimeDescription = relativeTimeDescription;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
