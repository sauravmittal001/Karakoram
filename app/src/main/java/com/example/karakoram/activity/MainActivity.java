package com.example.karakoram.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.karakoram.R;
import com.example.karakoram.parentFragment.HomeFragment;
import com.example.karakoram.parentFragment.MyStuffFragment;
import com.example.karakoram.parentFragment.billFragment;
import com.example.karakoram.parentFragment.messFragment;
import com.example.karakoram.resource.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle toggle;
    private BottomNavigationView navView;
    private NavigationView side_navview;
    private View header;
    private boolean editMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables();
        initViews();
        setViews();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        openFragment(new HomeFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Menu menu = side_navview.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_logout);
        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "loggedOut");
        if (userId.equals("loggedOut"))
            menuItem.setTitle("login/signin");
        else
            menuItem.setTitle("logout");
        TextView nameView = header.findViewById(R.id.user_name);
        TextView entryNumberView = header.findViewById(R.id.user_entry_number);
        nameView.setText(sharedPreferences.getString("userName", ""));
        entryNumberView.setText(sharedPreferences.getString("entryNumber", ""));

        if (editMode) {
            navView.setVisibility(View.GONE);
            openFragment(new messFragment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mdrawer.isDrawerOpen(GravityCompat.START)) {
            mdrawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    private void initVariables() {
        editMode = getIntent().getBooleanExtra("editMode", false);
    }

    private void initViews() {
        navView = findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);
    }

    private void setViews() {
//        setBottomNavigation();
        setNewBottomNav();
        setSideBar();
        ImageView header_image = (ImageView) header.findViewById(R.id.user_image);
        header_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
            }
        });

        mdrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mdrawer, R.string.open, R.string.close);
        mdrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setNewBottomNav() {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("A", R.drawable.icon_edit, R.color.lime);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("B", R.drawable.ic_mess, R.color.lime);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("C", R.drawable.ic_bill, R.color.lime);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("D", R.drawable.ic_dashboard_black_24dp, R.color.lime);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottomNavigation.setBehaviorTranslationEnabled(true);

        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        bottomNavigation.setColored(true);
        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        return openFragment(new HomeFragment());

                    case 1:
                        return openFragment(new messFragment());

                    case 2:
                        return openFragment(new billFragment());

                    case 3:
                        return openFragment(new MyStuffFragment());
                }
                return false;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {

            }
        });
    }

    private void setBottomNavigation() {
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        return openFragment(new HomeFragment());

                    case R.id.navigation_mess:
                        return openFragment(new messFragment());

                    case R.id.navigation_bill:
                        return openFragment(new billFragment());

                    case R.id.navigation_my_stuff:
                        return openFragment(new MyStuffFragment());
                }

                return false;
            }

        };
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private boolean openFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);

            // transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;
    }

    private void setSideBar() {
        side_navview = findViewById(R.id.side_navview);
        header = side_navview.getHeaderView(0);
        side_navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.navigation_complaints) {
                    SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
                    if (sharedPreferences.getString("type", "Student").equals("Admin"))
                        startActivity(new Intent(MainActivity.this, ComplaintActivity.class));
                    else
                        startActivity(new Intent(MainActivity.this, ComplaintFormActivity.class));
                } else if (id == R.id.navigation_about) {
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                } else if (id == R.id.navigation_logout) {
                    if (menuItem.getTitle().equals("logout")) {
                        SharedPreferences sharedPreferences = getSharedPreferences(User.SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        menuItem.setTitle("login/signin");
                        TextView nameView = header.findViewById(R.id.user_name);
                        TextView entryNumberView = header.findViewById(R.id.user_entry_number);
                        nameView.setText("");
                        entryNumberView.setText("");
                        Toast.makeText(getApplicationContext(), "logged out", Toast.LENGTH_SHORT).show();
                    } else
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
                return true;
            }
        });
    }

}




