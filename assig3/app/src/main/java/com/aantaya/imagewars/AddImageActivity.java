package com.aantaya.imagewars;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

public class AddImageActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_PATH = "image_path";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_DESC = "desc";
    public static final String EXTRA_LOCATION = "location";
    public static final String TAG = "ADD_CAT_ACTIVITY";
    private static final int CAMERA_REQUEST = 1888;

    public static final int PICK_IMAGE = 1;
    public static final int TAKE_IMAGE = 2;

    private EditText myEditNameView;
    private EditText myEditDescView;
    private ImageView myImageCatView;
    private Button upload;
    private Button takePicture;
    private Button saveButton;

    private String imagePath;

    //Location will be stored as a lat & long separated by a comma
    private String mLocation;
    private FusedLocationProviderClient fusedLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        fusedLocation = LocationServices.getFusedLocationProviderClient(this);

        getLocation();

        myEditNameView = findViewById(R.id.edit_image_name);
        myEditDescView = findViewById(R.id.edit_image_desc);
        myImageCatView = findViewById(R.id.add_image);

        takePicture = findViewById(R.id.capture_image);
        takePicture.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, 0);

            if (!activities.isEmpty()) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        upload = findViewById(R.id.upload_image);
        upload.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(myEditNameView.getText()) || TextUtils.isEmpty(myEditDescView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String name = myEditNameView.getText().toString();
                String desc = myEditDescView.getText().toString();

                replyIntent.putExtra(EXTRA_IMAGE_PATH, imagePath);
                replyIntent.putExtra(EXTRA_NAME, name);
                replyIntent.putExtra(EXTRA_DESC, desc);
                if (mLocation != null) replyIntent.putExtra(EXTRA_LOCATION, mLocation);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_IMAGE) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            myImageCatView.setImageBitmap(bitmap);
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            myImageCatView.setImageBitmap(bitmap);
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MainActivity.LOCATION_PERMISSION);
            Log.v(TAG, "*****Needed to ask permission again*****");
        } else {
            fusedLocation.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    mLocation = String.format(Locale.US, "%s, %s",
                            location.getLatitude(), location.getLongitude());
                    Log.v(TAG, "Found a location: " + mLocation);
                }
            });
        }
    }

}
