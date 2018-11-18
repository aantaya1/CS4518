package com.aantaya.imagewars.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aantaya.imagewars.Models.ImageModel;
import com.aantaya.imagewars.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VoteFragment extends Fragment {

    //Constants
    private static final String TAG = "VOTE_FRAGMENT";
    private static final String DATABASE_NAME = "uploads";

    //UI
    private View rootView;
    private Button upVoteButton;
    private Button downVoteButton;
    private ImageView image;
    private TextView titleText;
    private TextView descriptionText;

    //Firebase
    private static DatabaseReference mDb;
    private FirebaseAuth mAuth;
    private String userId;

    //Random Variables
    private List<ImageModel> mImageModels;
    private int currImage;

    public VoteFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static VoteFragment newInstance() {
        VoteFragment fragment = new VoteFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vote, container, false);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mDb = FirebaseDatabase.getInstance().getReference();

        upVoteButton = rootView.findViewById(R.id.vote_positive_button);
        downVoteButton = rootView.findViewById(R.id.vote_negative_button);
        image = rootView.findViewById(R.id.vote_image);
        titleText = rootView.findViewById(R.id.vote_image_title);
        descriptionText = rootView.findViewById(R.id.vote_image_description);

        mImageModels = new ArrayList<>();
        currImage = 0;

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        ValueEventListener mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.v(TAG, "DataSnapshot: " + dataSnapshot.toString());
                    for (DataSnapshot postSnapshot : dataSnapshot.child("uploads").getChildren()) {
                        ImageModel upload = postSnapshot.getValue(ImageModel.class);
                        upload.setUid(postSnapshot.getKey());
                        Log.v(TAG, "Image UUID: " + postSnapshot.getKey());
                        mImageModels.add(upload);
                    }
                    if(mImageModels.size() >= 1){
                        titleText.setText(mImageModels.get(currImage).getTitle());
                        descriptionText.setText(mImageModels.get(currImage).getDescription());
                        Picasso.with(getContext()).load(mImageModels.get(currImage).getImageUrl()).fit().centerCrop().into(image);
                    }
                }else {
                    //TODO: Display text saying there are no images to find
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        mDb.addListenerForSingleValueEvent(mValueEventListener);

        upVoteButton.setOnClickListener(view -> {
            String id = mImageModels.get(currImage).getUid();
            long numVotes = mImageModels.get(currImage).getVoteCount() + 1;
            ImageModel model = new ImageModel(mImageModels.get(currImage), numVotes);
            mImageModels.get(currImage).setVoteCount(numVotes);
            mDb.child(DATABASE_NAME).child(id).setValue(model);
            nextImage();
        });

        downVoteButton.setOnClickListener(view -> {
            String id = mImageModels.get(currImage).getUid();
            long numVotes = mImageModels.get(currImage).getVoteCount() - 1;
            ImageModel model = new ImageModel(mImageModels.get(currImage), numVotes);
            mImageModels.get(currImage).setVoteCount(numVotes);
            mDb.child(DATABASE_NAME).child(id).setValue(model);
            nextImage();
        });
    }

    public void nextImage(){
        currImage++;
        if (currImage >= mImageModels.size()) currImage = 0;
        titleText.setText(mImageModels.get(currImage).getTitle());
        descriptionText.setText(mImageModels.get(currImage).getDescription());
        Picasso.with(getContext()).load(mImageModels.get(currImage).getImageUrl()).fit().centerCrop().into(image);
    }
}
