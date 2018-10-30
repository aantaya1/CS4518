package com.aantaya.petwars.Database;

import android.Manifest;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.aantaya.petwars.Daos.CatDao;
import com.aantaya.petwars.Entities.CatEntity;

@Database(entities = {CatEntity.class}, version = 1)
public abstract class CatDatabase extends RoomDatabase{

    private static volatile CatDatabase INSTANCE;

    public abstract CatDao catDao();

    //Use singleton pattern so we don't accidentally create multiple databases
    static CatDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CatDatabase.class) {
                if (INSTANCE == null) {
                    // Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CatDatabase.class, "cat_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
