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

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String[] fragmentList = {"Events","Bills","Mess", "User", "Complain Form"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeActivity.this,R.layout.support_simple_spinner_dropdown_item, fragmentList);
        ListView listView = findViewById(R.id.events);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                if(((TextView)view).getText().equals("Events"))
                    intent = new Intent(HomeActivity.this,EventHomeActivity.class);
                else if(((TextView)view).getText().equals("Bills"))
                    intent = new Intent(HomeActivity.this,BillHomeActivity.class);
                else
                    intent = new Intent(HomeActivity.this,EventHomeActivity.class);
                startActivity(intent);
            }
        });
    }

}
