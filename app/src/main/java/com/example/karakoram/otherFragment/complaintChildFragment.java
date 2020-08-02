package com.example.karakoram.otherFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.adapter.ComplaintAdapter;
import com.example.karakoram.cache.HomeCache;
import com.example.karakoram.resource.Complaint;
import com.example.karakoram.resource.MaintComplaint;
import com.example.karakoram.resource.MessComplaint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lombok.SneakyThrows;

public class complaintChildFragment extends Fragment {

    private RecyclerView listView;

    Context context;
    View view;
    ComplaintAdapter adapter;
    ArrayList<String> key = new ArrayList<>();
    ArrayList<Complaint> complaints = new ArrayList<>();

    public complaintChildFragment() {
        // Required empty public constructor
    }

    public static complaintChildFragment newInstance(String param1, String param2) {
        complaintChildFragment fragment = new complaintChildFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_complaint_child, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViews();
        refreshListView();
    }

    @CallSuper
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void setViews() {
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_complaints);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setViews();
                refreshListView();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void start() {
        adapter = new ComplaintAdapter(getActivity(), complaints, key);
        listView = view.findViewById(R.id.list_complaints);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listView.setAdapter(adapter);
    }

    private void refreshListView() {
        complaints.clear();
        key.clear();
        FirebaseQuery.getOtherComplaints().addListenerForSingleValueEvent(new ValueEventListener() {
            @SneakyThrows
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    complaints.add(snapshotItem.getValue(Complaint.class));
                    key.add(snapshotItem.getKey());
                }
                start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });

        FirebaseQuery.getMessComplaints().addListenerForSingleValueEvent(new ValueEventListener() {
            @SneakyThrows
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    complaints.add(snapshotItem.getValue(MessComplaint.class));
                    key.add(snapshotItem.getKey());
                }
                start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });

        FirebaseQuery.getMaintComplaints().addListenerForSingleValueEvent(new ValueEventListener() {
            @SneakyThrows
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    complaints.add(snapshotItem.getValue(MaintComplaint.class));
                    key.add(snapshotItem.getKey());
                }
                start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });
    }
}