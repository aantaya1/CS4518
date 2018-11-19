package com.aantaya.imagewars.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Objects;

@IgnoreExtraProperties
public class ImageModel {
    private String uid;
    private String title;
    private String description;
    private String imageUrl;
    private String location;
    private long voteCount;
    private String lables;

    public ImageModel (){}

    public ImageModel (String mTitle, String mDescription, String mImageUrl, String mLocation, long mVoteCount, String mLables){
        if(mLocation == null) mLocation = "N/A";
        this.title = mTitle;
        this.description = mDescription;
        this.imageUrl = mImageUrl;
        this.location = mLocation;
        this.voteCount = mVoteCount;
        this.lables = mLables;
    }

    //This constructor is only used for updating the votes in Firebase
    public ImageModel (ImageModel m, long mVoteCount){
        if(m.getLocation() == null) m.setLocation("N/A");
        this.title = m.getTitle();
        this.description = m.getDescription();
        this.imageUrl = m.getImageUrl();
        this.location = m.getLocation();
        this.lables = m.getLables();
        this.voteCount = mVoteCount;
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

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLables() {
        return lables;
    }

    public void setLables(String lables) {
        this.lables = lables;
    }

    @Exclude
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageModel)) return false;
        ImageModel model = (ImageModel) o;
        return getVoteCount() == model.getVoteCount() &&
                Objects.equals(getUid(), model.getUid()) &&
                Objects.equals(getTitle(), model.getTitle()) &&
                Objects.equals(getDescription(), model.getDescription()) &&
                Objects.equals(getImageUrl(), model.getImageUrl()) &&
                Objects.equals(getLocation(), model.getLocation()) &&
                Objects.equals(getLables(), model.getLables());
    }

    @Exclude
    @Override
    public int hashCode() {

        return Objects.hash(getUid(), getTitle(), getDescription(), getImageUrl(), getLocation(), getVoteCount(), getLables());
    }

    @Exclude
    @Override
    public String toString() {
        return "ImageModel{" +
                "uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", location='" + location + '\'' +
                ", voteCount=" + voteCount +
                ", lables='" + lables + '\'' +
                '}';
    }
}
