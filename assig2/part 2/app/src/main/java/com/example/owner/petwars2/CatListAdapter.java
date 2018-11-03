package com.example.owner.petwars2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petwars2.Entities.CatEntity;

import java.util.List;

public class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.CatViewHolder>{

    public static final String TAG = "CAT_LIST_ADAPTER";

    class CatViewHolder extends RecyclerView.ViewHolder {
        private final TextView CatRankView;
        private final TextView CatTitleView;
        private final ImageView CatImageView;
        private final TextView CatDescriptionView;

        private CatViewHolder(View itemView) {
            super(itemView);
            CatRankView = itemView.findViewById(R.id.recyclerview_rank);
            CatTitleView = itemView.findViewById(R.id.recyclerview_title);
            CatImageView = itemView.findViewById(R.id.recyclerview_image);
            CatDescriptionView = itemView.findViewById(R.id.recyclerview_desc);
        }
    }

    private final LayoutInflater mInflater;
    private List<CatEntity> mCats; // Cached copy of Cats

    CatListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        if (mCats != null) {
            CatEntity current = mCats.get(position);
            holder.CatRankView.setText(String.valueOf(position+1));
            holder.CatTitleView.setText(current.getName());
            holder.CatDescriptionView.setText(current.getDescription());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap b = BitmapFactory.decodeFile(current.getImagePath(), options);

            holder.CatImageView.setImageBitmap(b);
        } else {
            // Covers the case of data not being ready yet.
            holder.CatTitleView.setText("No Cat");
        }
    }

    void setCats(List<CatEntity> Cats){
        mCats = Cats;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mCats has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mCats != null)
            return mCats.size();
        else return 0;
    }

}
