package com.example.karakoram.childFragment.mess;

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

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.activity.BillActivity;
import com.example.karakoram.activity.BillFormActivity;
import com.example.karakoram.adapter.HostelBillAdapter;
import com.example.karakoram.adapter.MenuAdapter;
import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.HostelBill;
import com.example.karakoram.resource.Menu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class messMenuFragment extends Fragment {

    private ArrayList<String> key = new ArrayList<>();
    private ArrayList<Menu> menuList;

    /* Views */
    private View view;
    private ListView listView;

    /* Adapters */
    private MenuAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_mess_menu, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        FirebaseQuery.getAllMenu().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuList = new ArrayList<>();
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    Menu menu = new Menu();
                    String menuString = snapshotItem.getValue(String.class);
                    menu.setMenuString(menuString);
                    menuList.add(menu);
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


    private void start() {
        Log.i("ASDF", String.valueOf(menuList));
        adapter = new MenuAdapter(getActivity(), menuList,key);
        listView = view.findViewById(R.id.list_menu);
        listView.setAdapter(adapter);
    }
}