package com.example.karakoram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karakoram.R;
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.resource.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BillFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_filter);
        TextView headingView = findViewById(R.id.heading_bill_filter);
        Intent intent = this.getIntent();
        String heading = intent.getStringExtra("type");
        headingView.setText(heading);
        Query query = FirebaseQuery.getCategoryBills(Category.valueOf(heading));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> billList = new ArrayList<>();
                for( DataSnapshot snapshotItem : snapshot.getChildren()){
                    billList.add(snapshotItem.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(BillFilterActivity.this,R.layout.support_simple_spinner_dropdown_item, billList);
                ListView listView = findViewById(R.id.events);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(BillFilterActivity.this,BillActivity.class);
                        intent.putExtra("key",((TextView)view).getText().toString());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error","Something went wrong");
            }
        });
    }
}