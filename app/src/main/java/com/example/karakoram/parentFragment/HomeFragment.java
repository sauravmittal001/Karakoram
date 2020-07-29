package com.example.karakoram.parentFragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.activity.EventFormActivity;
import com.example.karakoram.adapter.EventAdapter;
import com.example.karakoram.resource.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    /* Variables */
    ArrayList<String> key = new ArrayList<>();
    ArrayList<Event> event = new ArrayList<>();

    /* Views */
    View view;
    RecyclerView listView;
    FloatingActionButton fab;
    Drawable mdivider;

    /* Adapters */
    EventAdapter adapter;



    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseQuery.getAllEvents().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    event.add(snapshotItem.getValue(Event.class));
                    key.add(snapshotItem.getKey());
                }
                start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });

        // Add event button
        fab = view.findViewById(R.id.fab_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "add event", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), EventFormActivity.class);
                startActivity(intent);

            }
        });
    }

    private void start() {
        Log.i("ASDF", String.valueOf(event));
        adapter = new EventAdapter(getActivity(), event,key);
        listView = view.findViewById(R.id.list_event);
        //listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mdivider= ContextCompat.getDrawable(view.getContext(),R.drawable.divider);
        //mdivider.setBounds(getParentFragment().getPaddingLeft(),0,16,0);
        DividerItemDecoration itemdecor=new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL);
        itemdecor.setDrawable(mdivider);
        listView.addItemDecoration(itemdecor);
        listView.setAdapter(adapter);
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Date dateTime = event.get(i).getDateTime();
                String time = String.format("%02d", dateTime.getHours()) + " : " + String.format("%02d", dateTime.getMinutes());
                String date = (dateTime.getYear() + 1900) + "-" + String.format("%02d",dateTime.getMonth() + 1) + "-" + String.format("%02d",dateTime.getDate());
                Intent intent = new Intent(getActivity().getApplicationContext(), EventDescription.class);
                intent.putExtra("title", event.get(i).getTitle());
                intent.putExtra("description", event.get(i).getDescription());
                intent.putExtra("time", time);
                intent.putExtra("date", date);
                intent.putExtra("key", key.get(i));
                startActivity(intent);
            }
        }); */
    }

}