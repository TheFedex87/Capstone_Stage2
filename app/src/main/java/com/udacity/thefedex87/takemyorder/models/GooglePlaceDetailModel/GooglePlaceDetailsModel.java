package com.udacity.thefedex87.takemyorder.models.GooglePlaceDetailModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class GooglePlaceDetailsModel {
    @SerializedName("name")
    String name;

    @SerializedName("place_id")
    String placeId;

    @SerializedName("rating")
    double rating;

    @SerializedName("formatted_address")
    String formattedAddress;

//    @SerializedName("formatted_phone_number")
//    String formattedPhoneNumber;

    @SerializedName("reviews")
    List<RestaurantReviewModel> reviews;

    @SerializedName("photos")
    List<RestaurantPhotoModel> photos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

//    public String getFormattedPhoneNumber() {
//        return formattedPhoneNumber;
//    }
//
//    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
//        this.formattedPhoneNumber = formattedPhoneNumber;
//    }

    public List<RestaurantReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<RestaurantReviewModel> reviews) {
        this.reviews = reviews;
    }

    public List<RestaurantPhotoModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<RestaurantPhotoModel> photos) {
        this.photos = photos;
    }
}
