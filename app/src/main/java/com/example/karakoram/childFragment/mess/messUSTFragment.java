package com.example.karakoram.childFragment.mess;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.adapter.USTadapter;
import com.example.karakoram.resource.MessFeedback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class messUSTFragment extends Fragment {


    /* Variables */
    private ArrayList<String> key = new ArrayList<>();
    private ArrayList<MessFeedback>feedbacks;


    /* Views */
    private View view;
    private RecyclerView listView;
    Drawable mdivider;


    private USTadapter adapter;



    public messUSTFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_mess_u_s_t, container, false);
       return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


            FirebaseQuery.getAllMessFeedback().addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    feedbacks=new ArrayList<>();
                    for(DataSnapshot snapshotItem:snapshot.getChildren()){
                        feedbacks.add(snapshotItem.getValue(MessFeedback.class));
                        key.add(snapshotItem.getKey());
                    }
                    start();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("firebase error", "Something went wrong");
                }
            });
        }

    private void start() {
        Log.i("ASDF", String.valueOf(feedbacks));
        adapter = new USTadapter(getActivity(), feedbacks,key);
        listView = view.findViewById(R.id.bill_listView);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mdivider= ContextCompat.getDrawable(view.getContext(),R.drawable.divider);
        DividerItemDecoration itemdecor=new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL);
        itemdecor.setDrawable(mdivider);
        listView.addItemDecoration(itemdecor);
        listView.setAdapter(adapter);

    }
}