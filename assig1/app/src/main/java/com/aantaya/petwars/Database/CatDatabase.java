package com.aantaya.petwars.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onCreate (@NonNull SupportSQLiteDatabase db){
                    super.onCreate(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CatDao mDao;

        PopulateDbAsync(CatDatabase db) { mDao = db.catDao(); }

        @Override
        protected Void doInBackground(final Void... params) {
//            CatEntity cat = new CatEntity(1, 0, "Fluffy", "TestDescription");
//            Log.d("CATDATABASE", "Adding cat to db:" + "Fluffy");
//            mDao.insert(cat);
//
//            cat = new CatEntity(2, 0, "Snuffles", "AnotherTestDescription");
//            Log.d("CATDATABASE", "Adding cat to db:" + "Snuffles");
//            mDao.insert(cat);

            return null;
        }
    }
}
