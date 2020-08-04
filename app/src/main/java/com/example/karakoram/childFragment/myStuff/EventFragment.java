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
import com.example.karakoram.adapter.EventAdapter;
import com.example.karakoram.cache.HomeCache;
import com.example.karakoram.cache.myStuff.EventCache;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    private ArrayList<String> key = new ArrayList<>();
    private ArrayList<Event> event;
    Context context;

    /* Views */
    private View view;
    private RecyclerView listView;

    /* Adapters */
    private EventAdapter adapter;

    EventCache cache;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_home, container, false);
        context = container.getContext();
        cache = new EventCache(context);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViews();

        try {
            event = cache.getValueArray();
            key = cache.getKeyArray();
            Log.i("MyStuffEvent", "events: " + event);
            Log.i("MyStuffEvent", "keys: " + key);

            if (event.isEmpty() || key.isEmpty()) {
                Log.i("MyStuffEvent", "lists were empty");
                refreshListView();
            }
            start();
            Log.i("MyStuffEvent", "try block");
        } catch (Exception e) {
            Log.i("MyStuffEvent", "some problem in getting cached content");
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

        view.findViewById(R.id.fab_event).setVisibility(View.GONE);
    }

    private void refreshListView() {
        if (event != null) {
            event.clear();
        }
        if (key != null) {
            key.clear();
        }

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        Query query;
        String userId = sharedPreferences.getString("userId","loggedOut");
        query = FirebaseQuery.getUserEvents(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                event = new ArrayList<>();
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
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });
    }

    private void start() {
        Log.i("ASDF", String.valueOf(event));
        adapter = new EventAdapter(getActivity(), event,key);
        listView = view.findViewById(R.id.list_event);
        //listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Drawable mdivider = ContextCompat.getDrawable(view.getContext(), R.drawable.divider);
        //mdivider.setBounds(getParentFragment().getPaddingLeft(),0,16,0);
        DividerItemDecoration itemdecor=new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL);
        itemdecor.setDrawable(mdivider);
        listView.addItemDecoration(itemdecor);
        listView.setAdapter(adapter);
    }
}