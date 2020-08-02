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
import com.example.karakoram.activity.ustDescription;
import com.example.karakoram.resource.MessFeedback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class USTadapter extends RecyclerView.Adapter<USTadapter.myViewHolder> {

    Context mcontext;
    ArrayList<MessFeedback> feedbacks;
    ArrayList<String> key;

    public USTadapter(Context mcontext, ArrayList<MessFeedback> feedbacks, ArrayList<String> key) {

        /* Here, we initialize the ArrayAdapter's internal storage for the context and the list.
         * the second argument is used when the ArrayAdapter is populating a single TextView.
         * Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
         * going to use this second argument, so it can be any value. Here, we used 0.
         */

        this.mcontext=mcontext;
        this.feedbacks=feedbacks;
        this.key=key;
        Log.i("ASDF", "print");
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v=LayoutInflater.from(mcontext).inflate(R.layout.ust_list_view,parent,false);
        final USTadapter.myViewHolder vHolder=new USTadapter.myViewHolder(v);
        vHolder.ust_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=vHolder.getAdapterPosition();
                Intent intent = new Intent(mcontext, ustDescription.class);
                intent.putExtra("key", key.get(i));

                intent.putExtra("description", feedbacks.get(i).getDescription());
                intent.putExtra("rating", feedbacks.get(i).getRating());
                intent.putExtra("userdetails",feedbacks.get(i).getUserId());
                 Date dateTime = feedbacks.get(i).getTimestamp();
                String day= new SimpleDateFormat("EE").format(dateTime);
                String date = (dateTime.getYear() + 1900) + "-" + String.format("%02d",dateTime.getMonth() + 1) + "-" + String.format("%02d",dateTime.getDate());
                intent.putExtra("date", date);
                intent.putExtra("Meal", day+"/ "+feedbacks.get(i).getMeal().name());
                mcontext.startActivity(intent);
            }
        });

        return vHolder;
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
           // holder.mRating.setText(Integer.toString(messFeedback.getRating()));
            holder.mDescription.setText(messFeedback.getDescription());
            holder.mMeal.setText(messFeedback.getMeal().name());
            Date dateTime = messFeedback.getTimestamp();
            String date= new SimpleDateFormat("EE").format(dateTime);
           // String date = (dateTime.getYear() + 1900) + "-" + String.format("%02d",dateTime.getMonth() + 1) + "-" + String.format("%02d",dateTime.getDate());
            holder.mDate.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{


        LinearLayout ust_list_view;
        TextView mMeal;
        TextView mDate;
        RatingBar mRating;
        TextView mDescription;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mRating = itemView.findViewById(R.id.feedback_star);
            mDescription = itemView.findViewById(R.id.feedback_details);
            mMeal=itemView.findViewById(R.id.meal_type);
            mDate=itemView.findViewById(R.id.date_stamp);
            ust_list_view=(LinearLayout)itemView.findViewById(R.id.ust_item_id);

        }
    }


    public MessFeedback getItem(int position)
    {
        return feedbacks.get(position);
    }

}
