package com.aantaya.imagewars.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aantaya.imagewars.Models.ImageModel;
import com.aantaya.imagewars.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UsersImagesFragment extends Fragment {

    //Constants
    private static final String TAG = "USER_IMAGES_FRAGMENT";

    //UI
    private View rootView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    //Firebase
    private static DatabaseReference mDb;
    //private FirebaseRecyclerAdapter<ImageModel, MImageHolder> adapter;
    private FirebaseAuth mAuth;
    private String userId;

    private List<ImageModel> mImageModels;
    private UserImagesRecyclerAdapter mAdapter;

    public UsersImagesFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UsersImagesFragment newInstance() {
        return new UsersImagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(TAG, "#####INSIDE onCreateView()#####");

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mDb = FirebaseDatabase.getInstance().getReference().child("uploads").child(userId);

        recyclerView = rootView.findViewById(R.id.mRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = rootView.findViewById(R.id.main_progress_bar);

        if (recyclerView != null ) Log.d(TAG, "RecyclerView = " + recyclerView.toString());
        else Log.d(TAG, "RecyclerView = NULL");

        mImageModels = new ArrayList<>();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.v(TAG,"#####INSIDE onStart()#####");

        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v(TAG, "#####INSIDE onDataChange()#####");
                Log.v(TAG, "DataSnapShot--> " + dataSnapshot.toString());

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.v(TAG, "#####FOUND DataSnapshot#####...  " + postSnapshot.toString());
                    ImageModel upload = postSnapshot.getValue(ImageModel.class);
                    mImageModels.add(upload);
                }

                mAdapter = new UserImagesRecyclerAdapter(getActivity(), mImageModels);

                recyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v(TAG, "#####INSIDE onCancelled()#####");
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

//        FirebaseRecyclerOptions<ImageModel> options =
//                new FirebaseRecyclerOptions.Builder<ImageModel>()
//                        .setQuery(mDb, ImageModel.class)
//                        .build();
//
//        adapter = new FirebaseRecyclerAdapter <ImageModel, MImageHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull MImageHolder holder, int position, @NonNull ImageModel model) {
//                holder.rank.setText(position);
//                Log.d(TAG, "***Title: " + model.getTitle());
//                holder.title.setText(model.getTitle());
//                holder.description.setText(model.getDescription());
//                Picasso.with(getContext()).load(model.getImageUrl()).into(holder.image);
//            }
//
//            @NonNull
//            @Override
//            public MImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item,
//                        viewGroup, false);
//                Log.d(TAG, "***onCreateViewHolder");
//                return new MImageHolder(v);
//            }
//        };
//
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

//    public static class MImageHolder extends RecyclerView.ViewHolder{
//        TextView rank;
//        ImageView image;
//        TextView title;
//        TextView description;
//
//        MImageHolder(@NonNull View itemView) {
//            super(itemView);
//
//            rank = itemView.findViewById(R.id.recyclerview_rank);
//            image = itemView.findViewById(R.id.recyclerview_image);
//            title = itemView.findViewById(R.id.recyclerview_title);
//            description = itemView.findViewById(R.id.recyclerview_desc);
//        }
//    }
}
