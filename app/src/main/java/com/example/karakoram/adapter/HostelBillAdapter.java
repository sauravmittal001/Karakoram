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
import com.example.karakoram.activity.BillDescriptionActivity;
import com.example.karakoram.resource.HostelBill;

import java.util.ArrayList;
import java.util.Date;

public class HostelBillAdapter extends RecyclerView.Adapter<HostelBillAdapter.myViewHolder> {

    private Context mcontext;
    private ArrayList<HostelBill> bill;
    private ArrayList<String> key;

    public HostelBillAdapter(Context mcontext, ArrayList<HostelBill> bill, ArrayList<String> key) {
       this.mcontext=mcontext;
       this.bill=bill;
       this.key=key;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v=LayoutInflater.from(mcontext).inflate(R.layout.hostel_bill_listview,parent,false);
        final HostelBillAdapter.myViewHolder vHolder=new HostelBillAdapter.myViewHolder(v);
        vHolder.bill_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=vHolder.getAdapterPosition();
                Date dateTime = bill.get(i).getTimeStamp();
                Log.i("CRASHHH", String.valueOf(dateTime));
                String time = showTime(dateTime.getHours(), dateTime.getMinutes());
                String date = (dateTime.getYear() + 1900) + " " + monthName(dateTime.getMonth() + 1)+ " " + String.format("%02d",dateTime.getDate());
                Intent intent = new Intent(mcontext, BillDescriptionActivity.class);
                intent.putExtra("category", String.valueOf(bill.get(i).getCategory()));
                intent.putExtra("description", bill.get(i).getDescription());
                intent.putExtra("amount", String.valueOf(bill.get(i).getAmount()));
                intent.putExtra("userId", bill.get(i).getUserId());
                intent.putExtra("time", time);
                intent.putExtra("date", date);
                intent.putExtra("key", key.get(i));
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
        return hour + " : " + String.format("%02d",min) + " " + format;
    }

    public String monthName (int monthNumber) {
        String[] monthOfYear = mcontext.getResources().getStringArray(R.array.months);
        return monthOfYear[monthNumber-1];
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        HostelBill hostelBill = getItem(position);
        if (hostelBill != null) {
            holder.mAmount.setText(String.format("Rs %s", hostelBill.getAmount()));

            String description;
            if(hostelBill.getDescription().length()>15)
                description = hostelBill.getDescription().substring(0,15) + "...";
            else
                description = hostelBill.getDescription();

            holder.mDescription.setText(description);
        }
    }

    @Override
    public int getItemCount() {
        return bill.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout bill_list_view;
        private TextView mAmount;
        private TextView mDescription;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mAmount = itemView.findViewById(R.id.tv_bill_amount);
            mDescription = itemView.findViewById(R.id.tv_bill_description);
            bill_list_view=(LinearLayout)itemView.findViewById(R.id.bill_item_id);

        }
    }


    public HostelBill getItem(int position)
    {
        return bill.get(position);
    }

}
