package com.example.karakoram.parentFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import com.example.karakoram.cache.HomeCache;
import com.example.karakoram.resource.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lombok.SneakyThrows;


public class HomeFragment extends Fragment {

    /* Variables */
    ArrayList<String> key = new ArrayList<>();
    ArrayList<Event> event = new ArrayList<>();
    Context context;

    /* Views */
    View view;
    RecyclerView listView;
    Drawable mdivider;

    /* Adapters */
    EventAdapter adapter;

    HomeCache cache;

    String x="str";

    public HomeFragment() {
    }

    HomeFragment(String x){
        this.x=x;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = container.getContext();
        cache = new HomeCache(context);
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
            event = cache.getValueArray();
            key = cache.getKeyArray();
            Log.i("HomeCacheLog", "events: " + event);
            Log.i("HomeCacheLog", "keys: " + key);

            if (event.isEmpty() || key.isEmpty()) {
                Log.i("HomeCacheLog", "lists were empty");
                refreshListView();
            }
            start();
            Log.i("HomeCacheLog", "try block");
        } catch (Exception e) {
            Log.i("HomeCacheLog", "some problem in getting cached content");
            refreshListView();
        }
    }

    private void setViews() {

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListView();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        view.findViewById(R.id.fab_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EventFormActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refreshListView() {
        event.clear();
        key.clear();


        if(x.equals("str")){
            FirebaseQuery.getAllEvents().addListenerForSingleValueEvent(new ValueEventListener() {
                @SneakyThrows
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                        event.add(snapshotItem.getValue(Event.class));
                        key.add(snapshotItem.getKey());
                    }
                    try {
                        cache.setKeyArray(key);
                        cache.setValueArray(event);
                    } catch (Exception ignored) {
                        Log.i("HomeCacheLog", "cache files are not getting updated");
                    }
                    start();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("firebase error", "Something went wrong");
                }
            });
        }

        else{
        FirebaseQuery.getUserEvents(x).addListenerForSingleValueEvent(new ValueEventListener() {
            @SneakyThrows
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    event.add(snapshotItem.getValue(Event.class));
                    key.add(snapshotItem.getKey());
                }
                try {
                    cache.setKeyArray(key);
                    cache.setValueArray(event);
                } catch (Exception ignored) {
                    Log.i("HomeCacheLog", "cache files are not getting updated");
                }
                start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });
    }
    }


    private void start() {

        Log.i("ASDF", String.valueOf(event));
        adapter = new EventAdapter(getActivity(), event,key);
        listView = view.findViewById(R.id.list_event);
        //listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mdivider= ContextCompat.getDrawable(view.getContext(),R.drawable.divider);
//        mdivider.setBounds(20,20,20,20);
        DividerItemDecoration itemdecor=new DividerItemDecoration(view.getContext(),LinearLayoutManager.VERTICAL);
        itemdecor.setDrawable(mdivider);
        listView.addItemDecoration(itemdecor);
        listView.setAdapter(adapter);
    }

}