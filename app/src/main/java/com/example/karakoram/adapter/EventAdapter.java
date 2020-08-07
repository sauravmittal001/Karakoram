package com.example.karakoram.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karakoram.R;
import com.example.karakoram.resource.Event;
import com.example.karakoram.activity.EventDescription;

import java.util.ArrayList;
import java.util.Date;


 public class EventAdapter extends RecyclerView.Adapter<EventAdapter.myViewHolder> {

    private Context mcontext;
    private ArrayList<Event> event1;
    private ArrayList<String> key;

    public EventAdapter(Context mcontext, ArrayList<Event> event1,ArrayList<String> key) {
       this.mcontext=mcontext;
       this.event1=event1;
       this.key=key;

    }

     @NonNull
     @Override
     public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v=LayoutInflater.from(mcontext).inflate(R.layout.event_listview,parent,false);
        final myViewHolder vHolder=new myViewHolder(v);

        vHolder.event_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=vHolder.getAdapterPosition();
                Date dateTime = event1.get(i).getDateTime();
                String time = showTime(dateTime.getHours(),dateTime.getMinutes());
                String date = (dateTime.getYear() + 1900) + " " + monthName(dateTime.getMonth() + 1)+ " " + String.format("%02d",dateTime.getDate());
                Intent intent = new Intent(mcontext, EventDescription.class);
                intent.putExtra("title", event1.get(i).getTitle());
                intent.putExtra("description", event1.get(i).getDescription());
                intent.putExtra("isImageAttached",event1.get(i).isImageAttached());
                intent.putExtra("time", time);
                intent.putExtra("date", date);
                intent.putExtra("key", key.get(i));
                intent.putExtra("dateTime",event1.get(i).getDateTime().toString());
                intent.putExtra("userId",event1.get(i).getUserId());
                mcontext.startActivity(intent);
            }
        });
         return vHolder;
     }

     @Override
     public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
         Event event = getItem(position);
         if (event != null) {
             Date dateTime = event.getDateTime();
             String time = showTime(dateTime.getHours(),dateTime.getMinutes());
             String date = (dateTime.getYear() + 1900) + "-" + String.format("%02d",dateTime.getMonth() + 1) + "-" + String.format("%02d",dateTime.getDate());
             String description;
             if(event.getDescription().length()>15)
                 description = event.getDescription().substring(0,15) + "...";
             else
                 description = event.getDescription();
             holder.mTitle.setText(event.getTitle());
             holder.mDescription.setText(description);
             holder.mTime.setText(time);
             holder.mDate.setText(date);
         }

     }

     @Override
     public int getItemCount() {
         return event1.size();
     }


     public static class myViewHolder extends RecyclerView.ViewHolder{

         private TextView mTitle;
         private TextView mTime;
         private TextView mDate;
         private TextView mDescription;
         private LinearLayout event_list_view;


         public myViewHolder(@NonNull View itemView) {
             super(itemView);
             mTitle = itemView.findViewById(R.id.tv_event_title);
             mDescription = itemView.findViewById(R.id.tv_event_description);
             mTime = itemView.findViewById(R.id.tv_event_time_list);
             mDate = itemView.findViewById(R.id.tv_event_date_list);
             event_list_view=(LinearLayout)itemView.findViewById(R.id.event_item_id);

         }
     }

     public Event getItem(int position)
     {
         return event1.get(position);
     }

     public String monthName (int monthNumber) {
         String[] monthOfYear = mcontext.getResources().getStringArray(R.array.months);
         return monthOfYear[monthNumber-1];
     }

     public String showTime(int hour, int min) {
         String format;
         if (hour == 0) {
             hour += 12;
             format = "AM";
         } else if (hour == 12) {
             format = "PM";
         } else if (hour > 12) {
             hour -= 12;
             format = "PM";
         } else {
             format = "AM";
         }
         return hour + " : " + String.format("%02d",min) + " " + format;
     }
 }
