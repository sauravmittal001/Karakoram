package com.example.karakoram;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.karakoram.R;


public class FullscreenImageFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fullscreen_image, container, false);
        ImageView imageView= v.findViewById(R.id.full_screen_image);
        int imagesrc=getArguments().getInt("bill");
        imageView.setImageResource(imagesrc);
        return v;
    }
}