package com.aantaya.petwars.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aantaya.petwars.Database.CatRepository;
import com.aantaya.petwars.Entities.CatEntity;

import java.util.List;

public class CatViewModel extends AndroidViewModel {

    public static final String TAG = "CAT_VIEW_MODEL";

    private CatRepository myCatRepository;
    private LiveData<List<CatEntity>> myAllCats;

    public CatViewModel(@NonNull Application _app) {
        super(_app);
        myCatRepository = new CatRepository(_app);
        myAllCats = myCatRepository.getMyAllCats();
    }

    public LiveData<List<CatEntity>> getMyAllCats() { return this.myAllCats; }
    public void insert(CatEntity _cat) { myCatRepository.insert(_cat); }
}
