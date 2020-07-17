package com.example.karakoram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karakoram.R;

public class BillHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_home);
        String[] fragmentList = {"Mess","Maintenance","Sports", "Cultural", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BillHomeActivity.this,R.layout.support_simple_spinner_dropdown_item, fragmentList);
        ListView listView = findViewById(R.id.events);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(BillHomeActivity.this,BillFilterActivity.class);
            intent.putExtra("type",((TextView)view).getText());
            startActivity(intent);
            }
        });
    }

    public void onClickAddBill(View view){
        Intent intent = new Intent(this,BillFormActivity.class);
        startActivity(intent);
    }
}