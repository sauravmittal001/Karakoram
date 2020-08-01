package com.example.karakoram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.karakoram.R;
import com.example.karakoram.otherFragment.complaintChildFragment;
import com.google.android.material.tabs.TabLayout;
import com.example.karakoram.adapter.pageadapter;

public class ComplaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        //find the view that shows the number category

// Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        pageadapter adapter = new pageadapter(getSupportFragmentManager());
        adapter.addFragment(new complaintChildFragment(), "Mess");
        adapter.addFragment(new complaintChildFragment(), "Maint");
        adapter.addFragment(new complaintChildFragment(), "Others");

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
    }
}