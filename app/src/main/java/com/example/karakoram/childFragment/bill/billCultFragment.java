package com.example.karakoram.childFragment.bill;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.karakoram.R;
import com.example.karakoram.childFragment.bill.fullscreenimage.fullimageActivity;
import com.example.karakoram.childFragment.bill.imageadapter.imageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;


public class billCultFragment extends Fragment {


    ArrayList<Integer> bill= new ArrayList<>(Arrays.asList( R.drawable.download_1,R.drawable.download,R.drawable.images,R.drawable.images_1,R.drawable.images_2,R.drawable.images_3,R.drawable.download_2,R.drawable.download_3,
            R.drawable.download_4,R.drawable.download_5,R.drawable.download_6));


    private ListView listView;
    View view;
    FloatingActionButton fab;

    public billCultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initialize gridview object
        listView=view.findViewById(R.id.cultbilllistView);
        //set adapter on gridview object
        listView.setAdapter(new imageAdapter(bill,getActivity()));

        //when click on image it open full screen iamge
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent( getActivity().getApplicationContext(), fullimageActivity.class);
                // passing array index
                i.putExtra("bill", bill);
                i.putExtra("currentbill",position);
                startActivity(i);
            }
        });


        fab=view.findViewById(R.id.FABCult_bill);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);

            }
        });
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_bill_cult, container, false);
        return view;
    }
}