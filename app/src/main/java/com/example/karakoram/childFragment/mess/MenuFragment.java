package com.example.karakoram.childFragment.mess;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.karakoram.FirebaseQuery;
import com.example.karakoram.R;
import com.example.karakoram.cache.mess.MessMenuCache;
import com.example.karakoram.otherFragment.FoodFragment;
import com.example.karakoram.resource.Menu;
import com.example.karakoram.views.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MenuFragment extends Fragment {

    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<Menu> allDayMenu = new ArrayList<>();
    private MessMenuCache cache;

    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        viewPager = view.findViewById(R.id.vp_menu);
        tabLayout = view.findViewById(R.id.tb_counter_menu);
        Context context = container.getContext();
        cache = new MessMenuCache(context);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            allDayMenu = cache.getMenuArray();
            days = cache.getDayArray();

            if (allDayMenu.isEmpty() || days.isEmpty()) {
                initVariables();
            }
            setupViews();
        } catch (Exception e) {
            Log.i("CacheLog", "some problem in getting cached content");
            initVariables();
        }
    }

    public void initVariables() {
        FirebaseQuery.getAllMenu().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allDayMenu.clear();
                days.clear();
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    Menu menuObject = snapshotItem.getValue(Menu.class);
                    allDayMenu.add(menuObject);
                    days.add(menuObject.getDay());
                }
                try {
                    cache.setDayArray(days);
                    cache.setMenuArray(allDayMenu);
                } catch (Exception ignored) {
                    Log.i("CacheLog", "cache files are not getting updated");
                }
                setupViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Something went wrong");
            }
        });
    }

    private void setupViews() {
        viewPager = view.findViewById(R.id.vp_menu);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.getLayoutParams().height = (Resources.getSystem().getDisplayMetrics().heightPixels * 2) / 3;
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        for (int i = 0; i < days.size(); i++) {
            String day = days.get(i);
            Menu menu = allDayMenu.get(i);
            adapter.addFragment(new FoodFragment(menu, this,i), "FOOD");
        }
        viewPager.setAdapter(adapter);
        updatePagerView();
    }

    private void updatePagerView() {
        Calendar sCalendar = Calendar.getInstance();
        String dayLongName = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        int nextPosition = days.indexOf(dayLongName);
        viewPager.setCurrentItem(nextPosition, false);
    }
}