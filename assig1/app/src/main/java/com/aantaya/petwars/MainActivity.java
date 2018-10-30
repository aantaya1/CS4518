package com.aantaya.petwars;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aantaya.petwars.Entities.CatEntity;
import com.aantaya.petwars.ViewModels.CatViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_CAT_ACTIVITY_REQUEST_CODE = 1;
    public static final String TAG = "MAIN_ACTIVITY";
    public int numRows;

    private CatViewModel catViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                Log.v(TAG, "Permission is revoked");
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
        }

        catViewModel = ViewModelProviders.of(this).get(CatViewModel.class);

        final CatListAdapter adapter = new CatListAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        catViewModel.getMyAllCats().observe(this, new Observer<List<CatEntity>>() {
            @Override
            public void onChanged(@Nullable final List<CatEntity> words) {
                adapter.setCats(words);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCatActivity.class);
                startActivityForResult(intent, NEW_CAT_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CAT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String imagePath = data.getStringExtra(AddCatActivity.EXTRA_IMAGE_PATH);
            String name = data.getStringExtra(AddCatActivity.EXTRA_NAME);
            String desc = data.getStringExtra(AddCatActivity.EXTRA_DESC);

            CatEntity cat = new CatEntity(imagePath, 0, name, desc);
            catViewModel.insert(cat);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}