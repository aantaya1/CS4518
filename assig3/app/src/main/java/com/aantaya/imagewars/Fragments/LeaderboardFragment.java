package com.aantaya.imagewars.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aantaya.imagewars.Models.ImageModel;
import com.aantaya.imagewars.R;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.google.firebase.database.DatabaseReference;

public class LeaderboardFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    public LeaderboardFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LeaderboardFragment newInstance() {
        LeaderboardFragment fragment = new LeaderboardFragment();



        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_images, container, false);
        return rootView;
    }
}
