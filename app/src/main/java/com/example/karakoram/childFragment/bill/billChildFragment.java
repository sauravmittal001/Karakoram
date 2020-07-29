package com.example.karakoram.childFragment.bill;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.karakoram.activity.BillActivity;
import com.example.karakoram.activity.BillFormActivity;
import com.example.karakoram.resource.Category;
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.activity.HostelBillDescription;
import com.example.karakoram.R;
import com.example.karakoram.resource.HostelBill;
import com.example.karakoram.adapter.HostelBillAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class billChildFragment extends Fragment {

//   ArrayList<Integer> bill= new ArrayList<>();

    /* Variables */
    private ArrayList<String> key = new ArrayList<>();
    private ArrayList<HostelBill> hostelBill;
    private Category category;

    /* Views */
    private View view;
    private ListView listView;
    private FloatingActionButton fab;

    /* Adapters */
    private HostelBillAdapter adapter;

    public billChildFragment(Category category) {
        this.category = category;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_bill_child, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        FirebaseQuery.getCategoryBills(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hostelBill = new ArrayList<>();
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    hostelBill.add(snapshotItem.getValue(HostelBill.class));
                    key.add(snapshotItem.getKey());
                }
                start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });

        fab=view.findViewById(R.id.FABchild_bill);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), BillFormActivity.class);
                startActivity(intent);

            }
        });
    }


    private void start() {
        Log.i("ASDF", String.valueOf(hostelBill));
        adapter = new HostelBillAdapter(getActivity(), hostelBill);
        listView = view.findViewById(R.id.billlistView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), HostelBillDescription.class);
                intent.putExtra("key", key.get(i));
                startActivity(intent);
            }
        });
    }
}