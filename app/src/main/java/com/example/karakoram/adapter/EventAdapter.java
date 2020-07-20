package com.example.karakoram.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.karakoram.R;
import com.example.karakoram.resources.Event;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<Event> {

    TextView mTitle;
    TextView mDescription;
    TextView mTime;

    public EventAdapter(Activity context, ArrayList<Event> word) {
        /* Here, we initialize the ArrayAdapter's internal storage for the context and the list.
         * the second argument is used when the ArrayAdapter is populating a single TextView.
         * Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
         * going to use this second argument, so it can be any value. Here, we used 0.
         */
        super(context, 0, word);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View list = convertView;
        if (list == null) {
            list = LayoutInflater.from(getContext()).inflate(R.layout.event_listview, parent, false);
        }

        Event event = getItem(position);

        mTitle = list.findViewById(R.id.tv_event_title);
        mDescription = list.findViewById(R.id.tv_event_description);
        mTime = list.findViewById(R.id.tv_event_time);

        if (event != null) {
            String description = (String) event.getDescription().subSequence(0, Math.min(15, event.getDescription().length())) + "...";
            String time = String.format("%02d", event.getDateTime().getHours()) + " : " + String.format("%02d", event.getDateTime().getMinutes());

            mTitle.setText(event.getTitle());
            mDescription.setText(description);
            mTime.setText(time);
        }

        return list;
    }

}
