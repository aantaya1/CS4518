package com.aantaya.imagewars;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
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
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.tensorflow.lite.Interpreter;

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
    private ByteBuffer imgData;
    private String[] labelArr = new String[1001];
    private float[][] labelProbArray = new float[1][1001];
    private StringBuilder tfLabels = new StringBuilder();

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri mUri;

    private FirebaseVisionLabelDetector detector;
    private FirebaseVisionCloudLabelDetector cloudDetector;
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
        cloudDetector = FirebaseVision.getInstance().getVisionCloudLabelDetector();

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

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        int[] intValues = new int[299 * 299];

        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        for (int i = 0; i < 299; ++i) {
            for (int j = 0; j < 299; ++j) {
                final int val = intValues[pixel++];
                addPixelValue(val);
            }
        }
    }

    protected void addPixelValue(int pixelValue) {
        imgData.putFloat((((pixelValue >> 16) & 0xFF) - 128) / 128.0f);
        imgData.putFloat((((pixelValue >> 8) & 0xFF) - 128) / 128.0f);
        imgData.putFloat(((pixelValue & 0xFF) - 128) / 128.0f);
    }

     private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor fileDescriptor = getAssets().openFd("model/inception_v3.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return  fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    void LabelProbDet() {
        float curr;
        double threshold = 0.03;

        for (int i = 0; i < labelProbArray[0].length; i++) {
            curr = labelProbArray[0][i];
            if(curr > threshold) {
                tfLabels.append(labelArr[i]);
                tfLabels.append(",");
            }
        }

    }
    private void readLabels() throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("model/labels.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        labelList.toArray(labelArr);
    }

    public void uploadFile(){
        imgData = ByteBuffer.allocateDirect(1 * 299 * 299 * 3 * 4);
        imgData.order(ByteOrder.nativeOrder());
        Bitmap bitmap;
        Bitmap bm;


        if(mUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtention(mUri));

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mUri);
                firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
                bm = Bitmap.createScaledBitmap(bitmap, 299, 299, true);

                long beforeTensorFlow = System.currentTimeMillis();
                convertBitmapToByteBuffer(bm);
                MappedByteBuffer tfliteModel = loadModelFile();
                Interpreter tflite = new Interpreter(tfliteModel);
                tflite.run(imgData, labelProbArray);
                readLabels();
                LabelProbDet();
                long afterTensorFlow = System.currentTimeMillis();

                fileReference.putFile(mUri).continueWithTask(task -> {
                    if(!task.isSuccessful()) throw task.getException();
                    return fileReference.getDownloadUrl();
                }).addOnCompleteListener(taskA -> {
                    if (taskA.isSuccessful()){
                        long beforeOnDeviceGoogle = System.currentTimeMillis();
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
                                            // Note: This is where the other image labeling will likely happen
                                            long afterOnDeviceGoogle = System.currentTimeMillis();
                                            Task<List<FirebaseVisionCloudLabel>> cloudResult =
                                                    cloudDetector.detectInImage(firebaseVisionImage)
                                                            .addOnSuccessListener(cloudLabels -> {
                                                                Log.d("GOOGLE-CLOUD", cloudLabels.toString());
                                                                StringBuilder mCloudLabels = new StringBuilder();
                                                                for (FirebaseVisionCloudLabel l : cloudLabels) mCloudLabels.append(l.getLabel()).append(", ");
                                                                long afterOffDeviceGoogle = System.currentTimeMillis();
                                                                // then we do tensorflow

                                                                long onDeviceGoogleTime = afterOnDeviceGoogle - beforeOnDeviceGoogle;
                                                                long offDeviceGoogleTime = afterOffDeviceGoogle - afterOnDeviceGoogle;
                                                                // this vvv will be inside the tensorflow on success
                                                                //Do not create the model until we have finished all of the other labeling
                                                                // then you will pass all of the labels to this constructor so we can send it to firebase
                                                                ImageModel mImageModel = new ImageModel(title, desc, imageUrl, location, 0, mLables.toString(), mCloudLabels.toString(), tfLabels.toString());
                                                                mImageModel.setTimeOnDeviceTensorFlow(afterTensorFlow - beforeTensorFlow);
                                                                mImageModel.setTimeOffDeviceFirebase(offDeviceGoogleTime);
                                                                mImageModel.setTimeOnDeviceFirebase(onDeviceGoogleTime);
                                                                String id = mDatabaseRef.push().getKey();
                                                                mDatabaseRef.child(id).setValue(mImageModel);
                                                                Toast.makeText(AddImageActivity.this, "Upload Successful w/ All 3 ML", Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener( e -> {
                                                                Log.d(TAG, "Successful Upload URI: " + imageUrl);
                                                                long onDeviceGoogleTime = afterOnDeviceGoogle - beforeOnDeviceGoogle;

                                                                ImageModel mImageModel = new ImageModel(title, desc, imageUrl, location, 0, mLables.toString(), "N/A", tfLabels.toString());
                                                                mImageModel.setTimeOnDeviceTensorFlow(afterTensorFlow - beforeTensorFlow);
                                                                mImageModel.setTimeOffDeviceFirebase(-1);
                                                                mImageModel.setTimeOnDeviceFirebase(onDeviceGoogleTime);
                                                                String id = mDatabaseRef.push().getKey();
                                                                mDatabaseRef.child(id).setValue(mImageModel);
                                                                Toast.makeText(AddImageActivity.this, "Upload Successful w/ ML 1 & 2", Toast.LENGTH_SHORT).show();
                                                            });
                                        })
                                        .addOnFailureListener( e -> {
                                            String mLables = "N/A";
                                            String title = myEditNameView.getText().toString().trim();
                                            String desc = myEditDescView.getText().toString().trim();
                                            String location = mLocation;
                                            String imageUrl = taskA.getResult().toString();
                                            Log.d(TAG, "Successful Upload URI: " + imageUrl);

                                            ImageModel mImageModel = new ImageModel(title, desc, imageUrl, location, 0, mLables, "", tfLabels.toString());
                                            mImageModel.setTimeOnDeviceTensorFlow(afterTensorFlow - beforeTensorFlow);
                                            mImageModel.setTimeOffDeviceFirebase(-1);
                                            mImageModel.setTimeOnDeviceFirebase(-1);
                                            String id = mDatabaseRef.push().getKey();
                                            mDatabaseRef.child(id).setValue(mImageModel);
                                            Toast.makeText(AddImageActivity.this, "Upload Successful w/ ML 1", Toast.LENGTH_SHORT).show();
                                        });
                    }else {
                        Toast.makeText(this, "Upload failed: " + taskA.getException() ,
                                Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
