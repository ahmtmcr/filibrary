package com.kalmac.filibrary.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kalmac.filibrary.R;


public class LibraryFragment extends Fragment {


    LinearLayout libraryLinear;
    View view;
    LayoutInflater rInflater;
    ViewGroup rContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rContainer = container;
        rInflater = inflater;
        view = rInflater.inflate(R.layout.fragment_library,  rContainer, false);


        libraryLinear = (LinearLayout) view.findViewById(R.id.libraryLinear);













        return view;
    }

}