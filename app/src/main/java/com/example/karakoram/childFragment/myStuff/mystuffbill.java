package com.example.karakoram.childFragment.myStuff;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.activity.BillFormActivity;
import com.example.karakoram.adapter.HostelBillAdapter;
import com.example.karakoram.cache.bill.HostelBillCache;
import com.example.karakoram.cache.myStuff.BillCache;
import com.example.karakoram.resource.HostelBill;
import com.example.karakoram.resource.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class mystuffbill extends Fragment {

//   ArrayList<Integer> bill= new ArrayList<>();

    /* Variables */
    private ArrayList<String> key = new ArrayList<>();
    private ArrayList<HostelBill> hostelBill;
    private Context context;
    private BillCache cache;
    private SwipeRefreshLayout swipeRefreshLayout;


    /* Views */
    private View view;
    private RecyclerView listView;
    private FloatingActionButton fab;
    Drawable mdivider;

    /* Adapters */
    private HostelBillAdapter adapter;

    public mystuffbill() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_bill_child, container, false);
        context = container.getContext();
        cache = new BillCache(context);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViews();

        try {
            hostelBill = cache.getHostelBillArray();
            key = cache.getKeyArray();
            Log.i("myStuffBill", "hostelBills: " + hostelBill);
            Log.i("myStuffBill", "keys: " + key);

            if (hostelBill.isEmpty() || key.isEmpty()) {
                Log.i("myStuffBill", "lists were empty");
                refreshListView();
            }
            start();
            Log.i("myStuffBill", "try block");
        } catch (Exception e) {
            Log.i("myStuffBill", "some problem in getting cached content");
            refreshListView();
        }

    }

    private void setViews() {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListView();
                setViews();
                adapter.notifyDataSetChanged();

            }
        });

        fab=view.findViewById(R.id.FABchild_bill);
        fab.setVisibility(View.GONE);
    }

    private void refreshListView() {
        if (key != null ) {
            key.clear();
        }
        if (hostelBill != null) {
            hostelBill.clear();
        }

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","loggedOut");
        FirebaseQuery.getUserBill(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hostelBill = new ArrayList<>();
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    hostelBill.add(snapshotItem.getValue(HostelBill.class));
                    key.add(snapshotItem.getKey());
                }
                try {
                    cache.setKeyArray(key);
                    cache.setHostelBillArray(hostelBill);
                    Log.i("myStuffBill", "hostelBills: after refresh" + hostelBill);
                    Log.i("myStuffBill", "keys: after refresh" + key);
                } catch (Exception ignored) {
                    Log.i("myStuffBill", "cache files are not getting updated");
                }
                start();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });
    }


    private void start() {
        Log.i("ASDF", String.valueOf(hostelBill));
        adapter = new HostelBillAdapter(getActivity(), hostelBill,key);
        listView = view.findViewById(R.id.billlistView);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mdivider= ContextCompat.getDrawable(view.getContext(),R.drawable.divider);
        //mdivider.setBounds(getParentFragment().getPaddingLeft(),0,16,0);
        DividerItemDecoration itemdecor=new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL);
        itemdecor.setDrawable(mdivider);
        listView.addItemDecoration(itemdecor);
        listView.setAdapter(adapter);
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), HostelBillDescription.class);
                intent.putExtra("key", key.get(i));
                startActivity(intent);
            }
        }); */
    }
}