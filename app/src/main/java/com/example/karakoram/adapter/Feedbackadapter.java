package com.example.karakoram.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karakoram.R;
import com.example.karakoram.activity.FeedbackDescription;
import com.example.karakoram.resource.MessFeedback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Feedbackadapter extends RecyclerView.Adapter<Feedbackadapter.myViewHolder> {

    private Context mcontext;
    private ArrayList<MessFeedback> feedbacks;
    private ArrayList<String> key;

    public Feedbackadapter(Context mcontext, ArrayList<MessFeedback> feedbacks, ArrayList<String> key) {
        this.mcontext=mcontext;
        this.feedbacks=feedbacks;
        this.key=key;
        Log.i("ASDF", "print");
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v=LayoutInflater.from(mcontext).inflate(R.layout.feedback_list_view,parent,false);
        final Feedbackadapter.myViewHolder vHolder=new Feedbackadapter.myViewHolder(v);
        vHolder.ust_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=vHolder.getAdapterPosition();
                Date dateTime = feedbacks.get(i).getTimestamp();
                String time = showTime(dateTime.getHours(), dateTime.getMinutes());//String.format("%02d", dateTime.getHours()) + " : " + String.format("%02d", dateTime.getMinutes());
                String date = (dateTime.getYear() + 1900) + " " + monthName(dateTime.getMonth() + 1)/*String.format("%02d",dateTime.getMonth() + 1)*/ + " " + String.format("%02d",dateTime.getDate());
                String day= new SimpleDateFormat("EEEE").format(dateTime);
                Intent intent = new Intent(mcontext, FeedbackDescription.class);
                intent.putExtra("key", key.get(i));
                intent.putExtra("userId",feedbacks.get(i).getUserId());
                intent.putExtra("userName",feedbacks.get(i).getUserName());
                intent.putExtra("description", feedbacks.get(i).getDescription());
                intent.putExtra("rating", String.valueOf(feedbacks.get(i).getRating()));
                intent.putExtra("meal", feedbacks.get(i).getMeal().toString());
                intent.putExtra("time", time);
                intent.putExtra("date", date);
                intent.putExtra("day", day);
                intent.putExtra("anonymity",feedbacks.get(i).getAnonymity().toString());
                mcontext.startActivity(intent);
            }
        });

        return vHolder;
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
        return hour + " : " + min + " " + format;
    }

    public String monthName (int monthNumber) {
        String[] monthOfYear = mcontext.getResources().getStringArray(R.array.months);
        return monthOfYear[monthNumber-1];
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        MessFeedback messFeedback = getItem(position);
        if (messFeedback != null) {
            String description = messFeedback.getDescription();
            int rating=messFeedback.getRating();
            holder.mRating.setNumStars(3);
            holder.mRating.setStepSize(1);
            holder.mRating.setRating(rating);
            holder.mDescription.setText(messFeedback.getDescription());
            holder.mMeal.setText(messFeedback.getMeal().name());
            Date timestamp = messFeedback.getTimestamp();
            String[] days = mcontext.getResources().getStringArray(R.array.days);
            String day = days[timestamp.getDay()];
            holder.mDay.setText(day);
        }
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{


        private LinearLayout ust_list_view;
        private TextView mMeal;
        private TextView mDay;
        private RatingBar mRating;
        private TextView mDescription;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mRating = itemView.findViewById(R.id.feedback_star);
            mDescription = itemView.findViewById(R.id.feedback_details);
            mMeal=itemView.findViewById(R.id.meal_type);
            mDay=itemView.findViewById(R.id.day_stamp);
            ust_list_view=(LinearLayout)itemView.findViewById(R.id.ust_item_id);

        }
    }


    public MessFeedback getItem(int position)
    {
        return feedbacks.get(position);
    }

}
