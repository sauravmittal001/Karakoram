package com.example.karakoram.parentFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.activity.EventFormActivity;
import com.example.karakoram.adapter.EventAdapter;
import com.example.karakoram.cache.HomeCache;
import com.example.karakoram.childFragment.EventResources.EventDescription;
import com.example.karakoram.resource.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    ListView listView;

    /* Adapters */
    EventAdapter adapter;

    HomeCache cache;

    public HomeFragment() {
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
            Log.i("HomeCacheLog", "try block");
            event = cache.getValueArray();
            key = cache.getKeyArray();
            start();
        } catch (Exception e) {
            Log.i("HomeCacheLog", "some problem in getting cached content");
            refreshListView();
        }
    }

    private void setViews() {

        view.findViewById(R.id.fab_event_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshListView();
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
        FirebaseQuery.getAllEvents().addListenerForSingleValueEvent(new ValueEventListener() {
            @SneakyThrows
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    event.add(snapshotItem.getValue(Event.class));
                    key.add(snapshotItem.getKey());
                }
                cache.setKeyArray(key);
                cache.setValueArray(event);
                start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });
    }

    private void start() {
        adapter = new EventAdapter(getActivity(), event);
        listView = view.findViewById(R.id.list_event);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String time = String.format("%02d", event.get(i).getDateTime().getHours()) + " : " + String.format("%02d", event.get(i).getDateTime().getMinutes());
                Intent intent = new Intent(getActivity().getApplicationContext(), EventDescription.class);
                intent.putExtra("title", event.get(i).getTitle());
                intent.putExtra("description", event.get(i).getDescription());
                intent.putExtra("time", time);
                intent.putExtra("key", key.get(i));
                startActivity(intent);
            }
        });
    }


}