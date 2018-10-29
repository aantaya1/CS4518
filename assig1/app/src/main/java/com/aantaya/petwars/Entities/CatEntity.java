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
    public CatEntity(@NonNull long imageId, @NonNull long votes, @NonNull String name, @NonNull String description) {
        this.imageId = imageId;
        this.votes = votes;
        this.name = name;
        this.description = description;
    }

    //Getters for accessing values in the model

    @NonNull
    public int get_id() { return _id; }
    //Required to have a setter for each field, however, doesn't make sense for _id so I made it do nothing
    public void set_id(int id) { id = id; }

    @NonNull
    public long getImageId() { return imageId; }
    public void setImageId(@NonNull long imageId) { this.imageId = imageId; }

    @NonNull
    public long getVotes() { return votes; }
    public void setVotes(@NonNull long votes) { this.votes = votes; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    @NonNull
    public String getDescription() { return description; }
    public void setDescription(@NonNull String description) { this.description = description; }
}