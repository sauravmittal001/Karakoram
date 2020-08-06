package com.example.karakoram.otherFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.adapter.ComplaintAdapter;
import com.example.karakoram.cache.complaint.MaintenanceComplaintCache;
import com.example.karakoram.cache.complaint.MessComplaintCache;
import com.example.karakoram.cache.complaint.OtherComplaintCache;
import com.example.karakoram.cache.complaint.allComplaintsCache.AllMaintenanceComplaintCache;
import com.example.karakoram.cache.complaint.allComplaintsCache.AllMessComplaintCache;
import com.example.karakoram.cache.complaint.allComplaintsCache.AllOtherComplaintCache;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.Complaint;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.MaintComplaint;
import com.example.karakoram.resource.MessComplaint;
import com.example.karakoram.resource.User;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import lombok.SneakyThrows;

public class ComplaintChildFragment extends Fragment {

    private RecyclerView listView;
    private Context context;
    private View view;
    private ComplaintAdapter adapter;
    private ArrayList<Pair<String, Complaint>> complaintsKv = new ArrayList<>();
    private Category category;
    private boolean getAllCategory;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaintenanceComplaintCache maintenanceComplaintCache;
    private MessComplaintCache messComplaintCache;
    private OtherComplaintCache otherComplaintCache;
    private AllMaintenanceComplaintCache allMaintenanceComplaintCache;
    private AllMessComplaintCache allMessComplaintCache;
    private AllOtherComplaintCache allOtherComplaintCache;

    public ComplaintChildFragment(Category category, boolean getAllCategory) {
        this.category = category;
        this.getAllCategory = getAllCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_complaint_child, container, false);
        context = container.getContext();
        maintenanceComplaintCache = new MaintenanceComplaintCache(context);
        messComplaintCache = new MessComplaintCache(context);
        otherComplaintCache = new OtherComplaintCache(context);
        allMaintenanceComplaintCache = new AllMaintenanceComplaintCache(context);
        allMessComplaintCache = new AllMessComplaintCache(context);
        allOtherComplaintCache = new AllOtherComplaintCache(context);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViews();
        try {
            ArrayList<Complaint> complaints = new ArrayList<>();
            ArrayList<String> key = new ArrayList<>();
            if(getAllCategory){
                complaints.addAll(allOtherComplaintCache.getComplaintArray());
                complaints.addAll(allMaintenanceComplaintCache.getComplaintArray());
                complaints.addAll(allMessComplaintCache.getComplaintArray());
                key.addAll(allOtherComplaintCache.getKeyArray());
                key.addAll(allMaintenanceComplaintCache.getKeyArray());
                key.addAll(allMessComplaintCache.getKeyArray());
//                Log.d("123hello", String.valueOf(allMaintenanceComplaintCache.getComplaintArray().size()));
            }
            else if(category.equals(Category.Maintenance)){
                complaints.addAll(maintenanceComplaintCache.getComplaintArray());
                key.addAll(maintenanceComplaintCache.getKeyArray());
            }
            else if(category.equals(Category.Mess)){
                complaints.addAll(messComplaintCache.getComplaintArray());
                key.addAll(messComplaintCache.getKeyArray());
            }
            else{
                complaints.addAll(otherComplaintCache.getComplaintArray());
                key.addAll(otherComplaintCache.getKeyArray());
            }
            complaintsKv.clear();
            for(int i = 0; i<complaints.size();i++)
                complaintsKv.add(Pair.create(key.get(i),complaints.get(i)));

            if (complaints.isEmpty() || key.isEmpty()) {
                refreshListView();
            }
            start();
        } catch (Exception e) {
            Log.i("ComplaintCacheLog", "some problem in getting cached content " + e);
            refreshListView();
        }
    }

    @CallSuper
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void setViews() {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_complaints);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setViews();
                refreshListView();
            }
        });
    }

    private void start() {
        Collections.sort(complaintsKv, new Comparator<Pair<String, Complaint>>() {
            @Override
            public int compare(Pair<String, Complaint> stringComplaintPair, Pair<String, Complaint> t1) {
                return t1.second.getTimestamp().compareTo(stringComplaintPair.second.getTimestamp());
            }
        });
        ArrayList<Complaint> complaints = new ArrayList<>();
        ArrayList<String> key = new ArrayList<>();
        for(int i=0; i<complaintsKv.size();i++) {
            key.add(complaintsKv.get(i).first);
            complaints.add(complaintsKv.get(i).second);
        }

        if (getActivity() == null) {
            return;
        }
        adapter = new ComplaintAdapter(getActivity(), complaints, key);
        listView = view.findViewById(R.id.list_complaints);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listView.setAdapter(adapter);
    }

    private void refreshListView() {
        complaintsKv.clear();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(User.SHARED_PREFS,Context.MODE_PRIVATE);
        final String userId = sharedPreferences.getString("userId","loggedOut");
        if(getAllCategory || category.equals(Category.Mess)) {
            FirebaseQuery.getMessComplaints().addListenerForSingleValueEvent(new ValueEventListener() {
                @SneakyThrows
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Pair<String,Complaint>> complaintsKvMess = new ArrayList<>();
                    for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                        Complaint complaint = snapshotItem.getValue(MessComplaint.class);
                        if(!getAllCategory || complaint.getUserId().equals(userId)) {
                            complaintsKvMess.add(Pair.create(snapshotItem.getKey(),complaint));
                        }
                    }
                    try {
                        ArrayList<MessComplaint> complaints = new ArrayList<>();
                        ArrayList<String> key = new ArrayList<>();
                        for(int i=0; i<complaintsKvMess.size();i++){
                            key.add(complaintsKvMess.get(i).first);
                            complaints.add((MessComplaint) complaintsKvMess.get(i).second);
                        }
                        if(getAllCategory) {
                            allMessComplaintCache.setKeyArray(key);
                            allMessComplaintCache.setValueArray(complaints);
                        }
                        else{
                            messComplaintCache.setKeyArray(key);
                            messComplaintCache.setValueArray(complaints);
                        }
                    } catch (Exception ignored) {
                        Log.i("HomeCacheLog", "cache files are not getting updated");
                    }
                    complaintsKv.addAll(complaintsKvMess);
                    start();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("firebase error", "Something went wrong");
                }
            });
        }
        if(getAllCategory || category.equals(Category.Maintenance)) {
            FirebaseQuery.getMaintComplaints().addListenerForSingleValueEvent(new ValueEventListener() {
                @SneakyThrows
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Pair<String,Complaint>> complaintsKvMaint = new ArrayList<>();
                    for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                        Complaint complaint = snapshotItem.getValue(MaintComplaint.class);
                        if(!getAllCategory || complaint.getUserId().equals(userId)) {
                            complaintsKvMaint.add(Pair.create(snapshotItem.getKey(),complaint));
                        }
                    }
                    try {
                        ArrayList<MaintComplaint> complaints = new ArrayList<>();
                        ArrayList<String> key = new ArrayList<>();
                        for(int i=0; i<complaintsKvMaint.size();i++){
                            key.add(complaintsKvMaint.get(i).first);
                            complaints.add((MaintComplaint) complaintsKvMaint.get(i).second);
                        }
                        if(getAllCategory) {
                            allMaintenanceComplaintCache.setKeyArray(key);
                            allMaintenanceComplaintCache.setValueArray(complaints);
                            Log.d("123hello", String.valueOf(complaints.size()));
                        }
                        else{
                            maintenanceComplaintCache.setKeyArray(key);
                            maintenanceComplaintCache.setValueArray(complaints);
                        }
                    } catch (Exception ignored) {
                        Log.i("ComplaintCacheLog", "cache files are not getting updated");
                    }
                    complaintsKv.addAll(complaintsKvMaint);
                    start();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("firebase error", "Something went wrong");
                }
            });
        }
        if(getAllCategory || category.equals(Category.Others)){
            FirebaseQuery.getOtherComplaints().addListenerForSingleValueEvent(new ValueEventListener() {
                @SneakyThrows
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Pair<String,Complaint>> complaintsKvOthers = new ArrayList<>();
                    for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                        Complaint complaint = snapshotItem.getValue(Complaint.class);
                        if(!getAllCategory || complaint.getUserId().equals(userId)) {
                            complaintsKvOthers.add(Pair.create(snapshotItem.getKey(),complaint));
                        }
                    }
                    try {
                        ArrayList<Complaint> complaints = new ArrayList<>();
                        ArrayList<String> key = new ArrayList<>();
                        for(int i=0; i<complaintsKvOthers.size();i++){
                            key.add(complaintsKvOthers.get(i).first);
                            complaints.add(complaintsKvOthers.get(i).second);
                        }
                        if(getAllCategory) {
                            allOtherComplaintCache.setKeyArray(key);
                            allOtherComplaintCache.setValueArray(complaints);
                        }
                        else{
                            otherComplaintCache.setKeyArray(key);
                            otherComplaintCache.setValueArray(complaints);
                        }
                    } catch (Exception ignored) {
                        Log.i("HomeCacheLog", "cache files are not getting updated");
                    }
                    complaintsKv.addAll(complaintsKvOthers);
                    start();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("firebase error", "Something went wrong");
                }
            });
        }
    }
}