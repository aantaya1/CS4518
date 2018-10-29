package com.aantaya.petwars.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cat_table")
public class CatEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int _id;

    @NonNull
    @ColumnInfo(name = "imageId")
    private long imageId;

    @NonNull
    @ColumnInfo(name = "votes")
    private long votes;

    //Constructor
    public CatEntity(long _imageId){
        this.imageId = _imageId;
        this.votes = 0;
    }

    //Getters for accessing values in the model
    @NonNull
    public long getImageId() { return imageId; }

    @NonNull
    public long getVotes() { return votes;}
}