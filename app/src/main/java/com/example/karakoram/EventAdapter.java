package com.example.karakoram;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Activity context, ArrayList<Event> word) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, word);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View list=convertView;
        if(list==null)
        {
            list= LayoutInflater.from(getContext()).inflate(R.layout.event_listview,parent,false);
        }

        Event event=getItem(position);
        TextView eventname =(TextView)list.findViewById(R.id.event_name);
        eventname.setText(event.getEvent_name());

        TextView eventdetails =(TextView)list.findViewById(R.id.event_details);
        eventdetails.setText(event.getEvent_details());

        TextView eventtime =(TextView)list.findViewById(R.id.event_time);
        eventtime.setText(event.getEvent_time());

        return list;
    }
}
