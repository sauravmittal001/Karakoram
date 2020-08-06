package com.example.karakoram.childFragment.bill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.HostelBill;
import com.example.karakoram.resource.User;
import com.example.karakoram.resource.UserType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;


public class billChildFragment extends Fragment {

    /* Variables */
    private ArrayList<Pair<String,HostelBill>> hostelBillsKv = new ArrayList<>();
    private Category category;
    private Context context;
    private HostelBillCache cache;
    private boolean getAll;
    private SharedPreferences sharedPreferences;

    /* Views */
    private View view;
    private RecyclerView listView;
    private FloatingActionButton fab;
    private Drawable mdivider;

    /* Adapters */
    private HostelBillAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public billChildFragment(Category category, boolean getAll) {
        this.category = category;
        this.getAll = getAll;
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
        cache = new HostelBillCache(context, category, getAll);
        sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViews();

        try {
            ArrayList<HostelBill> hostelBill = cache.getHostelBillArray();
            ArrayList<String> key = cache.getKeyArray();
            hostelBillsKv.clear();
            for(int i = 0; i<hostelBill.size();i++)
                hostelBillsKv.add(Pair.create(key.get(i),hostelBill.get(i)));

            if (hostelBill.isEmpty() || key.isEmpty()) {
                Log.i("111111 " + category.name(), "lists were empty");
                refreshListView();
            }
            start();
            Log.i("111111 " + category.name(), "try block");
        } catch (Exception e) {
            Log.i("111111 " + category.name(), "some problem in getting cached content");
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), BillFormActivity.class);
                intent.putExtra("category", category.name());
                startActivity(intent);

            }
        });
        UserType userType = UserType.valueOf(sharedPreferences.getString("type","Student"));
        if(userType.equals(UserType.Student))
            fab.setVisibility(View.GONE);
        else
            fab.setVisibility(View.VISIBLE);
    }

    private void refreshListView() {
        String userId = sharedPreferences.getString("userId","loggedOut");
        if(!userId.equals("loggedOut")) {
            Query query;
            if (getAll)
                query = FirebaseQuery.getUserBill(userId);
            else
                query = FirebaseQuery.getCategoryBills(category);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    hostelBillsKv.clear();
                    for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                        hostelBillsKv.add(Pair.create(snapshotItem.getKey(), snapshotItem.getValue(HostelBill.class)));
                    }
                    try {
                        ArrayList<HostelBill> hostelBill = new ArrayList<>();
                        ArrayList<String> key = new ArrayList<>();
                        for (int i = 0; i < hostelBillsKv.size(); i++) {
                            key.add(hostelBillsKv.get(i).first);
                            hostelBill.add(hostelBillsKv.get(i).second);
                        }
                        cache.setKeyArray(key);
                        cache.setHostelBillArray(hostelBill);
                        Log.i("111111 " + category.name(), "hostelBills: after refresh" + hostelBill);
                        Log.i("111111 " + category.name(), "keys: after refresh" + key);
                    } catch (Exception ignored) {
                        Log.i("111111 " + category.name(), "cache files are not getting updated");
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
        else {
            hostelBillsKv.clear();
            try {
                ArrayList<HostelBill> hostelBill = new ArrayList<>();
                ArrayList<String> key = new ArrayList<>();
                for (int i = 0; i < hostelBillsKv.size(); i++) {
                    key.add(hostelBillsKv.get(i).first);
                    hostelBill.add(hostelBillsKv.get(i).second);
                }
                cache.setKeyArray(key);
                cache.setHostelBillArray(hostelBill);
                Log.i("111111 " + category.name(), "hostelBills: after refresh" + hostelBill);
                Log.i("111111 " + category.name(), "keys: after refresh" + key);
            } catch (Exception ignored) {
                Log.i("111111 " + category.name(), "cache files are not getting updated");
            }
            start();
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    private void start() {
        Collections.sort(hostelBillsKv, new Comparator<Pair<String, HostelBill>>() {
            @Override
            public int compare(Pair<String, HostelBill> stringHostelBillPair, Pair<String, HostelBill> t1) {
                return t1.second.getTimeStamp().compareTo(stringHostelBillPair.second.getTimeStamp());
            }
        });

        ArrayList<HostelBill> hostelBill = new ArrayList<>();
        ArrayList<String> key = new ArrayList<>();
        for(int i=0; i<hostelBillsKv.size();i++){
            key.add(hostelBillsKv.get(i).first);
            hostelBill.add(hostelBillsKv.get(i).second);
        }
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
    }
}