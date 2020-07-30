package com.example.karakoram.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karakoram.R;
import com.example.karakoram.resource.Event;
import com.example.karakoram.childFragment.events.EventDescription;

import java.util.ArrayList;
import java.util.Date;


 public class EventAdapter extends RecyclerView.Adapter<EventAdapter.myViewHolder> {

    Context mcontext;
    ArrayList<Event> event1;
    ArrayList<String> key;

    public EventAdapter(Context mcontext, ArrayList<Event> event1,ArrayList<String> key) {
        /* Here, we initialize the ArrayAdapter's internal storage for the context and the list.
         * the second argument is used when the ArrayAdapter is populating a single TextView.
         * Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
         * going to use this second argument, so it can be any value. Here, we used 0.
         */
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
                String time = String.format("%02d", dateTime.getHours()) + " : " + String.format("%02d", dateTime.getMinutes());
                String date = (dateTime.getYear() + 1900) + "-" + String.format("%02d",dateTime.getMonth() + 1) + "-" + String.format("%02d",dateTime.getDate());
                Intent intent = new Intent(mcontext, EventDescription.class);
                intent.putExtra("title", event1.get(i).getTitle());
                intent.putExtra("description", event1.get(i).getDescription());
                intent.putExtra("time", time);
                intent.putExtra("date", date);
                intent.putExtra("key", key.get(i));
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
             int num=dateTime.getHours();
             String str="AM";
             if(num>12){num=num-12; str="PM";}
             String time = String.format("%02d", num) + " : " + String.format("%02d", dateTime.getMinutes())+" "+str;
             String date = (dateTime.getYear() + 1900) + "-" + String.format("%02d",dateTime.getMonth() + 1) + "-" + String.format("%02d",dateTime.getDate());

             holder.mTitle.setText(event.getTitle());
             holder.mTime.setText(time);
             holder.mDate.setText(date);
         }

     }

     @Override
     public int getItemCount() {
         return event1.size();
     }


     public static class myViewHolder extends RecyclerView.ViewHolder{


         TextView mTitle;
         TextView mDescription;
         TextView mTime;
         TextView mDate;
         LinearLayout event_list_view;


         public myViewHolder(@NonNull View itemView) {
             super(itemView);
             mTitle = itemView.findViewById(R.id.tv_event_title);
             mTime = itemView.findViewById(R.id.tv_event_time_list);
             mDate = itemView.findViewById(R.id.tv_event_date_list);
             event_list_view=(LinearLayout)itemView.findViewById(R.id.event_item_id);

         }
     }

     public Event getItem(int position)
     {
         return event1.get(position);
     }
 }
