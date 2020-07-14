package com.example.karakoram;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class homeFragment extends ListFragment  {

    final ArrayList<Event> event = new ArrayList<Event>();
    View view;


    public homeFragment() {
        // Required empty public constructor
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Event, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);  */

        event.add(new Event("Event 1","Mess meeting","9 PM"));
        event.add(new Event("Event 2","maint meeting","10 PM"));
        event.add(new Event("Event 3","elction meeting","5 PM"));
        event.add(new Event("Event 4","sports meeting","9 PM"));
        event.add(new Event("Event 5","general meeting","11 PM"));
        event.add(new Event("Event 6","freshers meeting","8 AM"));
        event.add(new Event("Event 7","Mess meeting","12 PM"));
        event.add(new Event("Event 8","Mess meeting","12 PM"));
        event.add(new Event("Event 9","Mess meeting","12 PM"));

        EventAdapter adapter = new EventAdapter(getActivity(),event);
        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDailog(i);
            }});


    }




    public void showDailog(int i)
    {    Event m=event.get(i);
         FragmentManager manager=getFragmentManager();
         mydialog dial=new mydialog(m.getEvent_name(),m.getEvent_details(),m.getEvent_time());
         dial.show(manager,"mydialog");

    }


}