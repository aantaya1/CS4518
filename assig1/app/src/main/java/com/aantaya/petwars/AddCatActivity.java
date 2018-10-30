package com.aantaya.petwars;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class AddCatActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_PATH = "image_path";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_DESC = "desc";
    public static final String TAG = "ADD_CAT_ACTIVITY";

    public static final int PICK_IMAGE = 1;

    private EditText myEditNameView;
    private EditText myEditDescView;
    private ImageView myImageCatView;

    private String imagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cat_activity);

        myEditNameView = findViewById(R.id.edit_cat_name);
        myEditDescView = findViewById(R.id.edit_cat_desc);
        myImageCatView = findViewById(R.id.cat_image);

        final Button upload = findViewById(R.id.upload_image);
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(myEditNameView.getText()) || TextUtils.isEmpty(myEditDescView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String name = myEditNameView.getText().toString();
                    String desc = myEditDescView.getText().toString();

                    replyIntent.putExtra(EXTRA_IMAGE_PATH, imagePath);
                    replyIntent.putExtra(EXTRA_NAME, name);
                    replyIntent.putExtra(EXTRA_DESC, desc);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                myImageCatView.setImageBitmap(bitmap);

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.v(TAG,"Permission is granted");
                        // Assume block needs to be inside a Try/Catch block.
                        String path = getApplicationContext().getFilesDir().toString();
                        OutputStream fOut = null;
                        Integer counter = 0;
                        File file = new File(path, "cat"+counter+".jpg"); // the File to save, append increasing numeric counter to prevent files from getting overwritten.
                        fOut = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                        fOut.flush(); // Not really required
                        fOut.close(); // do not forget to close the stream

                        imagePath = file.getAbsolutePath();
                        MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                    } else {
                        Log.v(TAG,"Permission is revoked");
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
                else { //permission is automatically granted on sdk<23 upon installation
                    Log.v(TAG,"Permission is granted");
                    // Assume block needs to be inside a Try/Catch block.
                    String path = getApplicationContext().getFilesDir().toString();
                    OutputStream fOut = null;
                    Integer counter = 0;
                    File file = new File(path, "cat"+counter+".jpg"); // the File to save, append increasing numeric counter to prevent files from getting overwritten.
                    fOut = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream

                    imagePath = file.getAbsolutePath();
                    MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                }

            }catch (IOException e){
                e.printStackTrace();
            }
            Log.v(TAG, "*****USER PICKED AN IMAGE!!!*****");
        }
    }
}