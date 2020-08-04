package com.example.karakoram.activity;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.karakoram.R;
import com.example.karakoram.childFragment.signin.LoginFragment;
import com.example.karakoram.childFragment.signin.SigninFragment;
import com.example.karakoram.views.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Locale;

public class SignInActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        viewPager = findViewById(R.id.vp_menu);
        tabLayout = findViewById(R.id.tb_counter_menu);
        setViews();
    }

    private void setViews() {
        tabLayout.setupWithViewPager(viewPager, true);
        this.viewPager.getLayoutParams().height = (Resources.getSystem().getDisplayMetrics().heightPixels * 2) / 3;
        this.setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "Login");
        adapter.addFragment(new SigninFragment(), "Signup");
        viewPager.setAdapter(adapter);
    }

    public void updatePagerView() {
        int nextPosition = 1;
        viewPager.setCurrentItem(nextPosition, false);
    }

}