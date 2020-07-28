package com.example.karakoram.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.karakoram.R;
import com.example.karakoram.resource.Event;

import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends ArrayAdapter<Event> {

    TextView mTitle;
    TextView mDescription;
    TextView mTime;
    TextView mDate;

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

        View view = list.findViewById(R.id.event_item);
//        view.setBackground(Drawable.createFromPath("@drawable/common_google_signin_btn_text_light_normal_background"));

        mTitle = list.findViewById(R.id.tv_event_title);
        mDescription = list.findViewById(R.id.tv_event_description);
        mTime = list.findViewById(R.id.tv_event_time_list);
        mDate = list.findViewById(R.id.tv_event_date_list);

        if (event != null) {
            String description = (String) event.getDescription().subSequence(0, Math.min(15, event.getDescription().length())) + "...";
            Date dateTime = event.getDateTime();
            String time = String.format("%02d", dateTime.getHours()) + " : " + String.format("%02d", dateTime.getMinutes());
            String date = (dateTime.getYear() + 1900) + "-" + String.format("%02d",dateTime.getMonth() + 1) + "-" + String.format("%02d",dateTime.getDate());

            mTitle.setText(event.getTitle());
            mDescription.setText(description);
            mTime.setText(time);
            mDate.setText(date);
        }

        return list;
    }

}
