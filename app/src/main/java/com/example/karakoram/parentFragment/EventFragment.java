package com.example.karakoram.parentFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.activity.EventFormActivity;
import com.example.karakoram.adapter.EventAdapter;
import com.example.karakoram.cache.EventCache;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.UserType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lombok.SneakyThrows;

import static android.content.Context.MODE_PRIVATE;


public class EventFragment extends Fragment {

    /* Variables */
    private ArrayList<Pair<String, Event>> eventsKv = new ArrayList<>();
    private Context context;
    private boolean getMine;
    private SharedPreferences sharedPreferences;

    /* Views */
    private View view;
    private RecyclerView listView;
    private Drawable mdivider;

    /* Adapters */
    private EventAdapter adapter;

    private EventCache cache;
    private SwipeRefreshLayout swipeRefreshLayout;


    public EventFragment(boolean getMine) {
        this.getMine = getMine;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_event, container, false);
        context = container.getContext();
        cache = new EventCache(context,getMine);
        return view;
    }

    @CallSuper
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @SneakyThrows
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViews();
        try {
            ArrayList<Event> events = cache.getValueArray();
            ArrayList<String> key = cache.getKeyArray();
            eventsKv.clear();
            for(int i = 0; i<events.size();i++)
                eventsKv.add(Pair.create(key.get(i),events.get(i)));

            if (events.isEmpty() || key.isEmpty()) {
                refreshListView();
            }
            start();
        } catch (Exception e) {
            Log.i("HomeCacheLog", "some problem in getting cached content " + e);
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

        View addEventButton = view.findViewById(R.id.fab_event);
        sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS,MODE_PRIVATE);
        String userId  = sharedPreferences.getString("userId","loggedOut");
        UserType userType = UserType.valueOf(sharedPreferences.getString("type","Student"));

        if(userId.equals("loggedOut") || userType.equals(UserType.Student))
            addEventButton.setVisibility(View.INVISIBLE);
        else
            addEventButton.setVisibility(View.VISIBLE);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EventFormActivity.class);
                startActivity(intent);
            }
        });

    }

    private void refreshListView() {
        String userId = sharedPreferences.getString("userId","loggedOut");
        if(!userId.equals("loggedOut")) {
            Query query;
            if(getMine)
                query = FirebaseQuery.getUserEvents(userId);
            else
                query= FirebaseQuery.getAllEvents();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @SneakyThrows
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    eventsKv.clear();
                    for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                        Event event = snapshotItem.getValue(Event.class);
                        eventsKv.add(Pair.create(snapshotItem.getKey(), event));
                    }
                    try {
                        ArrayList<Event> events = new ArrayList<>();
                        ArrayList<String> key = new ArrayList<>();
                        for (int i = 0; i < eventsKv.size(); i++) {
                            key.add(eventsKv.get(i).first);
                            events.add(eventsKv.get(i).second);
                        }
                        cache.setKeyArray(key);
                        cache.setValueArray(events);
                    } catch (Exception ignored) {
                        Log.i("HomeCacheLog", "cache files are not getting updated");
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
            eventsKv.clear();
            try {
                ArrayList<Event> events = new ArrayList<>();
                ArrayList<String> key = new ArrayList<>();
                for (int i = 0; i < eventsKv.size(); i++) {
                    key.add(eventsKv.get(i).first);
                    events.add(eventsKv.get(i).second);
                }
                cache.setKeyArray(key);
                cache.setValueArray(events);
            } catch (Exception ignored) {
                Log.i("HomeCacheLog", "cache files are not getting updated");
            }
            start();
            swipeRefreshLayout.setRefreshing(false);
        }
    }





    private void start() {
        Collections.sort(eventsKv, new Comparator<Pair<String, Event>>() {
            @Override
            public int compare(Pair<String, Event> stringEventPair, Pair<String, Event> t1) {
                return t1.second.getDateTime().compareTo(stringEventPair.second.getDateTime());
            }
        });
        ArrayList<Event> events = new ArrayList<>();
        ArrayList<String> key = new ArrayList<>();
        for(int i=0; i<eventsKv.size();i++){
            key.add(eventsKv.get(i).first);
            events.add(eventsKv.get(i).second);
        }
        adapter = new EventAdapter(getActivity(), events,key);
        listView = view.findViewById(R.id.list_event);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mdivider= ContextCompat.getDrawable(view.getContext(),R.drawable.divider);
        DividerItemDecoration itemdecor=new DividerItemDecoration(view.getContext(),LinearLayoutManager.VERTICAL);
        itemdecor.setDrawable(mdivider);
        listView.addItemDecoration(itemdecor);
        listView.setAdapter(adapter);
    }

}