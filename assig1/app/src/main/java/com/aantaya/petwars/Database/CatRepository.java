package com.aantaya.petwars.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.aantaya.petwars.Daos.CatDao;
import com.aantaya.petwars.Entities.CatEntity;

import java.util.List;
import java.util.Objects;

public class CatRepository {

    public static final String TAG = "CAT_REPOSITORY";

    private CatDao myCatDao;
    private LiveData<List<CatEntity>> myAllCats;

    public CatRepository(Application _app){
        CatDatabase db = CatDatabase.getDatabase(_app);
        myCatDao = db.catDao();
        myAllCats = myCatDao.getAllCats();
    }

    public LiveData<List<CatEntity>> getMyAllCats(){ return this.myAllCats; };

    public void insert (CatEntity _cat) {
        new insertAsyncTask(myCatDao).execute(_cat);
    }

    private static class insertAsyncTask extends AsyncTask<CatEntity, Void, Void> {

        private CatDao myAsyncTaskDao;

        insertAsyncTask(CatDao dao) { myAsyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final CatEntity... params) {
            myAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
