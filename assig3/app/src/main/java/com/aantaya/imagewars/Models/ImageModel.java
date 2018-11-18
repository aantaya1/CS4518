package com.aantaya.imagewars.Models;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

@IgnoreExtraProperties
public class ImageModel {
    private String title;
    private String description;
    private String imageUrl;
    private String location;

    public ImageModel (){}

    public ImageModel (String mTitle, String mDescription, String mImageUrl, String mLocation){
        if(mLocation == null) mLocation = "N/A";
        this.title = mTitle;
        this.description = mDescription;
        this.imageUrl = mImageUrl;
        this.location = mLocation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Exclude
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageModel)) return false;
        ImageModel model = (ImageModel) o;
        return Objects.equals(getTitle(), model.getTitle()) &&
                Objects.equals(getDescription(), model.getDescription()) &&
                Objects.equals(getImageUrl(), model.getImageUrl()) &&
                Objects.equals(getLocation(), model.getLocation());
    }

    @Exclude
    @Override
    public int hashCode() {

        return Objects.hash(getTitle(), getDescription(), getImageUrl(), getLocation());
    }

    @Exclude
    @NonNull
    @Override
    public String toString() {
        return "ImageModel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
