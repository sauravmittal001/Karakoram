package com.example.karakoram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Eventdiscription extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdiscription);



        String event_name= getIntent().getExtras().getString("event_name");
        String event_details=getIntent().getExtras().getString("event_details");
        String event_time=getIntent().getExtras().getString("event_time");

        TextView event_text_name= (TextView)findViewById(R.id.dialog_event_name);
        TextView event_text_details= findViewById(R.id.dialog_event_details);
        TextView event_text_time= findViewById(R.id.dialog_event_time);

        event_text_name.setText(event_name);
        event_text_details.setText(event_details);
        event_text_time.setText(event_time);

    }
}