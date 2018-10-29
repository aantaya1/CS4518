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

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    //Default Constructor
    public CatEntity(String _name, long _imageId){
        this.name = _name;
        this.imageId = _imageId;
        this.votes = 0;
    }

    public CatEntity(String _name, String _desc, long _imageId){
        this.name = _name;
        this.description = _desc;
        this.imageId = _imageId;
        this.votes = 0;
    }

    //Getters for accessing values in the model
    @NonNull
    public long getImageId() { return this.imageId; }

    @NonNull
    public long getVotes() { return this.votes; }

    @NonNull
    public String getName() { return this.name; }

    @NonNull
    public String getDescription() { return this.description; }
}