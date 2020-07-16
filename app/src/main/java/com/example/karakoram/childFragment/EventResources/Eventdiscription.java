package com.example.karakoram.childFragment.EventResources;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karakoram.R;

import java.util.ArrayList;
import java.util.List;

public class Eventdiscription extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdiscription);



        String event_name= getIntent().getExtras().getString("event_name");
        String event_title=getIntent().getExtras().getString("event_title");
        String event_time=getIntent().getExtras().getString("event_time");
        String event_details=getIntent().getExtras().getString("event_details");
        int event_image_res=getIntent().getExtras().getInt("event_image");

        TextView event_text_name= (TextView)findViewById(R.id.event_name);
        TextView event_text_title= findViewById(R.id.event_title);
        TextView event_text_time= findViewById(R.id.event_time);
        TextView event_text_details= findViewById(R.id.event_details);
        ImageView event_image=findViewById(R.id.event_image);

        event_text_name.setText(event_name);
        event_text_title.setText(event_title);
        event_text_details.setText(event_details);
        event_text_time.setText(event_time);
        //Log.d("event_image_res", "onCreate: "+event_image_res);
        if(event_image_res!=0){
        event_image.setImageResource(event_image_res);
            }

    }
}