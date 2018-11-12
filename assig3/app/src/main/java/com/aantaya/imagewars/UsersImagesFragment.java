package com.aantaya.imagewars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UsersImagesFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    public UsersImagesFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UsersImagesFragment newInstance() {
        UsersImagesFragment fragment = new UsersImagesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_images, container, false);
        return rootView;
    }

}
