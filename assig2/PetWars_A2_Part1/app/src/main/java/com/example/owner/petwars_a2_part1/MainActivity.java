package com.example.owner.petwars_a2_part1;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import static java.io.File.createTempFile;

public class MainActivity extends AppCompatActivity {

    private static final int TAKE_PIC = 1;

    ImageView viewImage;
    TextView viewImagePath;
    Button button;

    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewImage = findViewById(R.id.main_image);
        viewImagePath = findViewById(R.id.main_image_path);
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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, 0);

                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                dir.mkdir();
                String path = dir.toString();
                long count = dir.listFiles().length + 1;
                String fileName = "cat" + count + ".jpg";

                File mFile = new File(path, fileName);
                imagePath = mFile.getAbsolutePath();
                Uri mUri = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.owner.petwars_a2_part1.fileprovider", mFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(intent, TAKE_PIC);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TAKE_PIC){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            bitmap = rotateImage(bitmap, 90);
            viewImage.setImageBitmap(bitmap);
            viewImagePath.setText(imagePath);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}