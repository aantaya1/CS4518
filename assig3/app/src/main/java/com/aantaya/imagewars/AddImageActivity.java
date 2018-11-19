package com.aantaya.imagewars;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aantaya.imagewars.Models.ImageModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddImageActivity extends AppCompatActivity {

    public static final String TAG = "ADD_CAT_ACTIVITY";
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1789;

    public static final int PICK_IMAGE = 1;
    public static final int TAKE_IMAGE = 2;

    private EditText myEditNameView;
    private EditText myEditDescView;
    private ImageView myImageCatView;
    private ProgressBar myProgressBar;
    private Button upload;
    private Button takePicture;
    private Button saveButton;

    private String imagePath;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri mUri;

    private FirebaseVisionLabelDetector detector;
    private FirebaseVisionImage firebaseVisionImage;

    //Location will be stored as a lat & long separated by a comma
    private String mLocation;
    private FusedLocationProviderClient fusedLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        fusedLocation = LocationServices.getFusedLocationProviderClient(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        detector = FirebaseVision.getInstance().getVisionLabelDetector();

        getLocation();

        myEditNameView = findViewById(R.id.edit_image_name);
        myEditDescView = findViewById(R.id.edit_image_desc);
        myImageCatView = findViewById(R.id.add_image);
        //TODO: Add progress bar to layout and then bind view here
        //myProgressBar = findViewById(R.id.upload_progress_bar);

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
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
        });

        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(myEditNameView.getText()) || TextUtils.isEmpty(myEditDescView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                uploadFile();
            }
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            mUri = data.getData();
            Picasso.with(this).load(mUri).into(myImageCatView);
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            mUri = data.getData();
            Picasso.with(this).load(mUri).into(myImageCatView);
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

    public String getFileExtention(Uri mUri){
        ContentResolver mContent = getContentResolver();
        MimeTypeMap mMime = MimeTypeMap.getSingleton();
        return mMime.getExtensionFromMimeType(mContent.getType(mUri));
    }

    public void uploadFile(){
        if(mUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtention(mUri));

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mUri);
                firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileReference.putFile(mUri).continueWithTask(task -> {
                if(!task.isSuccessful()) throw task.getException();
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(taskA -> {
                if (taskA.isSuccessful()){
                    Task<List<FirebaseVisionLabel>> result =
                            detector.detectInImage(firebaseVisionImage)
                                    .addOnSuccessListener( labels -> {
                                                StringBuilder mLables = new StringBuilder();
                                                for (FirebaseVisionLabel l : labels) mLables.append(l.getLabel()).append(", ");

                                                String title = myEditNameView.getText().toString().trim();
                                                String desc = myEditDescView.getText().toString().trim();
                                                String location = mLocation;
                                                String imageUrl = taskA.getResult().toString();
                                                Log.d(TAG, "Successful Upload URI: " + imageUrl);

                                                ImageModel mImageModel = new ImageModel(title, desc, imageUrl, location, 0, mLables.toString());
                                                String id = mDatabaseRef.push().getKey();
                                                mDatabaseRef.child(id).setValue(mImageModel);
                                                Toast.makeText(AddImageActivity.this, "Upload Successful w/ ML", Toast.LENGTH_SHORT).show();
                                            })
                                    .addOnFailureListener( e -> {
                                                String mLables = "N/A";
                                                String title = myEditNameView.getText().toString().trim();
                                                String desc = myEditDescView.getText().toString().trim();
                                                String location = mLocation;
                                                String imageUrl = taskA.getResult().toString();
                                                Log.d(TAG, "Successful Upload URI: " + imageUrl);

                                                ImageModel mImageModel = new ImageModel(title, desc, imageUrl, location, 0, mLables);
                                                String id = mDatabaseRef.push().getKey();
                                                mDatabaseRef.child(id).setValue(mImageModel);
                                                Toast.makeText(AddImageActivity.this, "Upload Successful w/o ML", Toast.LENGTH_SHORT).show();
                                            });
                }else {
                    Toast.makeText(this, "Upload failed: " + taskA.getException() ,
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
