package com.aantaya.imagewars;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aantaya.imagewars.Models.ImageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CardViewActivity extends AppCompatActivity {
    public static final String TAG = "VIEW_CARD_ACTIVITY";
    private DatabaseReference mDatabase;
    private ImageView imageViewCard;
    private TextView titleView;
    private TextView voteView;
    private TextView descView;
    private TextView locView;
    private TextView offFireView;
    private TextView onFireView;
    private TextView onTFView;
    private TextView offFireLabels;
    private TextView onFireLabels;
    private TextView onTFLabels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setHooks();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");

        Query query = mDatabase.getRoot().child("uploads").orderByChild("imageUrl").equalTo(url);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ImageModel upload = dataSnapshot.getValue(ImageModel.class);
                    upload.setUid(dataSnapshot.getKey());
                    titleView.setText(upload.getTitle());
                    Picasso.with(getApplicationContext()).load(upload.getImageUrl()).fit().centerCrop().into(imageViewCard);
                    descView.setText(upload.getDescription());
                    voteView.setText("Vote Count: " + upload.getVoteCount());
                    locView.setText("Location: " + upload.getLocation());
                    offFireView.setText("Time Off-Device Firebase: " + upload.getTimeOffDeviceFirebase() + "ms");
                    onFireView.setText("Time On-Device Firebase: " + upload.getTimeOnDeviceFirebase() + "ms");
                    onTFView.setText("Time On-Device TensorFlow: " + upload.getTimeOnDeviceTensorFlow() + "ms");
                    offFireLabels.setText("Off-Device Firebase Labels: " + upload.getLablesOffDeviceFirebase());
                    onFireLabels.setText("On-Device Firebase Labels: " + upload.getLablesOnDeviceFirebase());
                    onTFLabels.setText("On-Device TensorFlow Labels: " + upload.getTimeOnDeviceTensorFlow());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read app title value.", databaseError.toException());
            }
        });
    }

    private void setHooks(){
        imageViewCard = (ImageView) findViewById(R.id.imageViewCard);
        titleView = (TextView) findViewById(R.id.titleView);
        voteView = (TextView) findViewById(R.id.voteView);
        descView =(TextView) findViewById(R.id.descView);
        locView = (TextView) findViewById(R.id.locView);
        offFireLabels = (TextView) findViewById(R.id.offFireLabels);
        onFireLabels = (TextView) findViewById(R.id.onFireLabels);
        onTFLabels = (TextView) findViewById(R.id.onTFLabels);
        offFireView = (TextView) findViewById(R.id.offFireView);
        onFireView = (TextView) findViewById(R.id.onFireView);
        onTFView = (TextView) findViewById(R.id.onFireView);
    }

}
