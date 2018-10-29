package com.aantaya.petwars;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aantaya.petwars.Entities.CatEntity;

import java.util.List;

public class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.CatViewHolder>{

    class CatViewHolder extends RecyclerView.ViewHolder {
        private final TextView CatItemView;

        private CatViewHolder(View itemView) {
            super(itemView);
            CatItemView = itemView.findViewById(R.id.textView);
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
            holder.CatItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.CatItemView.setText("No Cat");
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
