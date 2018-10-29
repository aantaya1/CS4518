package com.aantaya.petwars.Daos;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.aantaya.petwars.Entities.CatEntity;

import java.util.List;

public interface CatDao {

    @Insert
    void insert(CatEntity cat);

    @Query("UPDATE cat_table SET votes= votes + 1 WHERE _id =:id")
    void incrementVotes(int id);

    @Query("SELECT * FROM cat_table ORDER BY votes DESC")
    List<CatEntity> getAllCats();
}
