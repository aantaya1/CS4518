package com.aantaya.imagewars.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;

@IgnoreExtraProperties
public class ImageModel {
    private String uid;
    private String title;
    private String description;
    private String imageUrl;
    private String location;
    private long voteCount;
    private String lablesOnDeviceFirebase;
    private String lablesOffDeviceFirebase;
    private String lablesOnDeviceTensorFlow;
    private double timeOnDeviceFirebase;
    private double timeOffDeviceFirebase;
    private double timeOnDeviceTensorFlow;

    public ImageModel (){}

    public ImageModel (String mTitle, String mDescription, String mImageUrl, String mLocation, long mVoteCount,
                       String mOnDeviceFirebaseLables, String mOffDeviceFirebaseLabels, String mOnDeviceTensorFlow){
        if(mLocation == null) mLocation = "N/A";
        this.title = mTitle;
        this.description = mDescription;
        this.imageUrl = mImageUrl;
        this.location = mLocation;
        this.voteCount = mVoteCount;
        this.lablesOnDeviceFirebase = mOnDeviceFirebaseLables;
        this.lablesOffDeviceFirebase = mOffDeviceFirebaseLabels;
        this.lablesOnDeviceTensorFlow = mOnDeviceTensorFlow;
    }

    public ImageModel (String mTitle, String mDescription, String mImageUrl, String mLocation, long mVoteCount,
                       String mOnDeviceFirebaseLables, String mOffDeviceFirebaseLabels, String mOnDeviceTensorFlow,
                       double mTimeOnDeviceFirebase, double mTimeOffDeviceFirebase, double mTimeOnDeviceTensorFlow){
        if(mLocation == null) mLocation = "N/A";
        this.title = mTitle;
        this.description = mDescription;
        this.imageUrl = mImageUrl;
        this.location = mLocation;
        this.voteCount = mVoteCount;
        this.lablesOnDeviceFirebase = mOnDeviceFirebaseLables;
        this.lablesOffDeviceFirebase = mOffDeviceFirebaseLabels;
        this.lablesOnDeviceTensorFlow = mOnDeviceTensorFlow;
        this.timeOnDeviceFirebase = mTimeOnDeviceFirebase;
        this.timeOffDeviceFirebase = mTimeOffDeviceFirebase;
        this.timeOnDeviceTensorFlow = mTimeOnDeviceTensorFlow;
    }

    //This constructor is only used for updating the votes in Firebase
    public ImageModel (ImageModel m, long mVoteCount){
        if(m.getLocation() == null) m.setLocation("N/A");
        this.title = m.getTitle();
        this.description = m.getDescription();
        this.imageUrl = m.getImageUrl();
        this.location = m.getLocation();
        this.lablesOnDeviceFirebase = m.getLablesOnDeviceFirebase();
        this.lablesOffDeviceFirebase = m.getLablesOffDeviceFirebase();
        this.lablesOnDeviceTensorFlow = m.getLablesOnDeviceTensorFlow();
        this.voteCount = mVoteCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getLablesOnDeviceFirebase() {
        return lablesOnDeviceFirebase;
    }

    public void setLablesOnDeviceFirebase(String lablesOnDeviceFirebase) {
        this.lablesOnDeviceFirebase = lablesOnDeviceFirebase;
    }

    public String getLablesOffDeviceFirebase() {
        return lablesOffDeviceFirebase;
    }

    public void setLablesOffDeviceFirebase(String lablesOffDeviceFirebase) {
        this.lablesOffDeviceFirebase = lablesOffDeviceFirebase;
    }

    public String getLablesOnDeviceTensorFlow() {
        return lablesOnDeviceTensorFlow;
    }

    public void setLablesOnDeviceTensorFlow(String lablesOnDeviceTensorFlow) {
        this.lablesOnDeviceTensorFlow = lablesOnDeviceTensorFlow;
    }

    public double getTimeOnDeviceFirebase() {
        return timeOnDeviceFirebase;
    }

    public void setTimeOnDeviceFirebase(double timeOnDeviceFirebase) {
        this.timeOnDeviceFirebase = timeOnDeviceFirebase;
    }

    public double getTimeOffDeviceFirebase() {
        return timeOffDeviceFirebase;
    }

    public void setTimeOffDeviceFirebase(double timeOffDeviceFirebase) {
        this.timeOffDeviceFirebase = timeOffDeviceFirebase;
    }

    public double getTimeOnDeviceTensorFlow() {
        return timeOnDeviceTensorFlow;
    }

    public void setTimeOnDeviceTensorFlow(double timeOnDeviceTensorFlow) {
        this.timeOnDeviceTensorFlow = timeOnDeviceTensorFlow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageModel)) return false;
        ImageModel that = (ImageModel) o;
        return getVoteCount() == that.getVoteCount() &&
                Double.compare(that.getTimeOnDeviceFirebase(), getTimeOnDeviceFirebase()) == 0 &&
                Double.compare(that.getTimeOffDeviceFirebase(), getTimeOffDeviceFirebase()) == 0 &&
                Double.compare(that.getTimeOnDeviceTensorFlow(), getTimeOnDeviceTensorFlow()) == 0 &&
                Objects.equals(getUid(), that.getUid()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getImageUrl(), that.getImageUrl()) &&
                Objects.equals(getLocation(), that.getLocation()) &&
                Objects.equals(getLablesOnDeviceFirebase(), that.getLablesOnDeviceFirebase()) &&
                Objects.equals(getLablesOffDeviceFirebase(), that.getLablesOffDeviceFirebase()) &&
                Objects.equals(getLablesOnDeviceTensorFlow(), that.getLablesOnDeviceTensorFlow());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getUid(), getTitle(), getDescription(), getImageUrl(), getLocation(), getVoteCount(), getLablesOnDeviceFirebase(), getLablesOffDeviceFirebase(), getLablesOnDeviceTensorFlow(), getTimeOnDeviceFirebase(), getTimeOffDeviceFirebase(), getTimeOnDeviceTensorFlow());
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", location='" + location + '\'' +
                ", voteCount=" + voteCount +
                ", lablesOnDeviceFirebase='" + lablesOnDeviceFirebase + '\'' +
                ", lablesOffDeviceFirebase='" + lablesOffDeviceFirebase + '\'' +
                ", lablesOnDeviceTensorFlow='" + lablesOnDeviceTensorFlow + '\'' +
                ", timeOnDeviceFirebase=" + timeOnDeviceFirebase +
                ", timeOffDeviceFirebase=" + timeOffDeviceFirebase +
                ", timeOnDeviceTensorFlow=" + timeOnDeviceTensorFlow +
                '}';
    }
}
