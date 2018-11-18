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
import android.widget.LinearLayout;
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
    private static final String DATABASE_NAME = "uploads";

    //UI
    private View rootView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    //Firebase
    private static DatabaseReference mDb;
    private FirebaseRecyclerAdapter<ImageModel, MImageHolder> adapter;
    private FirebaseAuth mAuth;
    private String userId;

    private List<ImageModel> mImageModels;

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

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = rootView.findViewById(R.id.mRecyclerView);
        progressBar = rootView.findViewById(R.id.main_progress_bar);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mDb = FirebaseDatabase.getInstance().getReference();

        mImageModels = new ArrayList<>();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query q = mDb.child(DATABASE_NAME).orderByChild("voteCount");

        FirebaseRecyclerOptions<ImageModel> options =
                new FirebaseRecyclerOptions.Builder<ImageModel>()
                        .setQuery(q, ImageModel.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter <ImageModel, MImageHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MImageHolder holder, int position, @NonNull ImageModel model) {
                holder.title.setText(model.getTitle());
                holder.description.setText(model.getDescription());
                holder.location.setText(model.getLocation());
                holder.voteCount.setText("Number of Votes: " + String.valueOf(model.getVoteCount()));
                Picasso.with(getContext()).load(model.getImageUrl()).into(holder.image);
            }

            @NonNull
            @Override
            public MImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item,
                        viewGroup, false);
                progressBar.setVisibility(View.INVISIBLE);
                return new MImageHolder(v);
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class MImageHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        TextView description;
        TextView location;
        TextView voteCount;

        MImageHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.recyclerview_image);
            title = itemView.findViewById(R.id.recyclerview_title);
            description = itemView.findViewById(R.id.recyclerview_desc);
            location = itemView.findViewById(R.id.recyclerview_location);
            voteCount = itemView.findViewById(R.id.recyclerview_votes);
        }
    }
}
