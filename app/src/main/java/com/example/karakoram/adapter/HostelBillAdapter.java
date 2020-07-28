package com.example.karakoram.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karakoram.R;
import com.example.karakoram.activity.BillActivity;
import com.example.karakoram.resource.HostelBill;

import java.util.ArrayList;

public class HostelBillAdapter extends RecyclerView.Adapter<HostelBillAdapter.myViewHolder> {

    TextView mAmount;
    TextView mDescription;
    Context mcontext;
    ArrayList<HostelBill> bill;
    ArrayList<String> key;

    public HostelBillAdapter(Context mcontext, ArrayList<HostelBill> bill, ArrayList<String> key) {

        /* Here, we initialize the ArrayAdapter's internal storage for the context and the list.
         * the second argument is used when the ArrayAdapter is populating a single TextView.
         * Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
         * going to use this second argument, so it can be any value. Here, we used 0.
         */
       this.mcontext=mcontext;
       this.bill=bill;
       this.key=key;
        Log.i("ASDF", "print");
    }

  /*  @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View list = convertView;
        if (list == null) {
            list = LayoutInflater.from(getContext()).inflate(R.layout.hostel_bill_listview, parent, false);
        }

        HostelBill hostelBill = getItem(position);

        mAmount = list.findViewById(R.id.tv_bill_amount);
        mDescription = list.findViewById(R.id.tv_bill_description);

        if (hostelBill != null) {
            String description = hostelBill.getDescription().subSequence(0, Math.min(15, hostelBill.getDescription().length())) + "...";
            mAmount.setText(String.format("Rs %s", hostelBill.getAmount()));
            mDescription.setText(hostelBill.getDescription());
        }
        return list;
    } */

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
                Intent intent = new Intent(mcontext, BillActivity.class);
                intent.putExtra("key", key.get(i));
                mcontext.startActivity(intent);
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        HostelBill hostelBill = getItem(position);
        if (hostelBill != null) {
            String description = hostelBill.getDescription().subSequence(0, Math.min(15, hostelBill.getDescription().length())) + "...";
            holder.mAmount.setText(String.format("Rs %s", hostelBill.getAmount()));
            holder.mDescription.setText(hostelBill.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return bill.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{


        LinearLayout bill_list_view;
        TextView mAmount;
        TextView mDescription;



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
