package com.example.karakoram.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.karakoram.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDialog extends DialogFragment {


    public MyDialog() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mydialog, container, false);
    }
}