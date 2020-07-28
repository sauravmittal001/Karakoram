package com.example.karakoram.childFragment.myStuff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.activity.BillActivity;
import com.example.karakoram.activity.BillFormActivity;
import com.example.karakoram.activity.EventActivity;
import com.example.karakoram.adapter.EventAdapter;
import com.example.karakoram.adapter.HostelBillAdapter;
import com.example.karakoram.childFragment.events.EventDescription;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.HostelBill;
import com.example.karakoram.resource.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    private ArrayList<String> key = new ArrayList<>();
    private ArrayList<Event> event;

    /* Views */
    private View view;
    private ListView listView;

    /* Adapters */
    private EventAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_bill_child, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });
    }


    private void start() {
        Log.i("ASDF", String.valueOf(event));
        adapter = new EventAdapter(getActivity(), event);
        listView = view.findViewById(R.id.billlistView);
        listView.setAdapter(adapter);
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
        });
    }
}