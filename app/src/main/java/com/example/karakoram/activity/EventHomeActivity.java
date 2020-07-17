package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karakoram.R;
import com.example.karakoram.firebase.FirebaseQuery;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_home);
        Query query = FirebaseQuery.getAllEvents();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> eventList = new ArrayList<>();
                for( DataSnapshot snapshotItem : snapshot.getChildren()){
                    eventList.add(snapshotItem.child("title").getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventHomeActivity.this,R.layout.support_simple_spinner_dropdown_item, eventList);
                ListView listView = findViewById(R.id.events);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(EventHomeActivity.this,EventActivity.class);
                        intent.putExtra("title",((TextView)view).getText().toString());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error","Something went wrong");
            }
        });
    }

    public void onClickAddEvent(View view){
        Intent intent = new Intent(this,EventFormActivity.class);
        startActivity(intent);
    }
}