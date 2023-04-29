package com.kalmac.filibrary.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalmac.filibrary.R;


public class SearchFragment extends Fragment {


    LayoutInflater rInflater;
    ViewGroup rContainer;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rContainer = container;
        rInflater = inflater;
        view = rInflater.inflate(R.layout.fragment_home,  rContainer, false);

        return view;
    }
}