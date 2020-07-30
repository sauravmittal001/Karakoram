package com.example.karakoram.childFragment.mess;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.Resource;
import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.childFragment.bill.signIn.AdminFragment;
import com.example.karakoram.childFragment.bill.signIn.ResidentFragment;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.Menu;
import com.example.karakoram.views.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SlidingFragment extends Fragment {

    View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
//    private String[] day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private List<String> days = new ArrayList<>();
    private List<String[]> allDayMenu = new ArrayList<>();

    public SlidingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initVariables();
//        initViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sliding, container, false);
        viewPager = view.findViewById(R.id.vp_menu);
        tabLayout = view.findViewById(R.id.tb_counter_menu);
        Context context = container.getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVariables();
    }

    private void initVariables() {
            FirebaseQuery.getAllMenu().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    allDayMenu.clear();
                    days.clear();
                    for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                        Menu menuObject = snapshotItem.getValue(Menu.class);
                        String[] menu = new String[3];
                        menu[0] = menuObject.getBreakFast();
                        menu[1] = menuObject.getLunch();
                        menu[2] = menuObject.getDinner();
                        allDayMenu.add(menu);
                        days.add(snapshotItem.getKey());
                    }
                    setupViews();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("firebase error", "Something went wrong");
                }
            });
    }

    private void initViews() {
        this.viewPager = view.findViewById(R.id.vp_menu);
        tabLayout = view.findViewById(R.id.tb_counter_menu);
    }

    private void setupViews() {
        tabLayout.setupWithViewPager(viewPager, true);
        this.viewPager.getLayoutParams().height = (Resources.getSystem().getDisplayMetrics().heightPixels * 2)/3;
        this.setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        for (int i = 0; i < days.size(); i++) {
            String day = days.get(i);
            String[] menu = allDayMenu.get(i);
            adapter.addFragment(new FoodFragment(day, menu), "FOOD");
        }
        viewPager.setAdapter(adapter);
        updatePagerView();
//        final Handler handler = new Handler();
//        final Runnable changeView = new Runnable() {
//            public void run() {
//                updatePagerView();
//                handler.postDelayed(this, 4000);
//            }
//        };
//        handler.postDelayed(changeView, 3000);
    }

    private void updatePagerView() {
        Calendar sCalendar = Calendar.getInstance();
        String dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        int nextPosition = days.indexOf(dayLongName);
//        int nextPosition = (viewPager.getCurrentItem() < viewPager.getAdapter().getCount() - 1) ? viewPager.getCurrentItem() + 1 : 0;
        viewPager.setCurrentItem(nextPosition, true);
    }
}