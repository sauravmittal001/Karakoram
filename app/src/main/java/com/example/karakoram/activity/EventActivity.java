package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karakoram.R;
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.resource.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = this.getIntent();
        final String key = intent.getStringExtra("key");
        Log.d("124hello",key);
        Query query = FirebaseQuery.getEvent(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Event event = snapshot.getValue(Event.class);
                assert event != null;
                ((TextView)findViewById(R.id.description)).setText(event.getDescription());
                ((TextView)findViewById(R.id.title)).setText(event.getTitle());
                String date = (event.getDateTime().getYear() + 1900) + "-" + String.format("%02d",event.getDateTime().getMonth() + 1) + "-" + String.format("%02d",event.getDateTime().getDate());
                String time = String.format("%02d",event.getDateTime().getHours()) + " : " + String.format("%02d",event.getDateTime().getMinutes());
                ((TextView)findViewById(R.id.date_output)).setText(date);
                ((TextView)findViewById(R.id.time_output)).setText(time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error","Something went wrong");
            }
        });
        final ImageView eventImage = findViewById(R.id.event_image);
        StorageReference ref = FirebaseStorage.getInstance().getReference("eventImages/"+key+".png");
        long MAXBYTES = 1024*1024;
        ref.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                eventImage.setImageBitmap(bitmap);
            }
        });
    }
}