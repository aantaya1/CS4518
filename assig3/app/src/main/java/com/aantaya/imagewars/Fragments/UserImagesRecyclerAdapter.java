package com.aantaya.imagewars.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aantaya.imagewars.Models.ImageModel;
import com.aantaya.imagewars.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserImagesRecyclerAdapter extends RecyclerView.Adapter<UserImagesRecyclerAdapter.ImageHolder> {
    private static final String TAG = "USER_IMAGES_RECYCLER";

    private Context mContext;
    private List<ImageModel> mModels;

    public UserImagesRecyclerAdapter(@NonNull Context context, List<ImageModel> models) {
        mContext = context;
        mModels = models;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item,
                viewGroup, false);
        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder imageHolder, int i) {
        ImageModel model = mModels.get(i);
        Log.d(TAG, "***Title: " + model.getTitle());
        imageHolder.title.setText(model.getTitle());
        imageHolder.description.setText(model.getDescription());
        imageHolder.lables.setText(model.getLables());
        //Picasso.with(mContext).load(model.getImageUrl()).into(imageHolder.image);
        Picasso.with(mContext).load(model.getImageUrl()).fit().centerInside().into(imageHolder.image);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public static class ImageHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        TextView description;
        TextView lables;

        ImageHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.recyclerview_image);
            title = itemView.findViewById(R.id.recyclerview_title);
            description = itemView.findViewById(R.id.recyclerview_desc);
            lables = itemView.findViewById(R.id.recyclerview_lables);
        }
    }
}
