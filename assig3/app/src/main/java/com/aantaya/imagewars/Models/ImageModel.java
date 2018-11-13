package com.aantaya.imagewars.Models;

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
}
