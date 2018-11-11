package com.aantaya.petwars.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cat_table")
public class CatEntity {

    public static final String TAG = "CAT_ENTITY";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int _id;

    @NonNull
    @ColumnInfo(name = "imagePath")
    private String imagePath;

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
    public CatEntity(@NonNull String imagePath, @NonNull long votes, @NonNull String name, @NonNull String description) {
        this.imagePath = imagePath;
        this.votes = votes;
        this.name = name;
        this.description = description;
    }

    //Getters and setters for manipulating the model

    @NonNull
    public int get_id() { return _id; }
    //Required to have a setter for each field, however, doesn't make sense for _id so I made it do nothing
    public void set_id(int id) { id = id; }

    @NonNull
    public String getImagePath() { return imagePath; }
    public void setImagePath(@NonNull String imagePath) { this.imagePath = imagePath; }

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