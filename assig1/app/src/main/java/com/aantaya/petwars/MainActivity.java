package com.aantaya.petwars;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
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

import com.aantaya.petwars.Daos.CatDao;
import com.aantaya.petwars.Entities.CatEntity;
import com.aantaya.petwars.ViewModels.CatViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_CAT_ACTIVITY_REQUEST_CODE = 1;
    public static final String TAG = "MAIN_ACTIVITY";
    public int numRows;

    private CatViewModel catViewModel;

    final String PREFS_NAME = "MyPrefsFile";

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                Log.v(TAG, "Permission is revoked");
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
        }

        //This will only execute in the first time the application runs...This will create two cats
        //for TA grading...
        if (settings.getBoolean("my_first_time", true) != false) {
            //the app is being launched for first time, do something
            Log.d(TAG, "First time");

            String imagePath1;
            String imagePath2;
            AssetManager assetManager = getApplicationContext().getAssets();

            InputStream istr;
            Bitmap bitmap1 = null;
            Bitmap bitmap2 = null;

            //Load in the images from assets
            try {
                istr = assetManager.open("kitten_pictures/kitten_1.jpg");
                bitmap1 = BitmapFactory.decodeStream(istr);
                istr = assetManager.open("kitten_pictures/kitten_2.jpg");
                bitmap2 = BitmapFactory.decodeStream(istr);
            } catch (IOException e) {
                e.printStackTrace();
            }

            File dir = new File(getApplicationContext().getFilesDir()+File.separator+"images");
            dir.mkdir();
            String path = dir.toString();
            long count = dir.listFiles().length+1;
            String fileName1 = "cat" + count + ".jpg";
            count++;
            String fileName2 = "cat" + count + ".jpg";
            Log.v(TAG, "Saving Image = " + fileName1);
            Log.v(TAG, "Saving Image = " + fileName2);
            Log.v(TAG, "Location = " + path);
            OutputStream fOut = null;
            File file1 = new File(path, fileName1); // the File to save, append increasing numeric counter to prevent files from getting overwritten.
            File file2 = new File(path, fileName2);
            try {
                fOut = new FileOutputStream(file1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (bitmap1 != null && bitmap2 != null) {
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                try {
                    fOut = new FileOutputStream(file2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            }

            try {
                assert fOut != null;
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
            } catch (IOException e) {
                e.printStackTrace();
            }

            imagePath1 = file1.getAbsolutePath();
            imagePath2 = file2.getAbsolutePath();

            try {
                MediaStore.Images.Media.insertImage(getContentResolver(), file1.getAbsolutePath(), file1.getName(), file1.getName());
                MediaStore.Images.Media.insertImage(getContentResolver(), file2.getAbsolutePath(), file2.getName(), file2.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            CatEntity cat1 = new CatEntity(imagePath1, 0, "Fluffy", "The cutest cat ever!");
            CatEntity cat2 = new CatEntity(imagePath2, 0, "Snuffles", "A really cute cat :) !");

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).apply();
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