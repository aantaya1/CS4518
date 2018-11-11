package com.example.owner.a1part1;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    ImageView viewImage;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewImage = findViewById(R.id.main_image);
        button = findViewById(R.id.main_button);

        AssetManager assetManager = getApplicationContext().getAssets();
        InputStream istr;

        Bitmap myImage1 = null;

        try {
            istr = assetManager.open("kitten_images/kitten_1.jpg");
            myImage1 = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        viewImage.setImageBitmap(myImage1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage();
            }
        });
    }

    void changeImage(){
        AssetManager assetManager = getApplicationContext().getAssets();
        InputStream istr;
        viewImage = findViewById(R.id.main_image);

        Random r = new Random();
        int i = r.nextInt(3) + 1;

        try {
            istr = assetManager.open("kitten_images/kitten_" + i + ".jpg");
            Bitmap myImage = BitmapFactory.decodeStream(istr);
            viewImage.setImageBitmap(myImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
