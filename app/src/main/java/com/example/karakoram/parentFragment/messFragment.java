package com.example.karakoram.parentFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.karakoram.R;
import com.example.karakoram.adapter.pageadapter;
import com.example.karakoram.childFragment.mess.MenuFragment;
import com.example.karakoram.childFragment.mess.messFeedbackFragment;
import com.example.karakoram.childFragment.mess.messUSTFragment;
import com.google.android.material.tabs.TabLayout;

public class messFragment extends Fragment {

    private View myfragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public messFragment() {
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
        pageadapter adapter=new pageadapter(getChildFragmentManager());
        adapter.addFragment(new MenuFragment(), "Menu");
        adapter.addFragment(new messFeedbackFragment(),"Feedback");
        adapter.addFragment(new messUSTFragment(false),"UST");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }
}
