package com.example.karakoram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karakoram.R;
import com.example.karakoram.firebase.FirebaseQuery;
import com.example.karakoram.resource.Event;

import java.util.Date;

public class EventFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
    }

    public void onClickUpdateEvent(View view){
        Event event = new Event();
        String[] date = ((EditText)findViewById(R.id.date_input)).getText().toString().split("-");
        String[] time = ((EditText)findViewById(R.id.time_input)).getText().toString().split(":");
        event.setDateTime(new Date(Integer.parseInt(date[0])-1900,Integer.parseInt(date[1])-1,Integer.parseInt(date[2]),Integer.parseInt(time[0]),Integer.parseInt(time[1])));
        event.setTitle(((EditText)findViewById(R.id.title_input)).getText().toString());
        event.setDescription(((EditText)findViewById(R.id.description_input)).getText().toString());
        FirebaseQuery.addEvent(event);
    }
}