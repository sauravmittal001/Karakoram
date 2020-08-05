package com.example.karakoram.childFragment.myStuff;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.adapter.USTadapter;
import com.example.karakoram.cache.myStuff.UstCache;
import com.example.karakoram.resource.MessFeedback;
import com.example.karakoram.resource.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class mystuffUST extends Fragment {


    /* Variables */
    private ArrayList<String> key = new ArrayList<>();
    private ArrayList<MessFeedback>feedbacks;
    Context context;
    UstCache cache;
    SwipeRefreshLayout swipeRefreshLayout;

    /* Views */
    private View view;
    private RecyclerView listView;
    Drawable mdivider;


    private USTadapter adapter;



    public mystuffUST(){

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
       context = container.getContext();
       cache = new UstCache(context);
       return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setViews();

        try {
            feedbacks = cache.getFeedbackArray();
            key = cache.getKeyArray();
            Log.i("UstCache", "hostelBills: " + feedbacks);
            Log.i("UstCache", "keys: " + key);

            if (feedbacks.isEmpty() || key.isEmpty()) {
                Log.i("UstCache", "lists were empty");
                refreshListView();
            }
            start();
            Log.i("UstCache", "try block");
        } catch (Exception e) {
            Log.i("UstCache", "some problem in getting cached content");
            refreshListView();
        }


    }


    private void setViews() {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListView();
                setViews();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void refreshListView() {
        if (feedbacks != null) {
            feedbacks.clear();
        }
        if (key != null) {
            key.clear();
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        String userid=sharedPreferences.getString("userId","logedOut");

        FirebaseQuery.getUserMessFeedback(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedbacks = new ArrayList<>();
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    feedbacks.add(snapshotItem.getValue(MessFeedback.class));
                    key.add(snapshotItem.getKey());
                }
                try {
                    cache.setKeyArray(key);
                    cache.setFeedbackArray(feedbacks);
                } catch (Exception ignored) {
                    Log.i("UstCache", "cache files are not getting updated");
                }
                start();
                swipeRefreshLayout.setRefreshing(false);
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
        //mdivider.setBounds(getParentFragment().getPaddingLeft(),0,16,0);
        DividerItemDecoration itemdecor=new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL);
        itemdecor.setDrawable(mdivider);
        listView.addItemDecoration(itemdecor);
        listView.setAdapter(adapter);

    }
}