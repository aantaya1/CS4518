package com.example.owner.petwars2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class AddCatActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_PATH = "image_path";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_DESC = "desc";
    public static final String TAG = "ADD_CAT_ACTIVITY";

    public static final int PICK_IMAGE = 1;
    public static final int TAKE_IMAGE = 2;

    private EditText myEditNameView;
    private EditText myEditDescView;
    private ImageView myImageCatView;
    private Button upload;
    private Button takePicture;
    private Button saveButton;

    private String imagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cat_activity);

        myEditNameView = findViewById(R.id.edit_cat_name);
        myEditDescView = findViewById(R.id.edit_cat_desc);
        myImageCatView = findViewById(R.id.cat_image);

        takePicture = findViewById(R.id.capture_image);
        takePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, 0);

            if (!activities.isEmpty()){
                if(isWriteStoragePermissionGranted()){
                    File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    dir.mkdir();
                    String path = dir.toString();
                    long count = dir.listFiles().length+1;
                    String fileName = "cat" + count + ".jpg";

                    File mFile = new File(path, fileName);
                    imagePath = mFile.getAbsolutePath();
                    Uri mUri = FileProvider.getUriForFile(getApplicationContext(),
                            "com.example.owner.petwars2.fileprovider", mFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                    startActivityForResult(intent, TAKE_IMAGE);
                }
            }else {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
            }
        });

        upload = findViewById(R.id.upload_image);
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_IMAGE){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            myImageCatView.setImageBitmap(bitmap);
        }

        if (requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                myImageCatView.setImageBitmap(bitmap);

                if (isWriteStoragePermissionGranted()) {
                    // Assume block needs to be inside a Try/Catch block.
                    File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    dir.mkdir();
                    String path = dir.toString();
                    long count = dir.listFiles().length+1;
                    String fileName = "cat" + count + ".jpg";

                    File file = new File(path, fileName);
                    imagePath = file.getAbsolutePath(); // the File to save, append increasing numeric counter to prevent files from getting overwritten.
                    OutputStream fOut = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream

                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                Log.v(TAG, "Permission is revoked");
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }
}
