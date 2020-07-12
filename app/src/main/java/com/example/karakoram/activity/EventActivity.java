package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.karakoram.R;
import com.example.karakoram.firebase.FirebaseQuery;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = this.getIntent();
        final String title = intent.getStringExtra("title");
        Query query = FirebaseQuery.getEvent(title);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String description = snapshot.getChildren().iterator().next().child("description").getValue(String.class);
                ((TextView)findViewById(R.id.description)).setText(description);
                ((TextView)findViewById(R.id.title)).setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}