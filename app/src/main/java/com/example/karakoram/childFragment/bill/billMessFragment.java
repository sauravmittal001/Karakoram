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

import com.example.karakoram.resources.Category;
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.activity.HostelBillDescription;
import com.example.karakoram.R;
import com.example.karakoram.resources.HostelBill;
import com.example.karakoram.adapter.HostelBillAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class billMessFragment extends Fragment {

//   ArrayList<Integer> bill= new ArrayList<>();

    /* Variables */
    ArrayList<String> key = new ArrayList<>();
    ArrayList<HostelBill> hostelBill = new ArrayList<>();

    /* Views */
    View view;
    ListView listView;
    FloatingActionButton fab;

    /* Adapters */
    HostelBillAdapter adapter;

    public billMessFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_bill_mess, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        FirebaseQuery.getCategoryBills(Category.Maintenance).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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


//        listView = (ListView) Arrays.asList( R.drawable.download_1,R.drawable.download,R.drawable.images,R.drawable.images_1,R.drawable.images_2,R.drawable.images_3,R.drawable.download_2,R.drawable.download_3, R.drawable.download_4,R.drawable.download_5,R.drawable.download_6);
//        //initialize gridview object
//        listView=view.findViewById(R.id.messbilllistView);
//        //set adapter on gridview object
//        listView.setAdapter(new imageAdapter(bill,getActivity()));
//
//        //when click on image it open full screen iamge
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Intent i = new Intent( getActivity().getApplicationContext(), fullimageActivity.class);
//                // passing array index
//                i.putExtra("bill", bill);
//                i.putExtra("currentbill",position);
//                startActivity(i);
//            }
//        });


        fab=view.findViewById(R.id.FABmess_bill);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);

            }
        });
    }


    private void start() {
        Log.i("ASDF", String.valueOf(hostelBill));
        adapter = new HostelBillAdapter(getActivity(), hostelBill);
        if (adapter == null) {
            Log.i("ASDF", "null adapter");
        } else {
            Log.i("ASDF", "ADAPTER = " + adapter);
        }
        listView = view.findViewById(R.id.messbilllistView);
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