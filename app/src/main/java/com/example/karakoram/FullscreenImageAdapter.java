package com.example.karakoram;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class FullscreenImageAdapter extends FragmentStatePagerAdapter {
    List<Integer> bill;

    public FullscreenImageAdapter(@NonNull FragmentManager fm, List<Integer>bill) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.bill=bill;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        FullscreenImageFragment fragment=new FullscreenImageFragment();
        Bundle b=new Bundle();
        b.putInt("bill",bill.get(position));
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getCount() {
        return bill.size();
    }
}
