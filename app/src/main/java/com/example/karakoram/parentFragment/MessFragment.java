package com.example.karakoram.parentFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.karakoram.R;
import com.example.karakoram.adapter.PageAdapter;
import com.example.karakoram.childFragment.mess.MenuFragment;
import com.example.karakoram.childFragment.mess.MessFeedbackFragment;
import com.example.karakoram.childFragment.mess.FeedBackListFragment;
import com.google.android.material.tabs.TabLayout;

public class MessFragment extends Fragment {

    private View myfragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public MessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myfragment= inflater.inflate(R.layout.fragment_mess, container, false);
        viewPager=myfragment.findViewById(R.id.viewpager);
        tabLayout=myfragment.findViewById(R.id.tabs);
        return myfragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        if(getActivity().getIntent().getBooleanExtra("editMode",false)) {
            tabLayout.selectTab(tabLayout.getTabAt(1));
            tabLayout.setVisibility(View.GONE);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setUpViewPager(ViewPager viewPager){
        PageAdapter adapter=new PageAdapter(getChildFragmentManager());
        adapter.addFragment(new MenuFragment(), "Menu");
        adapter.addFragment(new MessFeedbackFragment(),"Form");
        adapter.addFragment(new FeedBackListFragment(false),"Feedbacks");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }
}
