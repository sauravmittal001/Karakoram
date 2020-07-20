package com.example.karakoram.parentFragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karakoram.R;
import com.example.karakoram.childFragment.mess.messFeedbackFragment;
import com.example.karakoram.childFragment.mess.messMenuFragment;
import com.example.karakoram.childFragment.mess.messUSTFragment;
import com.example.karakoram.childFragment.pageadapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link messFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class messFragment extends Fragment {

    View myfragment;

    ViewPager viewPager;
    TabLayout tabLayout;




    public messFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        //find the view that shows the number category

// Find the view pager that will allow the user to swipe between fragments
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
        adapter.addFragment(new messMenuFragment(),"Menu");
        adapter.addFragment(new messFeedbackFragment(),"Feedback");
        adapter.addFragment(new messUSTFragment(),"UST");
        viewPager.setAdapter(adapter);


    }
}
