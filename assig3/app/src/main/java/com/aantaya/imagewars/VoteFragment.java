package com.aantaya.imagewars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VoteFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vote, container, false);
        return rootView;
    }
}
