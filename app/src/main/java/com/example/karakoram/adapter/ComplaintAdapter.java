package com.example.karakoram.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karakoram.R;
import com.example.karakoram.activity.ComplaintActivity;
import com.example.karakoram.activity.ComplaintFormActivity;
import com.example.karakoram.activity.ComplaintDescriptionActivity;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.Complaint;
import com.example.karakoram.resource.MaintComplaint;
import com.example.karakoram.resource.MessComplaint;
import com.example.karakoram.resource.Status;
import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Date;

import static com.example.karakoram.R.drawable.green_status;
import static com.example.karakoram.R.drawable.orange_status;
import static com.example.karakoram.R.drawable.red_status;


public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.myViewHolder> {

    private Context mcontext;
    private ArrayList<Complaint> complaints;
    private ArrayList<String> keys;
    private String[] maintAreaList, maintAreaEnumList, messAreaList, messAreaEnumList;

    public ComplaintAdapter(Context mcontext, ArrayList<Complaint> complaints, ArrayList<String> keys) {
        this.mcontext=mcontext;
        this.complaints = complaints;
        this.keys=keys;
        this.maintAreaList = mcontext.getResources().getStringArray(R.array.maintenance_area);
        this.maintAreaEnumList = mcontext.getResources().getStringArray(R.array.maintenance_area_enums);
        this.messAreaList = mcontext.getResources().getStringArray(R.array.mess_area);
        this.messAreaEnumList = mcontext.getResources().getStringArray(R.array.mess_area_enums);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v=LayoutInflater.from(mcontext).inflate(R.layout.complaint_listview,parent,false);
        final myViewHolder vHolder=new myViewHolder(v);

        vHolder.complaint_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=vHolder.getAdapterPosition();
                Date dateTime = complaints.get(i).getTimestamp();
                String key = keys.get(i);
                String time = showTime(dateTime.getHours(), dateTime.getMinutes());
                String date = (dateTime.getYear() + 1900) + " " + monthName(dateTime.getMonth() + 1) + " " + String.format("%02d",dateTime.getDate());
                Intent intent = new Intent(mcontext, ComplaintDescriptionActivity.class);
                intent.putExtra("entryNumber", complaints.get(i).getEntryNumber());
                intent.putExtra("name", complaints.get(i).getUserName());
                intent.putExtra("status", String.valueOf(complaints.get(i).getStatus()));
                intent.putExtra("description", complaints.get(i).getDescription());
                intent.putExtra("isImageAttached", complaints.get(i).isImageAttached());
                intent.putExtra("time", time);
                intent.putExtra("date", date);
                intent.putExtra("key", key);
                Category category = complaints.get(i).getCategory();
                intent.putExtra("category", category.name());
                if (category.equals(Category.Maintenance)) {
                    String complaintArea = ((MaintComplaint) complaints.get(i)).getComplaintArea().toString();
                    int pos = ArrayUtils.toArrayList(maintAreaEnumList).indexOf(complaintArea);
                    String area = maintAreaList[pos];
                    String room = ((MaintComplaint) complaints.get(i)).getRoom();
                    String wing = ((MaintComplaint) complaints.get(i)).getWing().toString();
                    intent.putExtra("area", area);
                    intent.putExtra("complaintArea",complaintArea);
                    intent.putExtra("room", room);
                    intent.putExtra("wing", wing);
                } else if (category.equals(Category.Mess)) {
                    String complaintArea = ((MessComplaint) complaints.get(i)).getComplaintArea().toString();
                    int pos = ArrayUtils.toArrayList(messAreaEnumList).indexOf(complaintArea);
                    String area = messAreaList[pos];
                    intent.putExtra("complaintArea",complaintArea );
                    intent.putExtra("area", area);
                }
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
        Complaint complaint = getItem(position);
        if (complaint != null) {
            holder.mStatus.setText(complaint.getStatus().toString());
            holder.mName.setText(complaint.getUserName());

            if(complaint.getStatus().equals(Status.Fired))
                holder.mStatusButton.setImageDrawable(mcontext.getResources().getDrawable(red_status));
            else if(complaint.getStatus().equals(Status.Processing))
                holder.mStatusButton.setImageDrawable(mcontext.getResources().getDrawable(orange_status));
            else
                holder.mStatusButton.setImageDrawable(mcontext.getResources().getDrawable(green_status));

            if(complaint.getCategory().equals(Category.Maintenance)) {
                String areaEnum = ((MaintComplaint) complaint).getComplaintArea().toString();
                int pos = ArrayUtils.toArrayList(maintAreaEnumList).indexOf(areaEnum);
                holder.mArea.setText(maintAreaList[pos]);
            }
            else if(complaint.getCategory().equals(Category.Mess)) {
                String areaEnum = ((MessComplaint) complaint).getComplaintArea().toString();
                int pos = ArrayUtils.toArrayList(messAreaEnumList).indexOf(areaEnum);
                holder.mArea.setText(messAreaList[pos]);
            }
            else
                holder.mArea.setText(complaint.getDescription());

            Date lastUpdate = complaint.getTimestamp();
            int num=lastUpdate.getHours();
            String str="AM";
            if(num>12){num=num-12; str="PM";}
            @SuppressLint("DefaultLocale") String time = String.format("%02d", num) + " : " + String.format("%02d", lastUpdate.getMinutes()) + " " + str;
            @SuppressLint("DefaultLocale") String date = (lastUpdate.getYear() + 1900) + " " + monthName(lastUpdate.getMonth() + 1) + " " + String.format("%02d",lastUpdate.getDate());
            Date now = new Date();
            @SuppressLint("DefaultLocale") String dateNow = (now.getYear() + 1900) + " " + monthName(now.getMonth() + 1) + " " + String.format("%02d",now.getDate());

            if(date.equals(dateNow))
                holder.mTime.setText(time);
            else
                holder.mTime.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{


        private TextView mArea;
        private TextView mStatus;
        private TextView mName;
        private TextView mTime;
        private ImageButton mStatusButton;
        private LinearLayout complaint_list_view;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mStatus = itemView.findViewById(R.id.tv_status);
            mTime = itemView.findViewById(R.id.tv_last_update);
            mName = itemView.findViewById(R.id.tv_complain_user_name);
            mArea = itemView.findViewById(R.id.tv_complaint_area);
            mStatusButton = itemView.findViewById(R.id.status_imageButton);
            complaint_list_view=(LinearLayout)itemView.findViewById(R.id.complaint_item_id);
        }
    }

    public Complaint getItem(int position) {
        return complaints.get(position);
    }
}
