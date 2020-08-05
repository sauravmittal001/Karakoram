package com.example.karakoram.childFragment.mess;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import com.example.karakoram.cache.mess.UstCache;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.MessFeedback;
import com.example.karakoram.resource.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class messUSTFragment extends Fragment {


    /* Variables */
    private ArrayList<Pair<String, MessFeedback>> feedbacksKv = new ArrayList<>();
    private Context context;
    private UstCache cache;
    private boolean getMine;

    /* Views */
    private View view;
    private RecyclerView listView;
    Drawable mdivider;


    private USTadapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    public messUSTFragment(boolean getMine){

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
        cache = new UstCache(context,getMine);
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViews();

        try {
            ArrayList<MessFeedback> feedbacks = cache.getFeedbackArray();
            ArrayList<String> key = cache.getKeyArray();
            feedbacksKv.clear();
            for(int i = 0; i<feedbacks.size();i++)
                feedbacksKv.add(Pair.create(key.get(i),feedbacks.get(i)));
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","loggedOut");
        if(!userId.equals("loggedOut")) {
            Query query;
            if(getMine)
                query = FirebaseQuery.getUserMessFeedback(userId);
            else
                query = FirebaseQuery.getAllMessFeedback();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    feedbacksKv.clear();
                    for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                        if (!Objects.requireNonNull(snapshotItem.getValue(MessFeedback.class)).getDescription().equals("")) {
                            feedbacksKv.add(Pair.create(snapshotItem.getKey(), snapshotItem.getValue(MessFeedback.class)));
                        }
                    }
                    try {
                        ArrayList<MessFeedback> feedbacks = new ArrayList<>();
                        ArrayList<String> key = new ArrayList<>();
                        for (int i = 0; i < feedbacksKv.size(); i++) {
                            key.add(feedbacksKv.get(i).first);
                            feedbacks.add(feedbacksKv.get(i).second);
                        }
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
        else {
            feedbacksKv.clear();
            try {
                ArrayList<MessFeedback> feedbacks = new ArrayList<>();
                ArrayList<String> key = new ArrayList<>();
                for (int i = 0; i < feedbacksKv.size(); i++) {
                    key.add(feedbacksKv.get(i).first);
                    feedbacks.add(feedbacksKv.get(i).second);
                }
                cache.setKeyArray(key);
                cache.setFeedbackArray(feedbacks);
            } catch (Exception ignored) {
                Log.i("UstCache", "cache files are not getting updated");
            }
            start();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void start() {
        Collections.sort(feedbacksKv, new Comparator<Pair<String, MessFeedback>>() {
            @Override
            public int compare(Pair<String, MessFeedback> stringMessFeedbackPair, Pair<String, MessFeedback> t1) {
                return t1.second.getTimestamp().compareTo(stringMessFeedbackPair.second.getTimestamp());
            }
        });
        ArrayList<MessFeedback> feedbacks = new ArrayList<>();
        ArrayList<String> key = new ArrayList<>();
        for(int i=0; i<feedbacksKv.size();i++){
            key.add(feedbacksKv.get(i).first);
            feedbacks.add(feedbacksKv.get(i).second);
        }
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