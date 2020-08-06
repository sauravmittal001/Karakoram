package com.example.karakoram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.karakoram.R;
import com.example.karakoram.otherFragment.complaintChildFragment;
import com.example.karakoram.resource.Category;
import com.google.android.material.tabs.TabLayout;
import com.example.karakoram.adapter.pageadapter;

public class ComplaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        ViewPager viewPager = findViewById(R.id.viewpager);

        pageadapter adapter = new pageadapter(getSupportFragmentManager());
        adapter.addFragment(new complaintChildFragment(Category.Mess,false), "Mess");
        adapter.addFragment(new complaintChildFragment(Category.Maintenance,false), "Maint");
        adapter.addFragment(new complaintChildFragment(Category.Others,false), "Others");

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}